package com.imap.cloud.common.service.map.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.imap.cloud.common.service.map.BMapTileService;
import com.imap.cloud.common.util.DesUtils;
import com.imap.cloud.common.util.HttpClientUtil;

@Service("bmapTileService")
public class BMapTileServiceImpl implements BMapTileService {

	@Override
	public BufferedImage getImgByArea(Integer minx, Integer maxx, Integer miny,
			Integer maxy,Integer zoom) throws Exception {
		//图片目标长宽
		int dst_width = Math.abs((maxx-minx+1)<<8);
		int dst_height = Math.abs((maxy-miny+1)<<8);
		BufferedImage tileImg = new BufferedImage(dst_width, dst_height,  
				BufferedImage.TYPE_INT_RGB); 
		StringBuilder urlStr = null;
		BufferedImage image = null;
		InputStream in = null;
		for(int i = maxy,startY=0;i>=miny;i--,startY++){
			for(int j = minx,startX=0;j<=maxx;j++,startX++){
				urlStr = new StringBuilder();
				urlStr.append("http://online1.map.bdimg.com/tile/?qt=tile");
				urlStr.append("&x="+j+"&y="+i+"&z="+zoom);
				urlStr.append("&styles=pl&scaler=1");
				
				in = HttpClientUtil.httpGetImage(urlStr.toString());  
				image = ImageIO.read(in);
				tileImg.setRGB(startX<<8, startY<<8, 256, 256,  
						image.getRGB(0, 0, 256, 256,new int[256*256], 0, 256), 0, 256);  
			}
		}
		return tileImg;
	}

	@Override
	public BufferedImage getTDImgByArea(Integer minx, Integer maxx,
			Integer miny, Integer maxy, Integer zoom) throws Exception {
		//图片目标长宽
		int dst_width = Math.abs((maxx-minx+1)<<8);
		int dst_height = Math.abs((maxy-miny+1)<<8);
		BufferedImage tileImg = new BufferedImage(dst_width, dst_height,  
				BufferedImage.TYPE_INT_RGB); 
		StringBuilder urlStr = null;
		BufferedImage image = null;
		InputStream in = null;
		for(int i = miny,startY=0;i<=maxy;i++,startY++){
			for(int j = minx,startX=0;j<=maxx;j++,startX++){
				urlStr = new StringBuilder();
				urlStr.append("http://t0.tianditu.com/DataServer?T=vec_c");
				urlStr.append("&x="+j+"&y="+i+"&l="+zoom);
				in = HttpClientUtil.httpGetImage(urlStr.toString());  
				if(in == null)continue;
				image = ImageIO.read(in);
				tileImg.setRGB(startX<<8, startY<<8, 256, 256,  
						image.getRGB(0, 0, 256, 256,new int[256*256], 0, 256), 0, 256);  
			}
		}
		return tileImg;
	}
}
