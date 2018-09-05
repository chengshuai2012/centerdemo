package com.link.cloud.base;

import com.google.gson.JsonObject;
import com.link.cloud.bean.CabinetNumberData;
import com.link.cloud.bean.Code_Message;
import com.link.cloud.bean.DeviceData;
import com.link.cloud.bean.DeviceUpdate;
import com.link.cloud.bean.DownLoadData;
import com.link.cloud.bean.FaceBindBean;
import com.link.cloud.bean.Lockdata;
import com.link.cloud.bean.Member;
import com.link.cloud.bean.PagesInfoBean;
import com.link.cloud.bean.RestResponse;
import com.link.cloud.bean.ResultHeartBeat;
import com.link.cloud.bean.ResultResponse;
import com.link.cloud.bean.Sign_data;
import com.link.cloud.bean.SyncFeaturesPage;
import com.link.cloud.bean.SyncUserFace;
import com.link.cloud.bean.UpDateBean;
import com.link.cloud.bean.UpdateMessage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Shaozy on 2016/8/10.
 */
public interface BaseService {
    @POST("Accounts/{accountSid}/SMS/TemplateSMS")
    Observable<RestResponse> sendTemplateSMS(@Path("accountSid") String accountSid, @Body JsonObject params);
    @POST("bindUserFinger")
    Observable<Member> bindVeinMemeber(@Body JsonObject params);
    @POST("getNotReveiceFeature")
    Observable<DownLoadData>downloadNotReceiver(@Body JsonObject params);

    @POST("syncUserFeatureCount")
    Observable<PagesInfoBean>getPagesInfo(@Body JsonObject params);

    @POST("syncUserFeaturePages")
    Observable<SyncFeaturesPage>syncUserFeaturePages(@Body JsonObject params);
    @POST("appUpdateInfo")
    Observable<UpDateBean>appUpdateInfo(@Body JsonObject params);

    @POST("openBrakeByQrCode")
    Observable<Code_Message>validationQrCode(@Body JsonObject paras);
    /**
     * 2.心跳包
     */
    @POST("deviceHeartBeat")
    Observable<ResultHeartBeat>deviceHeartBeat(@Body JsonObject params);
    /**
     * 同步接口
     * @param params
     * @return
     */
    @POST("syncSignUserFeature")
    Observable<Sign_data>syncSignUserFeature(@Body JsonObject params);

    /**
     * 12.检测软件版本
     */
    @POST("deviceUpgrade")
    Observable<UpdateMessage>deviceUpgrade(@Body JsonObject params);

    /**
     * 7.发送日志信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<ReturnBean>
     */
    @POST("validateLogs")
    Observable<RestResponse> sendLogMessage(@Body JsonObject params);

    //2018
    @POST("deviceRegister")
    /**
     * 13.获取设备id
     */
    Observable<DeviceData>getdeviceId(@Body JsonObject params);
    /**
     * 储物柜操作
     * @param params
     * @return
     */
    @POST("isOpenBrake")
    Observable<Lockdata>isOpenCabinet(@Body JsonObject params);

    /**
     * 清除储物柜
     * @param params
     * @return
     */
    @POST("clearCabinet")
    Observable<ResultResponse>clearCabinet(@Body JsonObject params);

    /**
     * 管理员开柜
     * @param params
     * @return
     */
    @POST("openCabinet")
    Observable<ResultResponse>adminiOpenCabinet(@Body JsonObject params);

    /**
     * 下载指静脉数据
     * @param params
     * @return
     */
    @POST("downloadFeature")
    Observable<DownLoadData>downloadFeature(@Body JsonObject params);

    /**
     * 同步接口
     * @param params
     * @return
     */
    @POST("syncUserFeature")
    Observable<DownLoadData>syncUserFeature(@Body JsonObject params);

    /**
     * 查询柜号列表
     * @param params
     * @return
     */
    @POST("cabinetNumberList")
    Observable<CabinetNumberData>cabinetNumberList(@Body JsonObject params);

    @POST("deviceUpdate")
    Observable<DeviceUpdate>deviceUpdate(@Body JsonObject params);
    @Streaming //大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
    @POST("syncUserFacePages")
    Observable<SyncUserFace>syncUserFacePages(@Body JsonObject params);
    @Multipart
    @POST("bindUserFace")
    Observable<FaceBindBean>bindFace(@Part("deviceId") RequestBody deviceId,
                                     @Part("userType")RequestBody userType,
                                     @Part("numberValue")RequestBody numberValue,
                                     @Part("numberType")RequestBody numberType,
                                     @Part("code")RequestBody code,
                                     @Part("key")RequestBody key,
                                     @Part("datetime")RequestBody datetime,
                                     @Part("sign")RequestBody sign,
                                     @Part() MultipartBody.Part faceImage,
                                     @Part() MultipartBody.Part faceData

    );
    @POST("validationUser")
    Observable<Member> getMemInfo( @Body JsonObject params);
    @POST("validationUser")
    Observable<FaceBindBean> getMemFace( @Body JsonObject params);
}
