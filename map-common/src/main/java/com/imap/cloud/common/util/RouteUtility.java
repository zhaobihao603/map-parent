package com.imap.cloud.common.util;

public class RouteUtility {
	private double mpx;//0.43738
	private double mpy;//0.31475
	private static RouteUtility routeUtility;
	private double overlook =30;//投影
	private double rotate =0;//旋转
	private double scale =100000;//分辨率

	public void SetXYScale(double mpx, double mpy) {
		this.mpx = mpx;
		this.mpy = mpy;
	}

	public static RouteUtility getInstance() {
		if (routeUtility == null) {
			routeUtility = new RouteUtility();
		}
		return routeUtility;
	}

	// 获取坐标距离
	public double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double getGISDistance2(double x1, double y1, double x2, double y2) {
		y1 = y1/Math.sin(this.overlook);
		y2 = y2/Math.sin(this.overlook);//投影30度
		x1=Math.cos(-this.rotate)*x1-Math.sin(-this.rotate)*y1;
		y1=Math.cos(-this.rotate)*y1+Math.sin(-this.rotate)*x1;
		
		x2=Math.cos(-this.rotate)*x2-Math.sin(-this.rotate)*y2;
		y2=Math.cos(-this.rotate)*y2+Math.sin(-this.rotate)*x2;
		return this.scale*Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1-y2));
	}
	
	// 获取gis坐标距离
	public double getGISDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) * mpx * mpy + (y1 - y2) * (y1 - y2) * mpx * mpy);
	}
}
