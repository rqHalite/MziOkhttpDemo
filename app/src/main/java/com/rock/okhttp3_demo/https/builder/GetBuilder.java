package com.rock.okhttp3_demo.https.builder;



import com.rock.okhttp3_demo.https.LogUtils;
import com.rock.okhttp3_demo.https.MyOkHttp;
import com.rock.okhttp3_demo.https.callback.MyCallback;
import com.rock.okhttp3_demo.https.response.IResponseHandler;

import java.util.Map;

import okhttp3.Request;

/**
 * Get Builder
 * Created by tsy on 16/9/18.
 */
public class GetBuilder extends OkHttpRequestBuilderHasParam<GetBuilder> {

    public GetBuilder(MyOkHttp myOkHttp) {
        super(myOkHttp);
    }

    @Override
    public void enqueue(final IResponseHandler responseHandler) {
        try {
            if(mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            if (mParams != null && mParams.size() > 0) {
                mUrl = appendParams(mUrl, mParams);
            }

            Request.Builder builder = new Request.Builder().url(mUrl).get();
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();

            mMyOkHttp.getOkHttpClient().
                    newCall(request).
                    enqueue(new MyCallback(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Get enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params to url
    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        //在这里对http://gank.io/进行判断get请求时末尾添加/10/10  例如：http://gank.io/api/data/10/10
        String meiziUrl = url.substring(0,url.indexOf("api"));
        if ("http://gank.io/".equals(meiziUrl)) {
            sb.append(url);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    sb.append(params.get(key)).append("/");
                }
            }
        }else {
            //添加后缀如：http://api.tianapi.com/social/?key=71e58b5b2f930eaf1f937407acde08fe&num=20
            sb.append(url + "?");
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    sb.append(key).append("=").append(params.get(key)).append("&");
                }
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
