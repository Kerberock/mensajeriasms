package lania.edu.mx.mensajeriasms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SendSMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
    }

    String getTxt(int key){ return ((EditText) findViewById(key)).getText().toString(); }

    void DisplayText(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void check(View v){
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
        startActivity(intent);

        DisplayText("Finish! :')");

        /*Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("sms:"+getTxt(R.id.txtnum)));
        i.putExtra("sms_body", getTxt(R.id.txtcuerpecito));
        startActivity(i);*/
    }

    public void mandarSMS(View v){
        String numTel = getTxt(R.id.txtnum);
        String msg = getTxt(R.id.txtcuerpecito);

        String enviado = "SMS ENVIADO COOL";
        String recibido = "SMS ENTREGADO COOL";

        PendingIntent sentIntent = PendingIntent.getBroadcast(this,
                0, new Intent(enviado), 0);

        PendingIntent deliveryIntent = PendingIntent.getBroadcast(this,
                0, new Intent(recibido), 0);

        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(numTel, null, msg, sentIntent, deliveryIntent);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        DisplayText("Mensaje enviado cool!");
                        break;

                    case Activity.RESULT_CANCELED:
                        DisplayText("Mensaje no enviado :(");
                        break;
                }
            }
        }, new IntentFilter(enviado));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        DisplayText("Mensaje recibido!");
                        break;

                    case Activity.RESULT_CANCELED:
                        DisplayText("Mensaje no recibido :(");
                        break;
                }
            }
        }, new IntentFilter(recibido));
    }
}
