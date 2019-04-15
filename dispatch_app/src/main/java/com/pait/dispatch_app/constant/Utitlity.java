package com.pait.dispatch_app.constant;

//Created by Sneha on 22-06-2018.

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.services.ScheduledJobService;
import com.pait.dispatch_app.services.ScheduledJobServicePL;

public class Utitlity {

    public static void scheduledJob(Context context){
        long period = 15*60*1000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ComponentName component = new ComponentName(context, ScheduledJobServicePL.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, component);
            builder.setPeriodic(period);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiresCharging(false);
            JobScheduler scheduler = context.getSystemService(JobScheduler.class);
            scheduler.schedule(builder.build());
        }else{
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
            Job job = dispatcher.newJobBuilder()
                    .setLifetime(Lifetime.FOREVER)
                    .setTag("TAG")
                    .setReplaceCurrent(false)
                    .setService(ScheduledJobService.class)
                    .setRecurring(true)
                    .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                    .setTrigger(Trigger.executionWindow(15*60,20*60))
                    .build();
            dispatcher.mustSchedule(job);
        }
        new WriteLog().writeLog(context,"Utility_scheduleJob_Job Scheduled");
    }
}
