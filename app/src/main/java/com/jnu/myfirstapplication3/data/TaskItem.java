package com.jnu.myfirstapplication3.data;

import java.io.Serializable;

// 任务类
public class TaskItem implements Serializable {
    private String taskTitle;
    private int taskPoint;
    private int taskNum;
    private int taskNumFinish;
    private String taskType;
    private String taskTag;

    public TaskItem() {
    }

    public TaskItem(String taskTitle, int taskPoint, int taskNum, int taskNumFinish, String taskType) {

        this.taskTitle = taskTitle;
        this.taskPoint = taskPoint;
        this.taskNum = taskNum;
        this.taskNumFinish = taskNumFinish;
        this.taskType = taskType;

    }



    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public int getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(int taskPoint) {
        this.taskPoint = taskPoint;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public int getTaskNumFinish() {
        return taskNumFinish;
    }

    public void setTaskNumFinish(int taskNumFinish) {
        this.taskNumFinish = taskNumFinish;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }




}
