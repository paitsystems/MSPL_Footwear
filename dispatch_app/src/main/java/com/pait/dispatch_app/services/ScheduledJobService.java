package com.pait.dispatch_app.services;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.log.WriteLog;

import java.util.List;

//Created by Sneha on 22-06-2018.

public class ScheduledJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Constant.showLog("onStopJob");
        writeLog("onStopJob");
        return false;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getBaseContext(), "ScheduledJobService_" + data);
    }

}
