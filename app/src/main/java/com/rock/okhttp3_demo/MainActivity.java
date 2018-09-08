package com.rock.okhttp3_demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.rock.okhttp3_demo.base.BaseActivity;
import com.rock.okhttp3_demo.bean.TabEntity;
import com.rock.okhttp3_demo.ui.fragment.FockFragment;
import com.rock.okhttp3_demo.ui.fragment.MainFragment;
import com.rock.okhttp3_demo.ui.fragment.MineFragment;
import com.rock.okhttp3_demo.ui.fragment.PhotoFragment;
import com.rock.okhttp3_demo.utils.manager.RxManager;
import com.rock.okhttp3_demo.utils.AppConstant;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    @Bind(R.id.fl_body)
    FrameLayout flBody;
    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"首页","图片","笑话","我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal,R.mipmap.ic_girl_normal,R.mipmap.ic_video_normal,R.mipmap.ic_care_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected,R.mipmap.ic_girl_selected, R.mipmap.ic_video_selected,R.mipmap.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private MainFragment mainFragment;
    private PhotoFragment photoFragment;
    private FockFragment fockFragment;
    private MineFragment mineFragment;
    private static int tabLayoutHeight;
    private RxManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SetTranslanteBar();
        initTab();
        initFragment(savedInstanceState);
        tabLayout.measure(0,0);
        tabLayoutHeight = tabLayout.getMeasuredHeight();
        manager = new RxManager();
        //设置菜单显示隐藏
        manager.on(AppConstant.MENU_SHOW_HIDE, new Action1<Boolean>() {
            @Override
            public void call(Boolean hideOrShow) {
                startAnimator(hideOrShow);
            }
        });
    }

    private void startAnimator(Boolean hideOrShow) {
        final ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        ValueAnimator valueAnimator;
        ObjectAnimator alpha;
        if(!hideOrShow){
            valueAnimator = ValueAnimator.ofInt(tabLayoutHeight, 0);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
        }else{
            valueAnimator = ValueAnimator.ofInt(0, tabLayoutHeight);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height= (int) valueAnimator.getAnimatedValue();
                tabLayout.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator,alpha);
        animatorSet.start();
    }

    private void initTab() {
        for (int i = 0; i < mTitles.length ; i++){
            mTabEntities.add(new TabEntity(mTitles[i],mIconSelectIds[i],mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //tabLayout 的点击事件
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    private void switchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(photoFragment);
                transaction.hide(fockFragment);
                transaction.hide(mineFragment);
                transaction.show(mainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //美女
            case 1:
                transaction.hide(mainFragment);
                transaction.hide(fockFragment);
                transaction.hide(mineFragment);
                transaction.show(photoFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(mainFragment);
                transaction.hide(photoFragment);
                transaction.hide(mineFragment);
                transaction.show(fockFragment);
                transaction.commitAllowingStateLoss();
                break;
            //关注
            case 3:
                transaction.hide(mainFragment);
                transaction.hide(photoFragment);
                transaction.hide(fockFragment);
                transaction.show(mineFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragment");
            photoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentByTag("photoFragment");
            fockFragment = (FockFragment) getSupportFragmentManager().findFragmentByTag("fockFragment");
            mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mineFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            mainFragment = new MainFragment();
            photoFragment = new PhotoFragment();
            fockFragment = new FockFragment();
            mineFragment = new MineFragment();

            transaction.add(R.id.fl_body, mainFragment, "mainFragment");
            transaction.add(R.id.fl_body, photoFragment, "photoFragment");
            transaction.add(R.id.fl_body, fockFragment, "fockFragment");
            transaction.add(R.id.fl_body, mineFragment, "mineFragment");
        }
        transaction.commit();
        switchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null){
            manager.clear();
        }
    }
}
