package com.imap.cloud.common.service.map;

import java.awt.image.BufferedImage;

public interface BMapTileService {

	BufferedImage getImgByArea(Integer minx, Integer maxx, Integer miny,
			Integer maxy, Integer zoom) throws Exception;
	
	BufferedImage getTDImgByArea(Integer minx, Integer maxx, Integer miny,
			Integer maxy, Integer zoom) throws Exception;

}
