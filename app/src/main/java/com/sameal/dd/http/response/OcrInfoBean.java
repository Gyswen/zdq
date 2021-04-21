package com.sameal.dd.http.response;

import java.io.Serializable;

public class OcrInfoBean implements Serializable {

    /**
     * code : 200
     * msg : ok
     * result : {"taskId":"d33c0d9f17aa4e16abbe7cdb1ad2d4fe","picType":1,"avatar":"http://nos.netease.com/authentication/04ba3c9ad3ad4968a5bd7b63f40421a6avatar?Signature=oMLNMxvJwY7Cfwdh2Cj2WU1MhgShZ9M6mxtqUX1s4e0%3D&Expires=1649299037&NOSAccessKeyId=6916763c068e49bb9c235018e2c88911","status":1,"reasonType":1,"similarityScore":0.97,"faceMatched":1}
     */

    private int code;
    private String msg;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * taskId : d33c0d9f17aa4e16abbe7cdb1ad2d4fe
         * picType : 1
         * avatar : http://nos.netease.com/authentication/04ba3c9ad3ad4968a5bd7b63f40421a6avatar?Signature=oMLNMxvJwY7Cfwdh2Cj2WU1MhgShZ9M6mxtqUX1s4e0%3D&Expires=1649299037&NOSAccessKeyId=6916763c068e49bb9c235018e2c88911
         * status : 1
         * reasonType : 1
         * similarityScore : 0.97
         * faceMatched : 1
         */

        private String taskId;
        private int picType;
        private String avatar;
        private int status;
        private int reasonType;
        private double similarityScore;
        private int faceMatched;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getPicType() {
            return picType;
        }

        public void setPicType(int picType) {
            this.picType = picType;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getReasonType() {
            return reasonType;
        }

        public void setReasonType(int reasonType) {
            this.reasonType = reasonType;
        }

        public double getSimilarityScore() {
            return similarityScore;
        }

        public void setSimilarityScore(double similarityScore) {
            this.similarityScore = similarityScore;
        }

        public int getFaceMatched() {
            return faceMatched;
        }

        public void setFaceMatched(int faceMatched) {
            this.faceMatched = faceMatched;
        }
    }
}
