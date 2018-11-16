package com.pait.exec.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.location.Location;
import android.os.Build;

import com.pait.exec.constant.Constant;
import com.pait.exec.location.LocationProvider;
import com.pait.exec.log.WriteLog;

//Created by Anup on 01-09-2018.

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LocationUpdateJobServicePL extends JobService
        implements LocationProvider.LocationCallback1 {

    private LocationProvider provider;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Constant.showLog("onStartJob");
        provider = new LocationProvider(getApplicationContext(),this);
        provider.connect();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Constant.showLog("onStopJob");
        writeLog("onStopJob");
        provider.disconnect();
        return false;
    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getBaseContext(), "ScheduledJobServicePL_" + data);
    }

    @Override
    public void handleNewLocation(Location location, String address) {
        try {
            Constant.showLog("handleNewLocation");
            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            Constant.showLog("LocationUpdateJobService_"+lat + "-" + lon);Constant.showLog(address);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void locationAvailable() {
        Constant.showLog("Location Available");
    }

}
