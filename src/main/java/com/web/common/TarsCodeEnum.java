package com.web.common;

public  class TarsCodeEnum {
    public  enum TarsCodeStatus{
        //跟单
    	TARS_SERVER_SUCCESS("服务器端处理成功",0),
    	TARS_SERVER_DECODE_ERR("服务器端解码异常",-1),
    	TARS_SERVER_ENCODE_ERR("服务器端编码异常",-2),
    	TARS_SERVER_NOFUNC_ERR("服务器端没有该函数",-3),
    	TARS_SERVER_NOSERVANT_ERR("服务器端没有该Servant对象",-4),
    	TARS_SERVER_RESETGRID("服务器端灰度状态不一致",-5),
    	TARS_SERVER_QUEUE_TIMEOUT("服务器队列超过限制",-6),
    	TARS_ASYNCCALL_TIMEOUT("异步调用超时",-7),
    	TARS_INVOKE_TIMEOUT("调用超时",-7),
    	TARS_PROXY_CONNECT_ERR("proxy链接异常",-8),
    	TARS_SERVER_OVERLOAD("服务器端超负载,超过队列长度",-9),
    	TARS_ADAPTER_NULL("客户端选路为空，服务不存在或者所有服务down掉了",-10),
    	TARS_INVOKEBY_INVALIDESET("客户端按set规则调用非法",-11),
    	TARS_CLIENT_DECODE_ERR("/客户端解码异常",-12),
    	TARS_SERVER_UNKNOWN_ERR("服务器端位置异常",-99)

        ;






        private String name;
        private Integer index;
        TarsCodeStatus(String name, Integer index) {
            this.name = name;
            this.index = index;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }
    }
}
