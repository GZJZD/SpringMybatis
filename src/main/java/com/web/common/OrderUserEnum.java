package com.web.common;

public class OrderUserEnum {
    public enum openOrClose{
        open("开仓",0),close("平仓",1);
        private String name;
        private int code;

        private    openOrClose(String name, int code) {
            this.name = name;
            this.code = code;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
