package com.jnu.myfirstapplication3.data;

import android.content.Context;

import com.jnu.myfirstapplication3.interFace.TaskJoint;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TaskDataBank implements TaskJoint {

    @Override
    public ArrayList<TaskItem> loadTaskItems(Context context, String fileName) {
        ArrayList<TaskItem> taskData = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            taskData = (ArrayList<TaskItem>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskData;
    }

    @Override
    public ArrayList<TaskItem> loadTaskItems(Context context, String fileName, String taskType) {
        ArrayList<TaskItem> taskData = loadTaskItems(context,fileName);
        ArrayList<TaskItem> taskDataFilter = new ArrayList<>();
        for(TaskItem task : taskData){
            if(task.getTaskType().equals(taskType)){
                taskDataFilter.add(task);
            }
        }
        return taskDataFilter;
    }


    @Override
    public void saveTaskItems(Context context, String fileName, ArrayList<TaskItem> taskData) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(taskData);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}