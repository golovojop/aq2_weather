package j.s.yarlykov.data.network.api;

import j.s.yarlykov.data.network.model.firebase.FcmResponseModel;
import j.s.yarlykov.data.network.model.firebase.PushMessageModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Firebase {
    @POST("fcm/send")
    Call<FcmResponseModel> sendPushNotification(
            @Header ("Authorization") String key,
            @Header("Content-Type") String contentType,
            @Body PushMessageModel pushOut);
}
