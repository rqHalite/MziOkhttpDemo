package com.rock.okhttp3_demo.utils.listener;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rock on 2018/6/25.
 *
 * 列表点击事件和长按事件
 */

public interface OnItemClickListener<T> {

    void onItemClick(ViewGroup parent, View view, T t, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
