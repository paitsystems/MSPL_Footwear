package com.lnbinfotech.msplfootwearex.constant;

//Created by Anup on 03-07-2018.

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.services.LocationUpdateJobService;
import com.lnbinfotech.msplfootwearex.services.LocationUpdateJobServicePL;
import com.lnbinfotech.msplfootwearex.services.ScheduledJobService;
import com.lnbinfotech.msplfootwearex.services.ScheduledJobServicePL;

public class Utility {

    public static void scheduledJob(Context context) {
        //TODO : Change time period
        long period = 15 * 60 * 1000;
        Constant.checkFolder(Constant.folder_name);
        Constant.checkFolder(Constant.folder_name+"/"+Constant.zipFolderName);
        Constant.checkFolder(Constant.folder_name+"/"+Constant.unzipFolderName);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ComponentName component = new ComponentName(context, ScheduledJobServicePL.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, component);
            builder.setPeriodic(period);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiresCharging(false);
            JobScheduler scheduler = context.getSystemService(JobScheduler.class);
            scheduler.schedule(builder.build());
            Constant.showLog("scheduledJob_JobScheduler");
            /*ComponentName component = new ComponentName(context, LocationUpdateJobServicePL.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, component);
            builder.setPeriodic(period);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiresCharging(false);
            JobScheduler scheduler = context.getSystemService(JobScheduler.class);
            scheduler.schedule(builder.build());
            Constant.showLog("scheduledJob_JobScheduler");*/
        } else {
            //TODO : Change time period
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
            Job job = dispatcher.newJobBuilder()
                    .setLifetime(Lifetime.FOREVER)
                    .setTag("TAG")
                    .setReplaceCurrent(false)
                    .setService(ScheduledJobService.class)
                    .setRecurring(true)
                    .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                    .setTrigger(Trigger.executionWindow(15*60, 20*60))
                    .build();
            dispatcher.mustSchedule(job);
            Constant.showLog("scheduledJob_FirebaseJobDispatcher");
            /*FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
            Job job = dispatcher.newJobBuilder()
                    .setLifetime(Lifetime.FOREVER)
                    .setTag("TAG")
                    .setReplaceCurrent(false)
                    .setService(LocationUpdateJobService.class)
                    .setRecurring(true)
                    .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                    .setTrigger(Trigger.executionWindow(110, 120))
                    .build();
            dispatcher.mustSchedule(job);
            Constant.showLog("scheduledJob_FirebaseJobDispatcher");*/
        }
        new WriteLog().writeLog(context, "Utility_scheduleJob_Job Scheduled");
    }
}

