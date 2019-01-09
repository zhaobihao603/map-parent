package com.imap.cloud.common.dto;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1297844687482803158L;

	private Double x;
	private Double y;

	public Point() {

	}

	public Point(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	@Override
    public boolean equals(Object object) {
        if (object instanceof Point) {
        	Point pt = (Point) object;
            if (pt.getX() == null || pt.getY() == null) {
                return false;
            }
            if (pt.getX().equals(x) && pt.getY().equals(y)) {
                return true;
            }
        }
        return false;
    }
}
