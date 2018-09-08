package com.rock.okhttp3_demo.utils.anim;

import android.animation.Animator;
import android.view.View;

/**
 * Created by Rock on 2018/6/25.
 *
 * 基本动画
 */

public interface BaseAnimation {

    Animator[] getAnimators(View view);
}
