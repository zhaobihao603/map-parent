package com.imap.cloud.common.entity.system;

import java.util.Date;

import com.imap.cloud.common.dto.DataDTO;
import com.imap.cloud.common.dto.UnitDTO;
import com.imap.cloud.common.entity.map.MapLayer;

/**
 * 单位(POI及学校单位的实体)
 * @author Bge
 * @since 2016-11-02
 * 
 */
public class UnitLabel {
	private String id;
	private String name;
	private String englishName;
	private String address;
	private String enAddress;
	private Date createTime;
	private String createUser;
	private boolean deleted;
	private String description;
	private Date updateTime;
	private String updateUser;
	private String url;
	private Double x;
	private Double y;
	private String keywords;
	private String linkMan;
	private String mobile;
	private DictionaryItem mainType;
	private DictionaryItem type;
	private String entityId;
	private String imageId;
	private MapLayer area;
	private String showTime;
	private String jdPhone;
	private String comments;
	private Integer seq;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEnAddress() {
		return enAddress;
	}
	public void setEnAddress(String enAddress) {
		this.enAddress = enAddress;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public DictionaryItem getMainType() {
		return mainType;
	}
	public void setMainType(DictionaryItem mainType) {
		this.mainType = mainType;
	}
	public DictionaryItem getType() {
		return type;
	}
	public void setType(DictionaryItem type) {
		this.type = type;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public MapLayer getArea() {
		return area;
	}
	public void setArea(MapLayer area) {
		this.area = area;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getJdPhone() {
		return jdPhone;
	}
	public void setJdPhone(String jdPhone) {
		this.jdPhone = jdPhone;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public DataDTO toDataDTO(){
		DataDTO data = new DataDTO();
		data.setId(this.id);
		data.setName(this.name);
		data.setEname(this.englishName);
		data.setAddress(this.address);
		data.setEnAddress(this.enAddress);
		data.setMobile(this.mobile);
		data.setX(this.x);
		data.setY(this.y);
		data.setDescription(this.description);
		data.setUrl(this.url);
		if(area != null && area.getId() !=null)
			data.setArea(area.getId());
		return data;
	}
	public UnitDTO toUnitDTO(){
		UnitDTO unitDTO = new UnitDTO();
		unitDTO.setId(this.id);
		unitDTO.setName(this.name);
		unitDTO.setEnglishName(this.englishName);
		unitDTO.setAddress(this.address);
		unitDTO.setEnAddress(this.enAddress);
		unitDTO.setCreateUser(this.createUser);
		unitDTO.setDeleted(this.deleted);
		unitDTO.setDescription(this.description);
		unitDTO.setUpdateTime(this.updateTime);
		unitDTO.setUpdateUser(this.updateUser);
		unitDTO.setUrl(this.url);
		unitDTO.setX(this.x);
		unitDTO.setY(this.y);
		unitDTO.setKeywords(this.keywords);
		unitDTO.setLinkMan(this.linkMan);
		unitDTO.setMobile(this.mobile);
		unitDTO.setMainTypeId(this.mainType.getId());
		unitDTO.setTypeId(this.type.getId());
		unitDTO.setEntityId(this.entityId);
		unitDTO.setImageId(this.imageId);
		unitDTO.setAreaId(this.area.getId());
		unitDTO.setShowTime(this.showTime);
		unitDTO.setJdPhone(this.jdPhone);
		unitDTO.setComments(this.comments);
		unitDTO.setSeq(this.seq);
		
		return unitDTO;
	}
}
