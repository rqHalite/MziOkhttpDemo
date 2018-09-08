package com.rock.okhttp3_demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rock.okhttp3_demo.app.MyApp;
import com.rock.okhttp3_demo.https.MyOkHttp;

import butterknife.ButterKnife;

/**
 * Created by Rock on 2018/6/23.
 */

public abstract class BaseFragment extends Fragment {

    protected MyOkHttp myOkHttp;
    private View root;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
//        return rootView;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myOkHttp = MyApp.getInstance().getmMyOkHttp();
        if (root == null){
            root = inflater.inflate(getLayoutResource(),container,false);
            ButterKnife.bind(this, root);
        }
        initView();
        return root;
    }

    //初始化view
    protected abstract void initView();

    //获取布局文件
    protected abstract int getLayoutResource();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
