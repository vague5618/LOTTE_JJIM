package com.example.n.lotte_jjim.Activity.Service;

/**
 * Created by n on 2016-02-01.
 */import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.n.lotte_jjim.Activity.Activity.MainActivity;
import com.example.n.lotte_jjim.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by saltfactory on 6/8/15.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    public static final String mPackageName = "com.example.n.lotte_jjim";

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String title = data.getString("title");
        String message = data.getString("message");
        int productId = Integer.parseInt(data.getString("productId"));
        String userId = data.getString("userId");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        SharedPreferences setting = getSharedPreferences("MYID", MODE_PRIVATE);
        String mCurrentUserID = setting.getString("ID","");

        if(mCurrentUserID.equals(userId)) {
            // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.

            Log.d(TAG, "SAME");

            Log.d(TAG, "mCureentUserID: " + mCurrentUserID);

            Log.d(TAG, "UserId: " + userId);
            sendNotification(title, message, getImageId(productId));
        }
    }

    public int getImageId(int productID) {
        String tmpProductRes = "a" + productID;
        int tmpProductImage = getResources().getIdentifier(tmpProductRes, "drawable", mPackageName);
        //Drawable drawable = getResources().getDrawable(tmpProductImage);
        return tmpProductImage;
    }

    private Bitmap imageResize(Bitmap targetmage) {
        Bitmap bitmapResized = Bitmap.createScaledBitmap(targetmage, 180, 180, false);
        return bitmapResized;
    }

    private Bitmap getBitmap(int getId)
    {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), getId);
        return bm;
    }

    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message, int productId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(imageResize(getBitmap(productId)))
                .setSmallIcon(productId)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
