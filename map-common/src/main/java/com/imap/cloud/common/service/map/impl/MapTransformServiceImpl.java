package com.imap.cloud.common.service.map.impl;

import org.springframework.stereotype.Service;

import com.imap.cloud.common.aop.annotation.LogDescription;
import com.imap.cloud.common.service.map.MapTransformService;
import com.imap.cloud.common.util.EvilTransform;

@LogDescription(name="坐标转换服务")
@Service
public class MapTransformServiceImpl implements MapTransformService {

	@Override
	public double[] utm2wgs84(Double x,Double y) {
		double deflection = 30d;//北方向顺时针偏转30
		double angle = 45d;//俯视角度45
		double x_ =x;
		double y_ = y/Math.cos(angle*2*Math.PI/360);//将y不倾斜45度
		x = Math.cos(deflection*2*Math.PI/360)*x_-Math.sin(deflection*2*Math.PI/360)*y_;//得到未旋转UTM的x坐标
		y = Math.sin(deflection*2*Math.PI/360)*x_+Math.cos(deflection*2*Math.PI/360)*y_;//得到未旋转UTM的y坐标
		double[] lonlat = EvilTransform.UTMXYToLonLat(x, y, 51, false);
		return lonlat;
	}

	@Override
	public double[] wgs842utm(Double tx, Double ty) {
		double[] utm = EvilTransform.LonLatToUTMXY(tx,ty);//经纬度转为UTMxy
		double deflection = 30d;//北方向顺时针偏转30
		double angle = 45d;//俯视角度45
		double x = Math.cos(deflection*2*Math.PI/360)*utm[0]+Math.sin(deflection*2*Math.PI/360)*utm[1];//UTMx旋转30
		double y_ = -Math.sin(deflection*2*Math.PI/360)*utm[0]+Math.cos(deflection*2*Math.PI/360)*utm[1];//UTMy旋转30
		double y = Math.cos(angle*2*Math.PI/360)*y_;//UTMy旋转30再倾斜45度
		double[] xy = new double[]{x,y};
		return xy;
	}

}
