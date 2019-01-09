package com.imap.cloud.common.entity.arboretum;

import java.util.List;

/**
 * 虚拟植物园 树林实体
 * @author 冯林
 *
 */
public class ArboretumShu {
    private String id;				//自增长主键
    private String chinesename;  	//中文学名
    private String genera;			//科属
    private String shutype;		//类型
    private String latinname;		//拉丁学名
    private String nickname;		//别称
    private String imgurl;			//植物图片
    private String shuzong;			//树种
    private Integer shuage;			//树龄
    private String shuceng;			//冠层
    private String shuheight;		//树高
    private String x;				//x
    private String y;				//y
    private String characteristics;	//形态特征
    private String habitat;			//产地生境
    private String mainvalue;		//主要价值
    private String pestcontrol;		//病虫防治
    private String donationunit;	//捐赠单位
    private String growthhabit;		//生长习性
    private String area;			//校区
    
    private List<ArboretumZbgl> zbglList;	//树木对应的植被规划管理
    
    public List<ArboretumZbgl> getZbglList() {
		return zbglList;
	}

	public void setZbglList(List<ArboretumZbgl> zbglList) {
		this.zbglList = zbglList;
	}

	public String getCharacteristics() {
        return characteristics;
    }
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics == null ? null : characteristics.trim();
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat == null ? null : habitat.trim();
    }

    public String getMainvalue() {
        return mainvalue;
    }

    public void setMainvalue(String mainvalue) {
        this.mainvalue = mainvalue == null ? null : mainvalue.trim();
    }

    public String getPestcontrol() {
        return pestcontrol;
    }

    public void setPestcontrol(String pestcontrol) {
        this.pestcontrol = pestcontrol == null ? null : pestcontrol.trim();
    }

    public String getDonationunit() {
        return donationunit;
    }

    public void setDonationunit(String donationunit) {
        this.donationunit = donationunit == null ? null : donationunit.trim();
    }

    public String getGrowthhabit() {
        return growthhabit;
    }

    public void setGrowthhabit(String growthhabit) {
        this.growthhabit = growthhabit == null ? null : growthhabit.trim();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getChinesename() {
        return chinesename;
    }

    public void setChinesename(String chinesename) {
        this.chinesename = chinesename == null ? null : chinesename.trim();
    }

    public String getGenera() {
        return genera;
    }

    public void setGenera(String genera) {
        this.genera = genera == null ? null : genera.trim();
    }

    public String getShutype() {
		return shutype;
	}

	public void setShutype(String shutype) {
		this.shutype = shutype;
	}

	public String getLatinname() {
        return latinname;
    }

    public void setLatinname(String latinname) {
        this.latinname = latinname == null ? null : latinname.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getShuzong() {
        return shuzong;
    }

    public void setShuzong(String shuzong) {
        this.shuzong = shuzong == null ? null : shuzong.trim();
    }

    public Integer getShuage() {
        return shuage;
    }

    public void setShuage(Integer shuage) {
        this.shuage = shuage;
    }

    public String getShuceng() {
        return shuceng;
    }

    public void setShuceng(String shuceng) {
        this.shuceng = shuceng == null ? null : shuceng.trim();
    }

    public String getShuheight() {
        return shuheight;
    }

    public void setShuheight(String shuheight) {
        this.shuheight = shuheight == null ? null : shuheight.trim();
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x == null ? null : x.trim();
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y == null ? null : y.trim();
    }

    
}