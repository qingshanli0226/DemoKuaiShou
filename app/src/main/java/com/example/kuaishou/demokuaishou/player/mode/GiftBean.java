package com.example.kuaishou.demokuaishou.player.mode;

import java.util.List;

public class GiftBean {

    /**
     * code : 200
     * msg : 请求成功
     * result : [{"gif_url":"/gif/1.gif","price":"100","gif_file":"/gif/1001.gif"},{"gif_url":"/gif/2.gif","price":"200","gif_file":"/gif/1002.gif"},{"gif_url":"/gif/3.gif","price":"300","gif_file":"/gif/1003.gif"},{"gif_url":"/gif/4.gif","price":"400","gif_file":"/gif/1004.gif"},{"gif_url":"/gif/5.gif","price":"500","gif_file":"/gif/1005.gif"},{"gif_url":"/gif/6.gif","price":"600","gif_file":"/gif/1006.gif"}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * gif_url : /gif/1.gif
         * price : 100
         * gif_file : /gif/1001.gif
         */

        private String gif_url;
        private String price;
        private String gif_file;

        public String getGif_url() {
            return gif_url;
        }

        public void setGif_url(String gif_url) {
            this.gif_url = gif_url;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGif_file() {
            return gif_file;
        }

        public void setGif_file(String gif_file) {
            this.gif_file = gif_file;
        }
    }
}
