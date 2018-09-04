package com.link.cloud.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.link.cloud.R;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/8/18.
 */

public class NewMainActivity extends AppCompatActivity {
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.data_time)
    TextView dataTime;
    @Bind(R.id.bt_main_bind)
    Button btMainBind;
    @Bind(R.id.bt_main_bind_face)
    Button btMainBindFace;
    @Bind(R.id.versionName)
    TextView versionName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        ButterKnife.bind(this);
    }

    /**
     * 广播接收器
     *
     * @author kevin
     */
    public class MesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView2.setText(intent.getStringExtra("timeStr"));
            dataTime.setText(intent.getStringExtra("timeData"));
            if (context == null) {

            }
        }
    }
}
