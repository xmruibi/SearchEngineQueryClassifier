package com.POJO;

// Generated Dec 4, 2014 2:33:36 PM by Hibernate Tools 3.4.0.CR1

/**
 * ActionInfo generated by hbm2java
 */
public class ActionInfo implements java.io.Serializable {

	private Integer actionId;
	private String logid;
	private String username;
	private String type;
	private String refer;
	private int groupid;
	private int taskid;
	private String time;
	private String query;
	private String title;
	private String url;
	private String abstract_;
	private String content;

	public ActionInfo() {
	}

	public ActionInfo(String logid, String username, String type, String refer,
			int groupid, int taskid, String time, String query, String title,
			String url, String abstract_, String content) {
		this.logid = logid;
		this.username = username;
		this.type = type;
		this.refer = refer;
		this.groupid = groupid;
		this.taskid = taskid;
		this.time = time;
		this.query = query;
		this.title = title;
		this.url = url;
		this.abstract_ = abstract_;
		this.content = content;
	}

	public Integer getActionId() {
		return this.actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getLogid() {
		return this.logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefer() {
		return this.refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}

	public int getGroupid() {
		return this.groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getTaskid() {
		return this.taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAbstract_() {
		return this.abstract_;
	}

	public void setAbstract_(String abstract_) {
		this.abstract_ = abstract_;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
