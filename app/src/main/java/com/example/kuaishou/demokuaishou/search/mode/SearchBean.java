package com.example.kuaishou.demokuaishou.search.mode;

import java.util.List;

public class SearchBean {


    /**
     * code : 200
     * msg : 请求成功
     * result : [{"vedioUrl":"http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4","vedioId":8,"userId":10018,"coverImg":"http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1109/24/c6/9067095_9067095_1316861219359.jpg"}]
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
         * vedioUrl : http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4
         * vedioId : 8
         * userId : 10018
         * coverImg : http://img.pconline.com.cn/images/upload/upc/tx/photoblog/1109/24/c6/9067095_9067095_1316861219359.jpg
         */

        private String vedioUrl;
        private int vedioId;
        private int userId;
        private String coverImg;

        public String getVedioUrl() {
            return vedioUrl;
        }

        public void setVedioUrl(String vedioUrl) {
            this.vedioUrl = vedioUrl;
        }

        public int getVedioId() {
            return vedioId;
        }

        public void setVedioId(int vedioId) {
            this.vedioId = vedioId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }
    }
}
