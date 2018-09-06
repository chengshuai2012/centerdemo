package com.link.cloud.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.ams.common.util.HexUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.link.cloud.BaseApplication;
import com.link.cloud.R;
import com.link.cloud.base.ApiException;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.Person;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.contract.BindTaskContract;
import com.link.cloud.contract.SendLogMessageTastContract;
import com.link.cloud.core.BaseAppCompatActivity;
import com.link.cloud.setting.TtsSettings;
import com.link.cloud.venue.MdDevice;
import com.link.cloud.venue.MdUsbService;
import com.link.cloud.venue.ModelImgMng;
import com.link.cloud.view.NoScrollViewPager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import md.com.sdk.MicroFingerVein;


/**

 * Created by Administrator on 2017/8/24.

 */



public class BindAcitvity extends BaseAppCompatActivity implements CallBackValue,BindTaskContract.BindView,SendLogMessageTastContract.sendLog {

    @Bind(R.id.bing_main_page)

    NoScrollViewPager viewPager;

    @Bind(R.id.layout_page_time)

    TextView timeStr;

    @Bind(R.id.layout_page_title)

    TextView tvTitle;

    @Bind(R.id.bind_one_Cimg)

    ImageView bind_one_Cimg;

    @Bind(R.id.bind_one_line)

    View bind_one_line;

    @Bind(R.id.layout_main_error)

    LinearLayout layout_error_text;

    @Bind(R.id.bind_two_Cimg)

    ImageView bind_two_Cimg;

    @Bind(R.id.bind_two_line)

    View bind_two_line;

    @Bind(R.id.bind_three_Cimg)

    ImageView bind_three_Cimg;

    @Bind(R.id.bind_three_line)

    View bind_three_line;

    @Bind(R.id.bind_four_Cimg)

    ImageView bind_four_Cimg;

    @Bind(R.id.bind_one_tv)

    TextView bind_one_tv;

    @Bind(R.id.bind_two_tv)

    TextView bind_two_tv;

    @Bind(R.id.bind_three_tv)

    TextView bind_three_tv;

    @Bind(R.id.mian_text_error)

    TextView textError;

    @Bind(R.id.text_tile)

    TextView text_tile;

    @Bind(R.id.bind_four_tv)

    TextView bind_four_tv;

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

//    public static final String ACTION_UPDATEUI = "action.updateTiem";

    BindTaskContract bindTaskContract;



    private BindVeinMainFragment bindVeinMainFragment;



    private MesReceiver mesReceiver;

    private int recLen=0;

    private Runnable runnable;

    String feature;

    // 语音合成对象

    public SpeechSynthesizer mTts;

//    // 默认本地发音人

    public static String voicerLocal="xiaoyan";

    // 引擎类型

    private SharedPreferences mSharedPreferences;

    SendLogMessageTastContract sendLogMessageTastContract;

    Realm realm;
    boolean isWorkFinsh =false;
    @Override

    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Intent intent = new Intent(this, MdUsbService.class);
        bindService(intent, mdSrvConn, Service.BIND_AUTO_CREATE);
        super.onCreate(savedInstanceState);
        realm= Realm.getDefaultInstance();
        // 初始化合成对象

        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);

        setParam();


    }



    @Override

    protected int getLayoutId() {

        return R.layout.layout_main_bind;

    }







    @Override

    protected void initToolbar(Bundle savedInstanceState) {



    }



    @Override

    protected void initListeners() {



    }





    /**

     * 参数设置

     * @return

     */

    private void setParam(){

        // 清空参数

        mTts.setParameter(SpeechConstant.PARAMS, null);

        //设置合成

        //设置使用本地引擎

        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);

        //设置发音人资源路径

        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());

        //设置发音人

        mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);

        //设置合成语速

        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));

        //设置合成音调

        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));

        //设置合成音量

        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));

        //设置播放器音频流类型

        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));

        // 设置播放合成音频打断音乐播放，默认为true

        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限

        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效

        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");





    }

    //获取发音人资源路径

    private String getResourcePath(){

        StringBuffer tempBuffer = new StringBuffer();

        //合成通用资源

        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));

        tempBuffer.append(";");

        //发音人资源

        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+BindAcitvity.voicerLocal+".jet"));

        return tempBuffer.toString();

    }

    /**

     * 初始化监听。

     */

    private InitListener mTtsInitListener = new InitListener() {

        @Override

        public void onInit(int code) {

            if (code != ErrorCode.SUCCESS) {

                showTip("初始化失败"+code);

            } else {

            }

        }

    };

    Toast mToast;

    private void showTip(final String str){

        runOnUiThread(new Runnable() {

            @Override

            public void run() {

                mToast.setText(str);

                mToast.show();

            }

        });

    }



    @Override

    protected void onStart() {

        super.onStart();

//        microFingerVein=MicroFingerVein.getInstance(this);

        bindTaskContract=new BindTaskContract();

        bindTaskContract.attachView(this);

        sendLogMessageTastContract=new SendLogMessageTastContract();

        sendLogMessageTastContract.attachView(this);

    }

    /**

     //     * 合成回调监听。

     */

    public SynthesizerListener mTtsListener = new SynthesizerListener() {



        @Override

        public void onSpeakBegin() {

        }

        @Override

        public void onSpeakPaused() {

        }

        @Override

        public void onSpeakResumed() {

        }

        @Override

        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override

        public void onCompleted(SpeechError speechError) {

        }

        @Override

        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }

        @Override

        public void onBufferProgress(int percent, int beginPos, int endPos,

                                     String info) {

        }

    };

    @Override

    protected void initViews(Bundle savedInstanceState) {

        tvTitle.setText(R.string.bind_finger);

        bind_one_tv.setText(R.string.put_number);

        bind_two_tv.setText(R.string.sure_message);

        bind_three_tv.setText(R.string.put_finger);

        bind_four_tv.setText(R.string.bind_finish);

        bindVeinMainFragment=new BindVeinMainFragment();

        mFragmentList.add(bindVeinMainFragment);

        FragmentManager fm=getSupportFragmentManager();

        SectionsPagerAdapter mfpa=new SectionsPagerAdapter(fm,mFragmentList); //new myFragmentPagerAdater记得带上两个参数

        viewPager.setAdapter(mfpa);

        viewPager.setCurrentItem(0);

    }

    @Override

    public void setActivtyChange(String string) {

        switch (string) {

            case "1":

//                mediaPlayer.start();

                mTts.startSpeaking(getResources().getString(R.string.put_number),mTtsListener);

                bind_one_Cimg.setImageResource(R.drawable.flow_circle_pressed);

                bind_one_line.setBackgroundResource(R.color.colorText);

                bind_one_tv.setTextColor(getResources().getColor(R.color.colorText));

                bind_two_Cimg.setImageResource(R.drawable.flow_circle);

                bind_two_line.setBackgroundResource(R.color.edittv);

                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_three_Cimg.setImageResource(R.drawable.flow_circle);

                bind_three_line.setBackgroundResource(R.color.edittv);

                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_four_Cimg.setImageResource(R.drawable.flow_circle);

                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));

                break;

            case "2":

//                mediaPlayer0.start();

                mTts.startSpeaking(getResources().getString(R.string.sure_message),mTtsListener);

                bind_one_Cimg.setImageResource(R.drawable.flow_circle);

                bind_one_line.setBackgroundResource(R.color.edittv);

                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_two_Cimg.setImageResource(R.drawable.flow_circle_pressed);

                bind_two_line.setBackgroundResource(R.color.colorText);

                bind_two_tv.setTextColor(getResources().getColor(R.color.colorText));

                bind_three_Cimg.setImageResource(R.drawable.flow_circle);

                bind_three_line.setBackgroundResource(R.color.edittv);

                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_four_Cimg.setImageResource(R.drawable.flow_circle);

                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));

                break;

            case "3":
                workHandler.sendEmptyMessage(18);
                mTts.startSpeaking(getResources().getString(R.string.put_finger),mTtsListener);

                layout_error_text.setVisibility(View.VISIBLE);

                bind_one_Cimg.setImageResource(R.drawable.flow_circle);

                bind_one_line.setBackgroundResource(R.color.edittv);

                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_two_Cimg.setImageResource(R.drawable.flow_circle);

                bind_two_line.setBackgroundResource(R.color.edittv);

                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_three_Cimg.setImageResource(R.drawable.flow_circle_pressed);

                bind_three_line.setBackgroundResource(R.color.colorText);

                bind_three_tv.setTextColor(getResources().getColor(R.color.colorText));

                bind_four_Cimg.setImageResource(R.drawable.flow_circle);

                bind_four_tv.setTextColor(getResources().getColor(R.color.edittv));

                break;

            case "4":

                mTts.startSpeaking(getResources().getString(R.string.bind_finish),mTtsListener);

                bind_one_Cimg.setImageResource(R.drawable.flow_circle);

                bind_one_line.setBackgroundResource(R.color.edittv);

                bind_one_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_two_Cimg.setImageResource(R.drawable.flow_circle);

                bind_two_line.setBackgroundResource(R.color.edittv);

                bind_two_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_three_Cimg.setImageResource(R.drawable.flow_circle);

                bind_three_line.setBackgroundResource(R.color.edittv);

                bind_three_tv.setTextColor(getResources().getColor(R.color.edittv));

                bind_four_Cimg.setImageResource(R.drawable.flow_circle_pressed);

                bind_four_tv.setTextColor(getResources().getColor(R.color.colorText));

                break;

        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override

    protected void initData() {

        text_tile.setText(R.string.membind_finger);

        mesReceiver=new MesReceiver();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BaseApplication.ACTION_UPDATEUI);

        registerReceiver(mesReceiver, intentFilter);



    }



    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);

    }






    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> list;

        public SectionsPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragmentList) {

            super(fm);

            this.list=mFragmentList;

        }

        @Override

        public Fragment getItem(int position) {

            return list.get(position);

        }

        @Override

        public int getCount() {

            return list.size();

        }

    }

    @OnClick(R.id.home_back_bt)

    public void onClick(View view){

        switch (view.getId()){

            case R.id.home_back_bt:

                finish();
                break;

        }

    }



    @Override

    public void sendLogSuccess(RestResponse resultResponse) {

    }



    @Override

    public void onPermissionError(ApiException e) {

        onError(e);

    }
    private List<MdDevice> mdDevicesList = new ArrayList<MdDevice>();
    private final int MSG_REFRESH_LIST = 0;
    private List<MdDevice> getDevList() {

        List<MdDevice> mdDevList = new ArrayList<MdDevice>();

        if (mdDeviceBinder != null) {

            int deviceCount = MicroFingerVein.fvdev_get_count();

            for (int i = 0; i < deviceCount; i++) {

                MdDevice mdDevice = new MdDevice();

                mdDevice.setNo(i);

                mdDevice.setIndex(mdDeviceBinder.getDeviceNo(i));

                mdDevList.add(mdDevice);

            }

        } else {

            Log.e(TAG, "microFingerVein not initialized by MdUsbService yet,wait a moment...");

        }

        return mdDevList;

    }
    public static MdDevice mdDevice;

    public MdUsbService.MyBinder mdDeviceBinder;
    private Handler listManageH = new Handler(new Handler.Callback() {

        @Override

        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_REFRESH_LIST: {

                    mdDevicesList.clear();

                    mdDevicesList = getDevList();

                    if (mdDevicesList.size() > 0) {

                        mdDevice = mdDevicesList.get(0);

                    } else {

                        listManageH.sendEmptyMessageDelayed(MSG_REFRESH_LIST, 1500L);

                    }

                    break;

                }

            }

            return false;

        }

    });
    private String TAG = "BindActivity";

    private ServiceConnection mdSrvConn = new ServiceConnection() {

        @Override

        public void onServiceConnected(ComponentName name, IBinder service) {

            mdDeviceBinder = (MdUsbService.MyBinder) service;


            if (mdDeviceBinder != null) {

                mdDeviceBinder.setOnUsbMsgCallback(mdUsbMsgCallback);

                listManageH.sendEmptyMessage(MSG_REFRESH_LIST);

                Log.e(TAG, "bind MdUsbService success.");

            } else {

                Log.e(TAG, "bind MdUsbService failed.");

                finish();

            }

        }

        @Override

        public void onServiceDisconnected(ComponentName name) {

            Log.e(TAG, "disconnect MdUsbService.");

        }


    };
    private MdUsbService.UsbMsgCallback mdUsbMsgCallback = new MdUsbService.UsbMsgCallback() {

        @Override

        public void onUsbConnSuccess(String usbManufacturerName, String usbDeviceName) {

            String newUsbInfo = "USB厂商：" + usbManufacturerName + "  \nUSB节点：" + usbDeviceName;

            Log.e(TAG, newUsbInfo);

        }

        @Override

        public void onUsbDisconnect() {

            Log.e(TAG, "USB连接已断开");


        }

    };
    Handler workHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 18:
                    if(textError==null){
                        return;
                    }
                    textError.setText(getString(R.string.register_template_tip));
                    handler.removeMessages(18);
                    int state = getState();
                    Log.e(TAG, state + "");
                    if (state == 3) {
                        workModel();
                    }
                    if(!isWorkFinsh){

                        workHandler.sendEmptyMessageDelayed(18, 1000);
                    }


                    break;

            }
        }};
    private boolean bOpen = false;//设备是否打开
    private int[] pos = new int[1];
    private float[] score = new float[1];
    private boolean ret;
    private ModelImgMng modelImgMng = new ModelImgMng();
    private int[] tipTimes = {0, 0};//后两次次建模时用了不同手指或提取特征识别时，最多重复提醒限制3次
    private int lastTouchState = 0;//记录上一次的触摸状态
    private int modOkProgress = 0;
    public int getState() {
        if (!bOpen) {
            modOkProgress = 0;
            modelImgMng.reset();
            bOpen = mdDeviceBinder.openDevice(0);//开启指定索引的设备
            if (bOpen) {
                Log.e(TAG, "open device success");
            } else {
                Log.e(TAG, "open device failed,stop identifying and modeling.");

            }
        }
        int state = mdDeviceBinder.getDeviceTouchState(0);
        if (state != 3) {
            if (lastTouchState != 0) {
                mdDeviceBinder.setDeviceLed(0, MdUsbService.getFvColorRED(), true);
            }
            lastTouchState = 0;
        }
        if (state == 3) {
            //返回值state=3表检测到了双Touch触摸,返回1表示仅指腹触碰，返回2表示仅指尖触碰，返回0表示未检测到触碰
            if (lastTouchState == 3) {
                textError.setText(getString(R.string.move_finger));
                return 4;
            }
            lastTouchState = 3;
            mdDeviceBinder.setDeviceLed(0, MdUsbService.getFvColorGREEN(), false);
            //optional way 3
            img = mdDeviceBinder.tryGrabImg(0);
            if (img == null) {
                Log.e(TAG, "get img failed,please try again");
            }
        }
        return state;
    }
    private final static float MODEL_SCORE_THRESHOLD = 0.4f;
    public void workModel() {
        float[] quaScore = {0f, 0f, 0f, 0f};
        int quaRtn = MdUsbService.qualityImgEx(img, quaScore);
        String oneResult = ("quality return=" + quaRtn) + ",result=" + quaScore[0] + ",score=" + quaScore[1] + ",fLeakRatio=" + quaScore[2] + ",fPress=" + quaScore[3];
        Log.e(TAG, oneResult);
        int quality = (int) quaScore[0];
        if (quality != 0) {

            textError.setText(getString(R.string.move_finger));
        }
        byte[] feature = MdUsbService.extractImgModel(img, null, null);
        if (feature == null) {
            textError.setText(getString(R.string.move_finger));
        } else {
            modOkProgress++;
            if (modOkProgress == 1) {//first model
                tipTimes[0] = 0;
                tipTimes[1] = 0;
                modelImgMng.setImg1(img);
                modelImgMng.setFeature1(feature);
                textError.setText(getString(R.string.again_finger));
            } else if (modOkProgress == 2) {//second model
                ret = MdUsbService.fvSearchFeature(modelImgMng.getFeature1(), 1, img, pos, score);
                if (ret && score[0] > MODEL_SCORE_THRESHOLD) {
                    feature = MdUsbService.extractImgModel(img, null, null);//无须传入第一张图片，第三次混合特征值时才同时传入3张图；
                    if (feature != null) {
                        tipTimes[0] = 0;
                        tipTimes[1] = 0;
                        modelImgMng.setImg2(img);
                        modelImgMng.setFeature2(feature);
                        textError.setText(getString(R.string.again_finger));
                    } else {//第二次建模从图片中取特征值无效
                        modOkProgress = 1;
                        if (++tipTimes[0] <= 3) {
                            textError.setText(getString(R.string.same_finger));
                        } else {//连续超过3次放了不同手指则忽略此次建模重来
                            modOkProgress = 0;
                            modelImgMng.reset();
                            textError.setText(getString(R.string.same_finger));
                        }
                    }
                } else {
                    modOkProgress = 1;
                    if (++tipTimes[0] <= 3) {
                        textError.setText(getString(R.string.same_finger));
                    } else {//连续超过3次放了不同手指则忽略此次建模重来
                        modOkProgress = 0;
                        modelImgMng.reset();
                        textError.setText(getString(R.string.same_finger));
                    }
                }
            } else if (modOkProgress == 3) {//third model
                ret = MdUsbService.fvSearchFeature(modelImgMng.getFeature2(), 1, img, pos, score);
                if (ret && score[0] > MODEL_SCORE_THRESHOLD) {
                    feature = MdUsbService.extractImgModel(modelImgMng.getImg1(), modelImgMng.getImg2(), img);
                    if (feature != null) {//成功生成一个3次建模并融合的融合特征数组
                        tipTimes[0] = 0;
                        tipTimes[1] = 0;
                        modelImgMng.setImg3(img);
                        modelImgMng.setFeature3(feature);
                        //----------------------------------------------------------
                        //if(isMdDebugOpen) {//保存3次建模后的3张图片用于分析异常情况;
                        //    String tips="DEBUG:本次建模图片及日志已经保存到:\n"+MdDebugger.debugModelSrcByTimeMillis(modelImgMng.getImg1(),modelImgMng.getImg2(),modelImgMng.getImg3());
                        //    Log.e(TAG,tips);
                        //    handler.obtainMessage(MSG_SHOW_LOG,tips).sendToTarget();
                        //}
                        //----------------------------------------------------------
                            final Person person = new Person();
                            person.setUid(System.currentTimeMillis()+"");
                            person.setFeature(HexUtil.bytesToHexString(feature));
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(person);
                                }
                            });
                        isWorkFinsh = true;
                        modelImgMng.reset();
                        fingersign();
                        mdDeviceBinder.closeDevice(0);
                        bOpen = false;
                    } else {//第三次建模从图片中取特征值无效
                        modOkProgress = 2;
                        if (++tipTimes[1] <= 3) {
                            textError.setText(getString(R.string.same_finger));
                        }
                    }
                } else {
                    modOkProgress = 2;
                    if (++tipTimes[1] <= 3) {
                        textError.setText(getString(R.string.same_finger));
                    } else {//连续超过3次放了不同手指则忽略此次建模重来
                        modOkProgress = 0;
                        modelImgMng.reset();
                        textError.setText(getString(R.string.same_finger));
                    }
                }
            } else {
                modOkProgress = 0;
                modelImgMng.reset();
            }
        }

    }
    private byte[] img;
    @Override

    public void onError(ApiException e) {

        String reg = "[^\u4e00-\u9fa5]";

        String syt=e.getMessage().replaceAll(reg, "");

        Logger.e("BindActivity"+syt);

        textError.setText(syt);

        mTts.startSpeaking(syt,mTtsListener);

    }


    @Override

    protected void onDestroy() {

        super.onDestroy();

        unregisterReceiver(mesReceiver);
        unbindService(mdSrvConn);
        isWorkFinsh=true;
        workHandler.removeCallbacksAndMessages(null);

    }










    @Override

    public void onResultError(ApiException e) {



    }



    @Override

    public void bindSuccess(Member returnBean) throws InterruptedException {

        setActivtyChange("4");

        textError.setText(R.string.bing_success);

        fingersign();


    }
    Handler handler = new Handler();
    private void fingersign(){
        textError.setText(R.string.bing_success);
        setActivtyChange("4");
        if (handler!=null) {

            handler.postDelayed(new Runnable() {

                @Override

                public void run() {
                    finish();

                }

            }, 3000);

        }

    }

    /**

     * 广播接收器

     *

     * @author kevin

     */

    public class MesReceiver extends BroadcastReceiver {

        @Override

        public void onReceive(Context context, Intent intent) {

            timeStr.setText(intent.getStringExtra("timethisStr"));

//            Logger.e("NewMainActivity" + intent.getStringExtra("timeStr"));


        }

    }

}