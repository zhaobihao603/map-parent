package com.imap.cloud.common.service.map;

public interface MapTransformService {
	
	double[] utm2wgs84(Double x,Double y);
	
	double[] wgs842utm(Double tx,Double ty);

}
