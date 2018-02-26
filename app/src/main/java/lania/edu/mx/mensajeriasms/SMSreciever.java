package lania.edu.mx.mensajeriasms;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSreciever extends BroadcastReceiver {

    String TAG = SMSreciever.class.getSimpleName();
    String BannerNum = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";

        for(String key : bundle.keySet()){
            Log.d("LLAVES", key + " - " + bundle.get(key));
        }

        Log.w("BLESSWARE", "Estamos en onReceive");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            assert pdus != null;
            msgs = new SmsMessage[pdus.length];

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String num = msgs[i].getOriginatingAddress();
                Log.d("TESTEO COOL", num);

                Uri uri = Uri.parse("content://sms/inbox");
                Cursor c = context.getContentResolver().query(
                        uri, new String[]{"_id", "thread_id", "address", "person",
                                "date", "body"}, null, null, null);

                if(c != null && c.moveToFirst()) {
                    do {
                        long id = c.getLong(0);
                        long threadId = c.getLong(1);
                        String address = c.getString(2);
                        String date = c.getString(3);
                        String body = c.getString(5);
                        Log.d("BLESSWARE", +id+" "+address+": "+body+" -- " + date);
                    } while (c.moveToNext());
                }


                if(num.equals(BannerNum)){
                    Log.d("SPAM", "Este número es SPAM");
                    uri = Uri.parse("content://sms/inbox");
                    c = context.getContentResolver().query(
                            uri, new String[]{"_id", "thread_id", "address", "person",
                                    "date", "body"}, null, null, null);
                    int count = 0;
                    if(c != null && c.moveToFirst()){
                        do{
                            long id = c.getLong(0);
                            long threadId = c.getLong(1);
                            String address = c.getString(2);
                            String date = c.getString(3);
                            String body = c.getString(5);
                            Log.d("SPAM", address);
                            if(address.equals(num)) {
                                try {
                                    count = context.getContentResolver().delete(
                                            Uri.parse("content://sms/"),
                                            "_id=" + id,
                                            null);
                                } catch (Exception e) { Log.d("SPAM" , "Error: "
                                        + e.getMessage()); }

                                Log.d("SPAM", "Eliminados: "+id+"-"+count);

                            }
                        } while(c.moveToNext());
                    }
                    abortBroadcast();
                    return;
                }

                // extraer el número de orígen
                str += "SMS RECIBIDO DESDE " + msgs[i].getOriginatingAddress() + ":";
                str += msgs[i].getMessageBody();
                str += "\n";
            }

            Log.w("BLESSWARE", str);
            Log.w(TAG, str);
        } else {
            Log.w("BLESSWARE", "No hay nada en extras almacenados en bundle");
        }
    }
}