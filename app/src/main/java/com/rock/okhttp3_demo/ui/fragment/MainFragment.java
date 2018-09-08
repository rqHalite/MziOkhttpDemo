package com.rock.okhttp3_demo.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rock.okhttp3_demo.R;
import com.rock.okhttp3_demo.adapter.CommonRecycleViewAdapter;
import com.rock.okhttp3_demo.base.BaseFragment;
import com.rock.okhttp3_demo.bean.main.MeinvBean;
import com.rock.okhttp3_demo.https.response.JsonResponseHandler;
import com.rock.okhttp3_demo.net.NetRequest;
import com.rock.okhttp3_demo.ui.TypeActivity;
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
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {


    @Bind(R.id.main_list)
    IRecyclerView mainList;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    private List<MeinvBean.ResultsBean> meinvBeen = new ArrayList<>();
//    private CommonAdapter adapter;

    private String size = "12";
    public int page = 1;
    private static String type = "Android";
    private CommonRecycleViewAdapter<MeinvBean.ResultsBean> adapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mainList.setLayoutManager(manager);
        mainList.setOnLoadMoreListener(this);
        mainList.setOnRefreshListener(this);

        //请求数据
        getDatas(size, page);
    }



    private void getDatas(String size, int page) {
        NetRequest.getMainFragment(this,type, size, page + "", myOkHttp, new JsonResponseHandler() {
            //
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                String error = response.optString("error");
                if ("false".equals(error)) {
                    JSONArray jsonArray = response.optJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.optJSONObject(i);
                        MeinvBean.ResultsBean resultsBean = new MeinvBean.ResultsBean();
                        resultsBean.setDesc(object.optString("desc"));
                        meinvBeen.add(resultsBean);
                    }
                    //
                    if (adapter == null){
                        adapter = new CommonRecycleViewAdapter<MeinvBean.ResultsBean>(getContext(), R.layout.recycleview_item_simple_image, meinvBeen) {
                            @Override
                            public void convert(ViewHolderHelper helper, MeinvBean.ResultsBean resultsBean) {
                                TextView txt = helper.getView(R.id.simple_title);
                                txt.setText(resultsBean.getDesc());
                            }
                        };
                        mainList.setAdapter(adapter);
                    }else {
                        adapter.notifyData(meinvBeen);
                    }
                    changePage();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    private void changePage() {
        if (meinvBeen != null) {
            page += 1;
            if (adapter.getPageBean().isRefresh()) {
                mainList.setRefreshing(false);
//                adapter.replaceAll(meinvBeen);

            } else {
                if (meinvBeen.size() > 0) {
                    mainList.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    adapter.addAll(meinvBeen);
                } else {
                    mainList.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }



    @Override
    public void onLoadMore(View loadMoreView) {
        adapter.getPageBean().setRefresh(false);
//        Toast.makeText(getContext(),"请求中。。。",Toast.LENGTH_SHORT).show();
        mainList.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        getDatas(size, page);
    }

    @Override
    public void onRefresh() {
        adapter.getPageBean().setRefresh(true);
        page = 0;
        mainList.setRefreshing(true);
        getDatas(size, page);

    }

    @OnClick({R.id.fab})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.fab:
                startActivity(new Intent(getContext(), TypeActivity.class));
                break;

        }
    }

}
