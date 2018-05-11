package com.web.pojo;

/**
 * 交易账号
 * @author riseSun
 *
 * 2017年12月23日下午5:48:16
 */

public class Account {
	private Long id;
	//用户名
	private String username;
	//密码
	private String password;
	//交易平台
	private TradePlatform platform;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TradePlatform getPlatform() {
		return platform;
	}
	public void setPlatform(TradePlatform platform) {
		this.platform = platform;
	}

	public Account(Long id, String username, String password, TradePlatform platform) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.platform = platform;
	}

	public Account() {
		this.id = id;
	}
}
