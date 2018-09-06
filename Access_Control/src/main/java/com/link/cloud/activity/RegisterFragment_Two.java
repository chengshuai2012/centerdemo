package com.link.cloud.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Member;
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.core.BaseFragment;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;

//import com.link.cloud.contract.RegisterTaskContract;

/**
 * Created by Administrator on 2017/8/30.
 */

public class RegisterFragment_Two extends BaseFragment {
//    @Bind(R.id.bind_bt_back)
//    Button back;
    @Bind(R.id.layout_two)
    LinearLayout layout_two;
    @Bind(R.id.layout_three)
    LinearLayout layout_three;
    @Bind(R.id.bind_member_next)
    Button next;
    @Bind(R.id.userType)
    TextView user_Type;
    @Bind(R.id.bind_member_name)
    TextView menber_name;
    @Bind(R.id.bind_member_cardtype)
    TextView cardtype;
    @Bind(R.id.bind_member_cardnumber)
    TextView cardnumber;
    @Bind(R.id.bind_member_begintime)
    TextView startTime;
    @Bind(R.id.bind_member_endtime)
    TextView endTime;
    @Bind(R.id.bind_member_sex)
    TextView menber_sex;
    @Bind(R.id.bind_member_phone)
    TextView menber_phone;
    private Member mMemberInfo;
    @Bind(R.id.card_value)

    TextView cardValue;
    @Bind(R.id.card_name)

    TextView cardName;

    public BindTaskContract presenter;
    private static int MAXT_FINGER = 3;//表示注册几个指静脉模版
    Handler mHandler;
    private SharedPreferences userInfo;
    public static CallBackValue callBackValue;
    private BindAcitvity acitvity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.acitvity=(BindAcitvity) activity;
        callBackValue=(CallBackValue)activity;
    }
    public static RegisterFragment_Two newInstance(Member memberInfo) {
        RegisterFragment_Two fragment = new RegisterFragment_Two();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.e("RegisterFragment_Two"+"onCreate");
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onError(ApiException error) {
        Logger.e("RegisterFragment_Two"+"onError");
        super.onError(error);
    }

    @Override
    protected void onVisible() {
        Logger.e("RegisterFragment_Two"+"onVisible");
    }
    @Override
    protected void initListeners() {
        Logger.e("RegisterFragment_Two"+"initListeners");
    }


    @Override
    protected void onInvisible() {
        Logger.e("RegisterFragment_Two"+"onInvisible");
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        layout_two.setVisibility(View.GONE);
        layout_three.setVisibility(View.VISIBLE);
    }


    @Override
    protected void initData() {
        Logger.e("RegisterFragment_Two"+"initData");
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_bind_member;
    }

    @OnClick({R.id.bind_member_next})
    public void onClick(View view){

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
