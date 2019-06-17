package j.s.yarlykov.data.network.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseProvider {

    private static FirebaseProvider instance = null;
    private Firebase api;

    private FirebaseProvider() {
        api = createAdapter();
    }

    public static FirebaseProvider getInstance() {
        if(instance == null) {
            instance = new FirebaseProvider();
        }
        return instance;
    }

    public Firebase getApi() {
        return api;
    }

    private static Firebase createAdapter(){

        // Установить таймауты
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(Firebase.class);
    }

}
