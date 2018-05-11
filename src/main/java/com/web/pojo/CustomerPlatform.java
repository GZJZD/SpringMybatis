package com.web.pojo;

/**
 * 客户数据平台
 * @author riseSun
 *
 * 2017年12月23日上午1:00:09
 */

public class CustomerPlatform {
	
	private Long id;
	//平台名称
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

	public CustomerPlatform(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
