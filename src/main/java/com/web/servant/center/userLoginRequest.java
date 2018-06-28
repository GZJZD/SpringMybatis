// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.web.servant.center;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

@TarsStruct
public class userLoginRequest {

	@TarsStructProperty(order = 1, isRequire = true)
	public String typeId = "";
	@TarsStructProperty(order = 2, isRequire = true)
	public int requestId = 0;
	@TarsStructProperty(order = 3, isRequire = true)
	public String brokerId = "";
	@TarsStructProperty(order = 4, isRequire = true)
	public String userId = "";
	@TarsStructProperty(order = 5, isRequire = true)
	public String password = "";
	@TarsStructProperty(order = 6, isRequire = false)
	public String userProductInfo = "";
	@TarsStructProperty(order = 7, isRequire = false)
	public int forcedreset = 0;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserProductInfo() {
		return userProductInfo;
	}

	public void setUserProductInfo(String userProductInfo) {
		this.userProductInfo = userProductInfo;
	}

	public int getForcedreset() {
		return forcedreset;
	}

	public void setForcedreset(int forcedreset) {
		this.forcedreset = forcedreset;
	}

	public userLoginRequest() {
	}

	public userLoginRequest(String typeId, int requestId, String brokerId, String userId, String password, String userProductInfo, int forcedreset) {
		this.typeId = typeId;
		this.requestId = requestId;
		this.brokerId = brokerId;
		this.userId = userId;
		this.password = password;
		this.userProductInfo = userProductInfo;
		this.forcedreset = forcedreset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(typeId);
		result = prime * result + TarsUtil.hashCode(requestId);
		result = prime * result + TarsUtil.hashCode(brokerId);
		result = prime * result + TarsUtil.hashCode(userId);
		result = prime * result + TarsUtil.hashCode(password);
		result = prime * result + TarsUtil.hashCode(userProductInfo);
		result = prime * result + TarsUtil.hashCode(forcedreset);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof userLoginRequest)) {
			return false;
		}
		userLoginRequest other = (userLoginRequest) obj;
		return (
			TarsUtil.equals(typeId, other.typeId) &&
			TarsUtil.equals(requestId, other.requestId) &&
			TarsUtil.equals(brokerId, other.brokerId) &&
			TarsUtil.equals(userId, other.userId) &&
			TarsUtil.equals(password, other.password) &&
			TarsUtil.equals(userProductInfo, other.userProductInfo) &&
			TarsUtil.equals(forcedreset, other.forcedreset) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(typeId, 1);
		_os.write(requestId, 2);
		_os.write(brokerId, 3);
		_os.write(userId, 4);
		_os.write(password, 5);
		if (null != userProductInfo) {
			_os.write(userProductInfo, 6);
		}
		_os.write(forcedreset, 7);
	}


	public void readFrom(TarsInputStream _is) {
		this.typeId = _is.readString(1, true);
		this.requestId = _is.read(requestId, 2, true);
		this.brokerId = _is.readString(3, true);
		this.userId = _is.readString(4, true);
		this.password = _is.readString(5, true);
		this.userProductInfo = _is.readString(6, false);
		this.forcedreset = _is.read(forcedreset, 7, false);
	}

}
