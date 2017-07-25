package com.lv.rx2demo.model;
/**
 * 
* @Title: UpdateBean.java 
* @Description: 更新的实体
* @author 吕勇   
* @date 2016-2-2 下午1:07:14
 */
public class UpdateBean {
	
	private String content;
	private int new_version;
	private String gmt_create;
	private int least_version;
	private String url;
	private int is_force_update;
	private String version_name;

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getNew_version() {
		return new_version;
	}
	public void setNew_version(int new_version) {
		this.new_version = new_version;
	}
	public String getGmt_create() {
		return gmt_create;
	}
	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}
	public int getLeast_version() {
		return least_version;
	}
	public void setLeast_version(int least_version) {
		this.least_version = least_version;
	}
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isIs_force_update() {
		return is_force_update==1;
	}
	
	public int getIs_force_update() {
		return is_force_update;
	}
	
	public void setIs_force_update(int is_force_update) {
		this.is_force_update = is_force_update;
	}

	@Override
	public String toString() {
		return "UpdateBean{" +
				"content='" + content + '\'' +
				", new_version=" + new_version +
				", gmt_create='" + gmt_create + '\'' +
				", least_version=" + least_version +
				", url='" + url + '\'' +
				", is_force_update=" + is_force_update +
				", version_name='" + version_name + '\'' +
				'}';
	}
}
