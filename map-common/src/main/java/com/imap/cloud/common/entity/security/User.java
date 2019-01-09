package com.imap.cloud.common.entity.security;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户实体
 * @author  冯林
 * UserDetails 接口
 * 返回的用户类型,必须实现UserDetails接口, 把相应的字段交给 UserDetails接口对应的字段即可
 * getAuthorities 此方法用来获取相应的角色集合,返回的是当前角色的名称
 */
public class User implements UserDetails{

	private static final long serialVersionUID = -5162955579563727413L;
	
    private String id;							/* 编号UUID */ 
    private String login;						/* 登录名  */              
    private String name; 						/* 姓名  */               
    private String password;					/* 密码 */          
    private String mobile;						/* 手机号 */
    private String mail;						/* 邮件 */
    private String deleted;						/* 逻辑删除 */	
    private String description;					/* 备注 ，同时也用于下拉查询机构时临时封装数据使用*/
    private String isAccountNonExpired;	 		/* 帐户未过期 0 == true  1 == false*/  
    private String isAccountNonLocked;			/* 帐户不被锁定 */
    private String isCredentialsNonExpired;		/* 凭证不过期 */
    private String isEnabled;        			/* 帐户是否启用 */
  
    private Set<Organizational> organizationalSet;	/* 机构   */
    private Set<Role> roleSet;				 	/* 角色信息 */
    private Set<Permission>	permissionSet;		/* 该用户对应的权限 */ 
	
	public User() {
		super();
	}
	
	public Set<Organizational> getOrganizationalSet() {
		return organizationalSet;
	}

	public void setOrganizationalSet(Set<Organizational> organizationalSet) {
		this.organizationalSet = organizationalSet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getIsAccountNonExpired() {
		return isAccountNonExpired;
	}

	public void setIsAccountNonExpired(String isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	public String getIsAccountNonLocked() {
		return isAccountNonLocked;
	}

	public void setIsAccountNonLocked(String isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public String getIsCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	public void setIsCredentialsNonExpired(String isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Permission> getPermissionSet() {
		return permissionSet;
	}

	public void setPermissionSet(Set<Permission> permissionSet) {
		this.permissionSet = permissionSet;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", name=" + name
				+ ", mobile=" + mobile + ", mail=" + mail + ", description="
				+ description + ", isAccountNonExpired=" + isAccountNonExpired
				+ ", isAccountNonLocked=" + isAccountNonLocked
				+ ", isCredentialsNonExpired=" + isCredentialsNonExpired
				+ ", isEnabled=" + isEnabled + "]";
	}

	//用户对应的角色
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roleSet;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		if(StringUtils.isNotBlank(isAccountNonExpired)){
			if(isAccountNonExpired.equals("0")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean isAccountNonLocked() {
		if(StringUtils.isNotBlank(isAccountNonLocked)){
			if(isAccountNonLocked.equals("0")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean isCredentialsNonExpired() {
		if(StringUtils.isNotBlank(isCredentialsNonExpired)){
			if(isCredentialsNonExpired.equals("0")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean isEnabled() {
		if(StringUtils.isNotBlank(isEnabled)){
			if(isEnabled.equals("0")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

   
}