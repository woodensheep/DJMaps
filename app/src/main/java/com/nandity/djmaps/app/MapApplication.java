package com.nandity.djmaps.app;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lemon on 2016/10/12.
 */
public class MapApplication extends Application{
    private static MapApplication instance;
    private List<Activity> activityList = new LinkedList<Activity>();
    private  int plan=1;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        getInstance();
    }

    // 单例模式获取唯一的MyApplication实例
    public static MapApplication getInstance() {
        return instance;
    }

    // 添加Activity到容器中
    //myapplication = (myapplication) getApplication();
    //myapplication.getInstance().addActivity(this);
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public  void addPlan(){
        //plan++;
    }
    public  void removePlan(){
        //plan--;
    }
    public  int getPlanNumber(){
        return plan;
    }
}
