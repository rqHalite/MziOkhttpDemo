package com.rock.okhttp3_demo.https;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.rock.okhttp3_demo.https.bean.StringNameValueBean;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by Rock on 2018/4/28.
 */

public class HttpRequest{

    private final static OkHttpClient okHttpClient;

    private static CheckNetworkCallback checkNetworkCallback;
    //本次请求的DomainInfo，保存使用的对象，
    //用于上传服务器请求状态，以作为下次IP筛选的依据

    private ArrayList<StringNameValueBean> requestBeanList;


    static {
        okHttpClient = new OkHttpClient();
    }




    public static void setCertificates(InputStream... certificates){
        try{
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates){
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try{
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e){
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(
                    null,
                    trustManagerFactory.getTrustManagers(),
                    new SecureRandom()
            );
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getCodeMsg(int code){
        switch (code){
            case 100:
                return "请继续提出请求";
            case 101:
                return "服务器已切换服务协议";
            case 200:
                return "访问成功";
            case 201:
                return "新资源已创建";
            case 202:
                return "服务器已收到请求";
            case 203:
                return "返回数据可能来自其他来源";
            case 204:
                return "服务器未返回任何内容";
            case 205:
                return "内容已重置";
            case 206:
                return "服务器已处理部分请求";
            case 300:
                return "服务器收到多种选择请求";
            case 301:
                return "请求的网页已永久移动到新位置";
            case 302:
                return "服务器返回数据临时从其他页面获取";
            case 303:
                return "请指定具体的访问地址";
            case 304:
                return "返回内容未修改，放弃返回数据";
            case 305:
                return "请使用代理方式访问";
            case 307:
                return "数据来自其他网页";
            case 400:
                return "请求语法错误";
            case 401:
                return "您的身份未验证";
            case 403:
                return "服务器拒绝请求";
            case 404:
                return "请求地址不存在";
            case 405:
                return "请求的方法已被禁用";
            case 406:
                return "服务器不接受非法请求";
            case 407:
                return "请提供代理授权";
            case 408:
                return "网络请求超时";
            case 409:
                return "数据返回时发生冲突";
            case 410:
                return "请求资源已被永久删除";
            case 411:
                return "拒绝未知长度的请求";
            case 412:
                return "请求操作未满足前提条件";
            case 413:
                return "请求内容过大";
            case 414:
                return "请求地址过长";
            case 415:
                return "不支持的媒体格式";
            case 416:
                return "请求范围不符合要求";
            case 417:
                return "未满足服务器需求";
            case 428:
                return "需要前提条件";
            case 429:
                return "请求过多";
            case 431:
                return "请求头部字段太长";
            case 500:
                return "服务器内部错误";
            case 501:
                return "尚未实施，无法完成请求";
            case 502:
                return "服务器网关错误";
            case 503:
                return "服务器不可用";
            case 504:
                return "网关请求超时";
            case 505:
                return "HTTP版本不受支持";
            case 511:
                return "需要网络认证，请使用浏览器打开任意网页认证";
            case 0:
                return "访问失败";
            default:
                return code+"";
        }
    }

    public static String getExceptionInfo(Exception e){
        if(e == null)
            return "";
        if(e instanceof NetworkException){
            return "当前无网络连接，请检查后再试";
        }
        if(e instanceof JSONException ||e instanceof NumberFormatException){
            return "数据解析失败";
        }
        if(e instanceof UnknownHostException){
            return "域名解析失败，请更换网络模式或清理DNS";
        }
        if(e instanceof java.net.SocketTimeoutException){
            return "网络连接超时，请检查网络状况";
        }
        if(e instanceof java.net.ConnectException){
            return "网络连接错误，请检查网络状况";
        }
        if(e instanceof RequestException)
            return e.getMessage();
        return "Exception:"+e.getMessage();
    }

    public static class NetworkException extends Exception{
        public NetworkException() {
            this("无网络连接");
        }

        public NetworkException(String message) {
            super(message);
        }

        public NetworkException(String message, Throwable cause) {
            super(message, cause);
        }

        public NetworkException(Throwable cause) {
            super(cause);
        }
    }

    public static String getError(int code,Exception e){
        return getCodeMsg(code)+(e==null?"":","+HttpRequest.getExceptionInfo(e));
    }

//    private MultipartBuilder builder;
    private Request.Builder requestBuilder;
    private ArrayList<RequestListener> requestListeners;
    private ArrayList<ResponseListener> responseListeners;

    private RequestListener thisRequestListener = new RequestListener() {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
            if(requestListeners!=null&&requestListeners.size()>0){
                for(RequestListener requestListener:requestListeners){
                    if(requestListener!=null)
                        requestListener.onRequestProgress(bytesWritten,contentLength,done);
                }
            }
        }

        @Override
        public boolean onError(Exception e) {
            if(requestListeners!=null&&requestListeners.size()>0){
                for(RequestListener requestListener:requestListeners){
                    if(requestListener!=null)
                        if(requestListener.onError(e))
                            return true;
                }
            }
            return false;
        }
    };

    private ResponseListener thisResponseListener = new ResponseListener() {
        @Override
        public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
            if(responseListeners!=null&&responseListeners.size()>0){
                for(ResponseListener responseListener:responseListeners){
                    if(responseListener!=null)
                        responseListener.onResponseProgress(bytesRead,contentLength,done);
                }
            }
        }

        @Override
        public boolean onError(Exception e) {
            if(responseListeners!=null&&responseListeners.size()>0){
                for(ResponseListener responseListener:responseListeners){
                    if(responseListener!=null)
                        if(responseListener.onError(e))
                            return true;
                }
            }
            return false;
        }
    };

    public HttpRequest() {
//        requestListeners = new ArrayList<>();
//        responseListeners = new ArrayList<>();
        requestBeanList = new ArrayList<>();
    }

//    private MultipartBuilder getBuilder(){
//        return new MultipartBuilder()
//                .type(MultipartBuilder.FORM);
//    }

    public static HttpRequest createRequest(){
        return new HttpRequest();
    }

    public HttpRequest url(String url){
        getRequestBuilder().url(url);
        return this;
    }


    public interface CheckNetworkCallback{
        public boolean isNetworkConnected();
    }


    public Request.Builder getRequestBuilder(){
        if(requestBuilder==null)
            requestBuilder = new Request.Builder();
        return requestBuilder;
    }


    public HttpRequest addHeader(String name,String value){
        getRequestBuilder().addHeader(name,value);
        return this;
    }

    public HttpRequest url(HttpUrl url){
        getRequestBuilder().url(url);
        return this;
    }


    private String guessMimeType(String path){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 数据获取回调接口
     * @author LiuJ
     */
    public static abstract class RequestCallBack<T>{
        HttpRequest httpRequest;

        private void setHttpRequest(HttpRequest httpRequest) {
            this.httpRequest = httpRequest;
        }
        public abstract void success(T object);
        public abstract void error(int code,Request request,Response response,Exception e);
        public abstract T processing(Response response)throws Exception;
    }

    public static abstract class RequestOnHandlerCallBack<T> extends RequestCallBack<T>{
        protected Handler handler;

        public RequestOnHandlerCallBack(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void success(final T object) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onUISuccess(object);
                }
            });
        }

        @Override
        public void error(final int code, final Request request, final Response response, final Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onUIError(code,request,response,e);
                }
            });
        }

        @Override
        public T processing(Response response)throws Exception {
            return onBackground(response);
        }

        public abstract void onUISuccess(T object);

        public abstract void onUIError(int code,Request request,Response response,Exception e);

        public abstract T onBackground(Response response) throws Exception;

    }

    public static final int HANDLER_WHAT_REQUEST = 963123;

    public static abstract class RequestStringOnHandlerCallBack<T> extends RequestOnHandlerCallBack<T>{

        public RequestStringOnHandlerCallBack(Handler handler) {
            super(handler);
        }

        protected abstract T onBackground(String response)throws Exception;

    }

    private static void handlerLog(Handler handler,HttpRequest httpRequest,String request,String response)throws Exception{
        if(handler!=null){
            Message message = handler.obtainMessage(HANDLER_WHAT_REQUEST);
            try{
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(request);//添加基本消息
                ArrayList<StringNameValueBean> beanArrayList = httpRequest.requestBeanList;
                stringBuilder.append("\nParameters:");
                for(StringNameValueBean bean:beanArrayList){
                    stringBuilder.append(bean.toString());
                }
                stringBuilder.append("\nResponse:");
                stringBuilder.append(response);
                message.obj = stringBuilder.toString();
            }finally {
                if(message.obj==null)
                    message.obj = "";
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 运行于UI线程的数据获取回掉接口
     * @param <T>
     */
    public static abstract class RequestOnUICallBack<T> extends RequestCallBack<T>{
        protected Activity activity;

        public RequestOnUICallBack(Fragment fragment) {
            this.activity = fragment.getActivity();
        }

        public RequestOnUICallBack(android.app.Fragment fragment) {
            this.activity = fragment.getActivity();
        }

        public RequestOnUICallBack(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void success(final T object) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onUISuccess(object);
                }
            });
        }

        @Override
        public void error(final int code, final Request request, final Response response, final Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onUIError(code,request,response,e);
                }
            });
        }

        @Override
        public T processing(Response response)throws Exception {
            return onBackground(response);
        }

        public abstract void onUISuccess(T object);

        public abstract void onUIError(int code,Request request,Response response,Exception e);

        public abstract T onBackground(Response response) throws Exception;

    }

    public static abstract class RequestStringOnUICallBack<T> extends RequestOnUICallBack<T>{

        public RequestStringOnUICallBack(Activity activity) {
            super(activity);
        }

        public RequestStringOnUICallBack(Fragment fragment) {
            super(fragment);
        }

        public RequestStringOnUICallBack(android.app.Fragment fragment) {
            super(fragment);
        }



        @Override
        public T onBackground(Response response)throws Exception{
            String res = "";
            if(response.isSuccessful()){
                res = response.body().string();
                return onBackground(res);
            }else
                error(response.code(),response.request(),response,null);
            return null;
        }

        protected abstract T onBackground(String response)throws Exception;

    }

    private static String getFileName(String path){
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    public interface DownloadCallBack{
        void onError(int code,Request request,Exception e);
        void onProgressChange(float pro,long allLength,long downLength);
        void onDownLoadSuccess(String path);
    }

    public static abstract class DownloadOnUICallBack implements DownloadCallBack{

        protected Activity content;

        public DownloadOnUICallBack(Activity content) {
            this.content = content;
        }

        @Override
        public void onError(final int code, final Request request, final Exception e) {
            if(content!=null)
                content.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIError(code,request,e);
                    }
                });
        }

        @Override
        public void onProgressChange(final float pro, final long allLength, final long downLength) {
            if(content!=null)
                content.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIProgressChange(pro,allLength,downLength);
                    }
                });
        }

        @Override
        public void onDownLoadSuccess(final String path) {
            if(content!=null)
                content.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIDownLoadSuccess(path);
                    }
                });
        }

        public abstract void onUIError(int code, Request request, Exception e);

        public abstract void onUIProgressChange(float pro, long allLength, long downLength);

        public abstract void onUIDownLoadSuccess(String path);

    }

    public static abstract class DownloadOnHandlerCallBack implements DownloadCallBack{

        protected Handler handler;

        public DownloadOnHandlerCallBack(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onError(final int code, final Request request, final Exception e) {
            if(handler!=null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUIError(code,request,e);
                    }
                });
        }

        @Override
        public void onProgressChange(final float pro, final long allLength, final long downLength) {
            if(handler!=null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUIProgressChange(pro,allLength,downLength);
                    }
                });
        }

        @Override
        public void onDownLoadSuccess(final String path) {
            if(handler!=null)
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUIDownLoadSuccess(path);
                    }
                });
        }

        public abstract void onUIError(int code, Request request, Exception e);

        public abstract void onUIProgressChange(float pro, long allLength, long downLength);

        public abstract void onUIDownLoadSuccess(String path);

    }


    //响应体进度回调接口，比如用于文件下载中
    //主要用于返回数据读取的监听
    public interface ResponseListener {
        void onResponseProgress(long bytesRead, long contentLength, boolean done);
        boolean onError(Exception e);
    }

    //请求体进度回调接口，比如用于文件上传中
    public interface RequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
        boolean onError(Exception e);
    }

    public synchronized HttpRequest addRequestListener(RequestListener requestListener) {
        if(requestListeners==null)
            requestListeners = new ArrayList<>();
        this.requestListeners.add(requestListener);
        return this;
    }

    public synchronized HttpRequest addResponseListener(ResponseListener responseListener) {
        if(responseListeners==null)
            responseListeners = new ArrayList<>();
        this.responseListeners.add(responseListener);
        return this;
    }

    public abstract static class UploadListener implements RequestListener{
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
            if(done){
                onSuccess(contentLength);
            }else{
                onProgressChange(bytesWritten*1.0f/contentLength,contentLength,bytesWritten);
            }
        }

        public abstract void onSuccess(long allLength);

        public abstract void onProgressChange(float progress, long allLength, long upLenth);
    }

    public abstract static class OnUIUploadListener extends UploadListener{
        protected Activity activity;

        public OnUIUploadListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onError(final Exception e) {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIError(e);
                    }
                });
                return true;
            }else
                return false;
        }

        @Override
        public void onSuccess(final long allLength) {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUISuccess(allLength);
                    }
                });
            }
        }

        @Override
        public void onProgressChange(final float progress, final long allLength, final long upLenth) {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIProgressChange(progress,allLength,upLenth);
                    }
                });
            }
        }

        public abstract void onUIProgressChange(float progress, long allLength, long upLenth);

        public abstract void onUISuccess(long allLength);

        public abstract void onUIError(Exception e);

    }

    public abstract static class OnHandlerUploadListener extends UploadListener{
        protected Handler handler;

        public OnHandlerUploadListener(Handler handler) {
            this.handler = handler;
        }

        @Override
        public boolean onError(final Exception e) {
            if(handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUIError(e);
                    }
                });
                return true;
            }else
                return false;
        }

        @Override
        public void onSuccess(final long allLength) {
            if(handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUISuccess(allLength);
                    }
                });
            }
        }

        @Override
        public void onProgressChange(final float progress, final long allLength, final long upLenth) {
            if(handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onUIProgressChnage(progress,allLength,upLenth);
                    }
                });
            }
        }

        public abstract void onUIProgressChnage(float progress, long allLength, long upLenth);

        public abstract void onUISuccess(long allLength);

        public abstract void onUIError(Exception e);

    }

    public abstract static class DownloadListener implements ResponseListener{
        @Override
        public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
            if(done){
                onSuccess(contentLength);
            }else{
                onProgressChnage(bytesRead*1.0f/contentLength,contentLength,bytesRead);
            }
        }

        public abstract void onSuccess(long allLength);

        public abstract void onProgressChnage(float progress,long allLength,long downLenth);
    }

    public abstract static class OnUIDownloadListener extends DownloadListener{
        protected Activity activity;

        public OnUIDownloadListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onError(final Exception e) {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIError(e);
                    }
                });
                return true;
            }else
                return false;
        }

        @Override
        public void onSuccess(final long allLength) {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUISuccess(allLength);
                    }
                });
            }
        }

        @Override
        public void onProgressChnage(final float progress, final long allLength, final long downLenth) {
            if(activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIProgressChnage(progress,allLength,downLenth);
                    }
                });
            }
        }

        public abstract void onUIProgressChnage(float progress, long allLength, long downLenth);

        public abstract void onUISuccess(long allLength);

        public abstract void onUIError(Exception e);

    }

    /**
     * 请求异常
     */
    public static class RequestException extends RuntimeException{
        public RequestException() {
        }

        public RequestException(String message) {
            super(message);
        }
    }

    public static CheckNetworkCallback getCheckNetworkCallback() {
        return checkNetworkCallback;
    }

    public static void setCheckNetworkCallback(CheckNetworkCallback checkNetworkCallback) {
        HttpRequest.checkNetworkCallback = checkNetworkCallback;
    }
}
