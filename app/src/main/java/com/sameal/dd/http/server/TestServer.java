package com.sameal.dd.http.server;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 测试环境
 */
public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
//        return "http://dong.zdqyl.com/";
//        return "http://cs_api.zdqyl.com/";
        return "http://csapi.zdqyl.com/";
//        return "https://dong.zdqyl.com/";
    }
}