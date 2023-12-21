package com.jnu.myfirstapplication3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.myfirstapplication3.R;
import com.jnu.myfirstapplication3.data.TaskDataBank;
import com.jnu.myfirstapplication3.data.TaskItem;
import com.jnu.myfirstapplication3.interFace.TaskJoint;

import java.util.ArrayList;

public class TaskRecyclerviewAdapter extends RecyclerView.Adapter<TaskRecyclerviewAdapter.MyViewHolder>{
    private final String FILE_NAME = "taskData.ser";
    private TaskJoint taskJoint;
    private Context context;
    private ArrayList<TaskItem> taskList;
    public int menuId;

    public TaskRecyclerviewAdapter(Context context, ArrayList<TaskItem> taskList,int menuId) {
        this.context = context;
        this.taskList = taskList;
        this.menuId = menuId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.task_item_row,parent,false);
        return new MyViewHolder(view,menuId);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskItem taskItem = taskList.get(position);
        holder.taskTitle.setText(taskItem.getTaskTitle());
        holder.taskNumFinish.setText(taskItem.getTaskNum()+"min");
        holder.taskAddPoint.setText("+"+taskItem.getTaskPoint());
        holder.finishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition(); // 获取点击的位置

                if (position != RecyclerView.NO_POSITION) {
                    taskList.remove(position); // 从任务列表中移除该位置的任务项
                    notifyItemRemoved(position); // 通知适配器数据集发生变化


                    Intent intent = new Intent("MY_CUSTOM_ACTION");
                    intent.putExtra("data", "invalidate");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public int menuId;
        ImageView finishImage;
        TextView taskTitle;
        TextView taskNumFinish;
        TextView taskAddPoint;

        public MyViewHolder(@NonNull View itemView,int menuId) {
            super(itemView);
            finishImage = itemView.findViewById(R.id.task_item_img);
            taskTitle = itemView.findViewById(R.id.task_item_title);
            taskNumFinish = itemView.findViewById(R.id.task_num_finish);
            taskAddPoint = itemView.findViewById(R.id.task_num_point);
            this.menuId = menuId;

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(menuId,0,this.getAdapterPosition(),"修改");
            menu.add(menuId,1,this.getAdapterPosition(),"删除");
        }
    }
}