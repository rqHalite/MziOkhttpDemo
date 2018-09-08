package com.rock.okhttp3_demo.bean.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rock on 2018/6/25.
 */

public class MeinvBean implements Serializable {


    /**
     * error : false
     * results : [{"_id":"5b2cb5dd421aa923c0fbfdd5","createdAt":"2018-06-26T10:52:34.916Z","desc":"渐进式组件化的Android开源框架：立即组件化开发 & 渐进式组件化改造","publishedAt":"2018-06-26T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/luckybilly/CC","used":true,"who":"齐翊"},{"_id":"5b3197b8421aa955684c3dff","createdAt":"2018-06-26T09:32:40.927Z","desc":"仿微博、酷安点击加号揭露动画弹出菜单","images":["http://img.gank.io/5ecd1b9e-ec88-4ce3-a700-8b7dc31cde8c"],"publishedAt":"2018-06-26T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/DuShuYuan/PlusMenu","used":true,"who":"CZ_DSY"},{"_id":"5b31ab8e421aa9556d2cc4a8","createdAt":"2018-06-26T10:57:18.324Z","desc":"仿京东金融首页的有速率差的联动ScrollView。","images":["http://img.gank.io/000d41c7-27a1-4a6b-9e7a-ef7148b738e8"],"publishedAt":"2018-06-26T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/meiniepan/TogetherScrollView","used":true,"who":"lijinshanmx"},{"_id":"5b31ad47421aa9556b44c672","createdAt":"2018-06-26T11:04:39.32Z","desc":"一款基于BottomSheetDialogFragment实现的图片选择器，支持多选和单选。","images":["http://img.gank.io/2920c057-136e-47f2-abb8-707f053a735a","http://img.gank.io/fbc2a80f-c6cc-4f22-bd03-b5b7deb03ce2"],"publishedAt":"2018-06-26T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/siralam/BSImagePicker","used":true,"who":"lijinshanmx"},{"_id":"5b31aef2421aa955684c3e04","createdAt":"2018-06-26T11:11:46.732Z","desc":"一个轻量级的处理耗时任务的Android库。","publishedAt":"2018-06-26T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/savepopulation/peanut","used":true,"who":"lijinshanmx"},{"_id":"5b2d16c3421aa9556b44c65c","createdAt":"2018-06-22T23:33:23.665Z","desc":"直播间的送礼物动画","images":["http://img.gank.io/ff89a354-c2a3-4085-aa85-63592697b71c"],"publishedAt":"2018-06-25T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/jenly1314/GiftSurfaceView","used":true,"who":"艾米"},{"_id":"5b2da19d421aa9556d2cc495","createdAt":"2018-06-23T09:25:49.808Z","desc":"史上最简单，最强大的Bundle存取封装库。支持存取任意类型数据！","publishedAt":"2018-06-25T00:00:00.0Z","source":"web","type":"Android","url":"https://juejin.im/entry/5b2c67c46fb9a00e4f74cc56","used":true,"who":"yjfnypeu"},{"_id":"5b303c0d421aa9556b44c668","createdAt":"2018-06-25T08:49:17.6Z","desc":"数字输入控件，像支付宝、微信支付的密码输入面板","images":["http://img.gank.io/6052c17f-54cc-4e08-8432-1bbb72cd5f12","http://img.gank.io/d032d8ce-8c03-4eae-9ecc-2433c50ba99c","http://img.gank.io/a2291caa-fd4d-46b1-834f-1f659656589a","http://img.gank.io/3a79d2df-4f4e-4f46-9181-b70d161e611f","http://img.gank.io/30441f39-b7eb-4450-84e4-239f209c39ac"],"publishedAt":"2018-06-25T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/linkaipeng/NumberCodeView","used":true,"who":"linkaipeng"},{"_id":"5b3060e6421aa9556d2cc49d","createdAt":"2018-06-25T11:26:30.92Z","desc":"一个底部导航，在选定的项目顶部有一个很棒的变形效果。","publishedAt":"2018-06-25T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/tommybuonomo/morph-bottom-navigation","used":true,"who":"lijinshanmx"},{"_id":"5b0d34b5421aa97efda86543","createdAt":"2018-05-29T19:08:37.562Z","desc":"基于 OKhttp Interceptor 的抓包工具，方便开发调试","images":["http://img.gank.io/426f8c31-3029-46eb-ac4a-afd1989d15aa","http://img.gank.io/261f65df-3ce2-4dd7-a0e4-ca690e4abadf","http://img.gank.io/2baa67eb-5de2-4008-86e5-cb9dfc7e8450"],"publishedAt":"2018-06-22T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/linkaipeng/OkNetworkMonitor","used":true,"who":"linkaipeng"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Serializable {
        /**
         * _id : 5b2cb5dd421aa923c0fbfdd5
         * createdAt : 2018-06-26T10:52:34.916Z
         * desc : 渐进式组件化的Android开源框架：立即组件化开发 & 渐进式组件化改造
         * publishedAt : 2018-06-26T00:00:00.0Z
         * source : web
         * type : Android
         * url : https://github.com/luckybilly/CC
         * used : true
         * who : 齐翊
         * images : ["http://img.gank.io/5ecd1b9e-ec88-4ce3-a700-8b7dc31cde8c"]
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
