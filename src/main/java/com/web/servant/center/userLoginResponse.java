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
public class userLoginResponse {

	@TarsStructProperty(order = 1, isRequire = true)
	public String typeId = "";
	@TarsStructProperty(order = 2, isRequire = true)
	public int requestId = 0;
	@TarsStructProperty(order = 3, isRequire = true)
	public int errcode = 0;
	@TarsStructProperty(order = 4, isRequire = true)
	public String errmsg = "";

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

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public userLoginResponse() {
	}

	public userLoginResponse(String typeId, int requestId, int errcode, String errmsg) {
		this.typeId = typeId;
		this.requestId = requestId;
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(typeId);
		result = prime * result + TarsUtil.hashCode(requestId);
		result = prime * result + TarsUtil.hashCode(errcode);
		result = prime * result + TarsUtil.hashCode(errmsg);
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
		if (!(obj instanceof userLoginResponse)) {
			return false;
		}
		userLoginResponse other = (userLoginResponse) obj;
		return (
			TarsUtil.equals(typeId, other.typeId) &&
			TarsUtil.equals(requestId, other.requestId) &&
			TarsUtil.equals(errcode, other.errcode) &&
			TarsUtil.equals(errmsg, other.errmsg) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(typeId, 1);
		_os.write(requestId, 2);
		_os.write(errcode, 3);
		_os.write(errmsg, 4);
	}


	public void readFrom(TarsInputStream _is) {
		this.typeId = _is.readString(1, true);
		this.requestId = _is.read(requestId, 2, true);
		this.errcode = _is.read(errcode, 3, true);
		this.errmsg = _is.readString(4, true);
	}

}
