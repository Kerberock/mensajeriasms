package lania.edu.mx.mensajeriasms;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if(!Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(
                    getApplicationContext().getPackageName())){
                Intent i = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                i.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        getApplicationContext().getPackageName());
                startActivity(i);
            }
        }
    }

    public void onClickSendSMS(View v){
        Intent i = new Intent(getBaseContext(), SendSMSActivity.class);
        startActivity(i);
    }

    int NOTIFICATION = 81237;

    public void disabledRecieved(View v){
        ComponentName reciever = new ComponentName(this, SMSreciever.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(reciever,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        //DisplayText("Sin mensaje");
        ((NotificationManager) getApplicationContext().getSystemService(
                Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION);
    }
}