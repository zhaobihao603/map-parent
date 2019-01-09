package com.imap.cloud.common.cache.lucene;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockObtainFailedException;

import com.imap.cloud.common.cache.lucene.util.Constants;


public class RedisLockFactory extends LockFactory{
	private static final Set<String> LOCK_HELD = Collections.synchronizedSet(new HashSet<String>());
	private final Path lockFileDirectory = Paths.get(Constants.LOCK_FILE_PATH); // The underlying filesystem directory

	RedisLockFactory() throws IOException {
	    if (!Files.isDirectory(this.lockFileDirectory)) {
	        Files.createDirectories(lockFileDirectory);  // create directory, if it doesn't exist
	    }
	}

	@Override
	public Lock obtainLock(Directory dir, String lockName) throws IOException {
		if (dir == null) {
            throw new NullPointerException("dir");
        }
		if (!(dir instanceof RedisDirectory)) {
            throw new IllegalArgumentException("Expect argument of type [" + RedisDirectory.class.getName() + "]!");
        }
        Path lockFile = lockFileDirectory.resolve(lockName);
        try {
            Files.createFile(lockFile);
            //log.debug("Lock file path = {}", lockFile.toFile().getAbsolutePath());
        } catch (IOException ignore) {
            //ignore
            //log.debug("Lock file already exists!");
        }
        final Path realPath = lockFile.toRealPath();
        final FileTime creationTime = Files.readAttributes(realPath, BasicFileAttributes.class).creationTime();
        if (LOCK_HELD.add(realPath.toString())) {
            FileChannel fileChannel = null;
            FileLock lock = null;
            try {
                fileChannel = FileChannel.open(realPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                lock = fileChannel.tryLock();
                if (lock != null) {
                    return new RedisLock(lock, fileChannel, realPath, creationTime);
                } else {
                    throw new LockObtainFailedException("Lock held by another program: " + realPath);
                }
            } finally {
                if (lock == null) {
                    IOUtils.closeQuietly(fileChannel);
                    clearLockHeld(realPath);
                }
            }
        } else {
            throw new LockObtainFailedException("Lock held by this virtual machine: " + realPath);
        }
	}
	
	private static void clearLockHeld(Path realPath) {
        boolean remove = LOCK_HELD.remove(realPath.toString());
        if (!remove) {
            throw new AlreadyClosedException("Lock path was cleared but never marked as held: " + realPath);
        }
    }
	
	private static class RedisLock extends Lock {
        final FileLock lock;
        final FileChannel fileChannel;
        final Path path;
        final FileTime creationTime;
        volatile boolean closed;

        RedisLock(FileLock lock, FileChannel channel, Path path, FileTime creationTime) {
            this.lock = lock;
            this.fileChannel = channel;
            this.path = path;
            this.creationTime = creationTime;
        }

        @Override
        public void close() throws IOException {
            if (closed) {
                return;
            }
            try (FileChannel channel = this.fileChannel; FileLock lock = this.lock) {
                Objects.requireNonNull(channel);
                Objects.requireNonNull(lock);
            } finally {
                closed = true;
                clearLockHeld(path);
            }
        }

        @Override
        public void ensureValid() throws IOException {
            if (closed) {
                throw new AlreadyClosedException("Lock instance already released: " + this);
            }
            if (!LOCK_HELD.contains(path.toString())) {
                throw new AlreadyClosedException("Lock path unexpectedly cleared from map: " + this);
            }
            if (!lock.isValid()) {
                throw new AlreadyClosedException("FileLock invalidated by an external force: " + this);
            }
            long size = fileChannel.size();
            if (size != 0) {
                throw new AlreadyClosedException("Unexpected lock file size: " + size + ", (lock=" + this + ")");
            }
            FileTime ctime = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
            if (!creationTime.equals(ctime)) {
                throw new AlreadyClosedException("Underlying file changed by an external force at " + creationTime + ", " +
                        "(lock=" + this + ")");
            }
        }

        @Override
        public String toString() {
            return "RedisLock(path=" + path + ",impl=" + lock + ",ctime=" + creationTime + ")";
        }
    }

}
