package com.web.pojo;

/**
 * 交易平台
 * @author riseSun
 *
 * 2017年12月23日下午5:44:30
 */
public class TradePlatform {
	private Long id;
	//交易平台名称
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public TradePlatform(String name) {
		this.name = name;
	}
}
