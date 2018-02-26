package lania.edu.mx.mensajeriasms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SendSMSService extends Service {

    public SendSMSService(){

    }

    @Override
    public IBinder onBind(Intent i){
        throw new UnsupportedOperationException("Aún no se implementa chato");
    }
}
