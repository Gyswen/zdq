package com.sameal.dd.http.server;

import com.hjq.http.config.IRequestServer;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 正式环境
 */
public class ReleaseServer implements IRequestServer {

    @Override
    public String getHost() {
        return "https://dong.zdqyl.com/";
//        return "http://dong.weifangtianxia.com/";
    }

    @Override
    public String getPath() {
        return "";
    }
}