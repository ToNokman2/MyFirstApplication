package com.jnu.myfirstapplication3.interFace;

import android.content.Context;

import com.jnu.myfirstapplication3.data.TaskItem;


import java.util.ArrayList;

// 存储持久化接口
public interface TaskJoint {
    // 获得所有的任务
    ArrayList<TaskItem> loadTaskItems(Context context, String fileName);
    // 获得指定类型的任务
    ArrayList<TaskItem> loadTaskItems(Context context,String fileName,String type);

    // 保存数据
    void saveTaskItems(Context context,String fileName,ArrayList<TaskItem> taskData);
}