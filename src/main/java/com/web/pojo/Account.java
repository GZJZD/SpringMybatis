package com.web.pojo;

/**
 * 交易账号
 * @author riseSun
 *
 * 2017年12月23日下午5:48:16
 */

public class Account {


	private Long id;
	//账号
	private String account;
	//密码
	private String password;
	//交易平台
	private Platform platform;
	//代理人
	private Agent agent;
	//创建人
	private String createUser;
	//创建时间
	private String createTime;
	//修改人
	private String modifyUser;
	//修改时间
	private String modifyTime;
	//状态
	private Integer status;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Account(Long id, String account, String password, Platform platform) {
		this.id = id;
		this.account = account;
		this.password = password;
		this.platform = platform;
	}

	public Account() {

	}
}
