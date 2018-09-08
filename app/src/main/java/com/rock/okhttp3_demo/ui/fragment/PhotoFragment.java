package com.rock.okhttp3_demo.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rock.okhttp3_demo.R;
import com.rock.okhttp3_demo.adapter.CommonRecycleViewAdapter;
import com.rock.okhttp3_demo.base.BaseFragment;
import com.rock.okhttp3_demo.bean.main.PhotoBean;
import com.rock.okhttp3_demo.https.response.JsonResponseHandler;
import com.rock.okhttp3_demo.net.NetRequest;
import com.rock.okhttp3_demo.utils.ImageLoaderUtils;
import com.rock.okhttp3_demo.utils.helper.ViewHolderHelper;
import com.rock.okhttp3_demo.utils.listener.OnLoadMoreListener;
import com.rock.okhttp3_demo.utils.listener.OnRefreshListener;
import com.rock.okhttp3_demo.view.IRecyclerView;
import com.rock.okhttp3_demo.view.LoadMoreFooterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    @Bind(R.id.irc)
    IRecyclerView irc;


    private String size = "30";
    private String type = "福利";
    private int page = 1;
    private List<PhotoBean.ResultsBean> photoBean;
    private CommonRecycleViewAdapter<PhotoBean.ResultsBean> adapter;

//    public PhotoFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_photo, container, false);
//    }

    @Override
    protected void initView() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        irc.setLayoutManager(manager);
        irc.setOnLoadMoreListener(this);
        irc.setOnRefreshListener(this);
        getDatas(type, size, page + "");

    }

    private void getDatas(String type, String size, String s) {
        NetRequest.getMainFragment(this, type, size, s, myOkHttp, new JsonResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                photoBean = new ArrayList<PhotoBean.ResultsBean>();
                JSONArray jsonArray = response.optJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    PhotoBean.ResultsBean resultsBean = new PhotoBean.ResultsBean();
                    resultsBean.setDesc(jsonObject.optString("desc"));
                    resultsBean.setUrl(jsonObject.optString("url"));
                    photoBean.add(resultsBean);
                }
                changeDatas();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    private void changeDatas() {
        if (adapter == null){
            adapter = new CommonRecycleViewAdapter<PhotoBean.ResultsBean>(getContext(), R.layout.recycleview_item_simple_image,photoBean) {
                @Override
                public void convert(ViewHolderHelper helper, PhotoBean.ResultsBean resultsBean) {
                    ImageView photoImg = helper.getView(R.id.simple_image);
                    TextView title = helper.getView(R.id.simple_title);
                    title.setText(resultsBean.getDesc());
                    ImageLoaderUtils.display(getContext(),photoImg,resultsBean.getUrl());
                }
            };

            irc.setAdapter(adapter);
        }else {
            adapter.notifyData(photoBean);
        }
        if (photoBean != null){
            page += 1;
            if (adapter.getPageBean().isRefresh()){
                irc.setRefreshing(false);
            }else {
            if (photoBean.size() > 0){
                irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                adapter.addAll(photoBean);

            }else {
                irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
            }
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_photo;
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        adapter.getPageBean().setRefresh(false);
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        getDatas(type, size, page + "");
    }

    @Override
    public void onRefresh() {
        adapter.getPageBean().setRefresh(true);
        page = 0;
        irc.setRefreshing(true);
        getDatas(type, size, page + "");
    }
}
