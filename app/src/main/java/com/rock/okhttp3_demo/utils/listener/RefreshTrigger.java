package com.rock.okhttp3_demo.utils.listener;

/**
 * Created by Rock on 2018/6/25.
 */

public interface RefreshTrigger {
    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete();

    void onReset();
}
