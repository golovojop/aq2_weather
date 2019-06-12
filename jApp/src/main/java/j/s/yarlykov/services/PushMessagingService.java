package j.s.yarlykov.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import j.s.yarlykov.R;
import j.s.yarlykov.ui.MainActivity;
import j.s.yarlykov.util.Utils;

/**
 * Legacy Server Key
 * AIzaSyBWI-ySOo3LMex1e-y27jBmLHhO6VRTydQ
 *
 * API Key for web app (видимо для сервера рассылающего Push)
 * AIzaSyD_2KJ1O7QM7vZ9nu3hRoW-SoKDUx7Sk3U
 *
 */

public class PushMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage == null) {
            Utils.logI(this, "onMessageReceived: remoteMessage = null");
            return;
        }

        /**
         * Должен прилететь вот такой json
         * 	    {
         * 		"city" : "Moscow",
         * 	    "country" : "ru",
         * 		"temperature" : "21",
         * 		"wind" : "4.1",
         * 		"humidity" : "87",
         * 		"pressure" : "765"
         * 		}
         */
        StringBuilder sb = new StringBuilder();
        List<String> keys = Arrays.asList(
                "city",
                "country",
                "temperature",
                "wind",
                "humidity",
                "pressure");

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            for(String k : keys) {
                try {
                    sb.append(String.format("%s: %s\n", k, data.get(k)));
                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }

            if(sb.length() > 0) {
                makeNotification(getApplicationContext(), sb.toString());
            }
        }

        if (remoteMessage.getNotification() != null) {
            Utils.logI(this, "Notification received");
        }
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    private void makeNotification(Context context, String messageBody) {
        String channelId = "101";

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setContentTitle("Forecast")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendRegistrationToServer(String token) {
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
    }
}
