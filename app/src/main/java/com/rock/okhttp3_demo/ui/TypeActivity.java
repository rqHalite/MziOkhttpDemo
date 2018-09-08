package com.rock.okhttp3_demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rock.okhttp3_demo.R;
import com.rock.okhttp3_demo.app.MyApp;
import com.rock.okhttp3_demo.base.BaseActivity;
import com.rock.okhttp3_demo.https.MyOkHttp;
import com.rock.okhttp3_demo.https.response.JsonResponseHandler;
import com.rock.okhttp3_demo.net.NetRequest;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TypeActivity extends BaseActivity {

    @Bind(R.id.lv_left)
    ListView lvLeft;
    @Bind(R.id.lv_Right)
    ListView lvRight;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private MyOkHttp okHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        ButterKnife.bind(this);
        okHttp = MyApp.getInstance().getmMyOkHttp();
        getDatas();
    }

    private void getDatas() {
        NetRequest.getTypes(this, okHttp, new JsonResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                String str = response.toString();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }


//    @OnClick({R.id.test})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.test:
//
//                break;
//        }
//
//
//    }
}
