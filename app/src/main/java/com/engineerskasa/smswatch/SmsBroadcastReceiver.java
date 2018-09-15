package com.engineerskasa.smswatch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.engineerskasa.smswatch.Interface.Services;
import com.engineerskasa.smswatch.Model.Post;
import com.engineerskasa.smswatch.Model.SMS_Sent;
import com.engineerskasa.smswatch.Model.SMS_Unsent;
import com.engineerskasa.smswatch.Remote.APIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static String sender,messagees;
    private Services mAPIService;
    static boolean activated = false;
    public UnsentAdapter mAdapter;
    public SMSAdapter Adapter;
    private List<SMS_Unsent> unsentList = new ArrayList<>();
    private List<SMS_Sent> sentList = new ArrayList<>();
    public SQLiteDBHelper db;

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        db = new SQLiteDBHelper(context);
        Adapter = new SMSAdapter(context, sentList);
        mAdapter = new UnsentAdapter(context, unsentList);
        sentList.addAll(db.getAllSentMessages());
        unsentList.addAll(db.getAllUnsentMessages());
        mAPIService = APIUtils.getAPIService();
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "n";
            }
            //---display the new SMS message


                sender = msgs[0].getOriginatingAddress();
                messagees = msgs[0].getMessageBody().toString();
            if (sender.equals("MobileMoney")) {
                mAPIService.savePost(sender, messagees, 1).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {

                        if (response.isSuccessful()) {
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
                            broadcastIntent.putExtra("sender", sender);
                            broadcastIntent.putExtra("message", messagees);
                            context.sendBroadcast(broadcastIntent);
                            createSentDbSMS(sender, messagees);
                            getNotification(context, sender, messagees);
                        }
                    }
                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("SMS_RECEIVEDD_ACTION");
                        broadcastIntent.putExtra("sender", sender);
                        broadcastIntent.putExtra("message", messagees);
                        context.sendBroadcast(broadcastIntent);
                        createDbSMS(sender, messagees);
                        getNotification(context, sender, messagees);
                    }
                });

            }
        }
    }

    public void getNotification(Context context, String sender,String message) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle(sender)
                .setContentText(message)
                .setSound(defaultSoundUri);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        notificationManager.notify(id, notificationBuilder.build());
    }
    public void createDbSMS(String sender,String message) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertUnsentSMS(sender,message);

        // get the newly inserted note from db
        SMS_Unsent n = db.getUnsent(id);

        if (n != null) {
            // adding new note to array list at 0 position
            unsentList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            //toggleEmptySMS();
        }
    }
    public void createSentDbSMS(String sender,String message) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertSentSMS(sender,message);

        // get the newly inserted note from db
        SMS_Sent n = db.getSent(id);

        if (n != null) {
            // adding new note to array list at 0 position
            sentList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            //toggleEmptySMS();
        }
    }

}
