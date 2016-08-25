package com.ihandy.a2014011367.wtfnews.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataPullingService extends Service {
    private DataPullingService theInstance = null;
    public DataPullingService getInstance() {
        return theInstance;
    }
    public DataPullingService() {
    }

    @Override
    public void onCreate() {
        theInstance = this;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
