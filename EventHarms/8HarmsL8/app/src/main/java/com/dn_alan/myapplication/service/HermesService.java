package com.dn_alan.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HermesService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
