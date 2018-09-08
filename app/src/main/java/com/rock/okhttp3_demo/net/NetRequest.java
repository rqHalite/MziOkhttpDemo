package com.rock.okhttp3_demo.net;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.rock.okhttp3_demo.https.MyOkHttp;
import com.rock.okhttp3_demo.https.response.IResponseHandler;

/**
 * Created by Rock on 2018/4/28.
 */

public class NetRequest {
//    http://api.tianapi.com/social/?key=71e58b5b2f930eaf1f937407acde08fe&num=20
//      http://gank.io/api/data/10/10

    public static  String Test_URL = "https://newtest.huolail.com/Admin/Cinterface/taskSort_new";//类型选择

    public static  String URL = "http://api.tianapi.com/meinv/?key=71e58b5b2f930eaf1f937407acde08fe&num=50";

    public static  String MEIZI_URL = "http://gank.io/api/data/";

//    public static String URL = "http://api.tianapi.com/";
//
//
//    public static final String END_URL = "?key=71e58b5b2f930eaf1f937407acde08fe&num=50";//apikey+ 加载数据条数
//
//
//    public static final String SOCIAL_URL = "social/" + END_URL;//社会
//
//    //    http://api.tianapi.com/meinv/?key=71e58b5b2f930eaf1f937407acde08fe&num=50
//    public static final String MEINV_URL = "meinv/"  + END_URL;//美女



    public static void getBackOrderMoney(Fragment activity, MyOkHttp myOkHttp, IResponseHandler iResponseHandler) {
        myOkHttp.post()
                .url(URL)
//                .addParam("key", "71e58b5b2f930eaf1f937407acde08fe")
//                .addParam("num", "20")
                .tag(activity)
                .enqueue(iResponseHandler);
    }
    public static void getMainFragment(Fragment activity,String type,String size ,String page , MyOkHttp myOkHttp, IResponseHandler iResponseHandler) {
        myOkHttp.get()
                .url(MEIZI_URL)
                .addParam("type",type)
                .addParam("size", size)
                .addParam("page", page)
                .tag(activity)
                .enqueue(iResponseHandler);
    }

    public static void getTypes(Activity activity, MyOkHttp myOkHttp, IResponseHandler iResponseHandler) {
        myOkHttp.post()
                .url(Test_URL)
                .tag(activity)
                .enqueue(iResponseHandler);
    }
}
