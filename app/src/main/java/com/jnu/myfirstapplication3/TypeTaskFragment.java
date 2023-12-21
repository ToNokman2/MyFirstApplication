package com.jnu.myfirstapplication3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.myfirstapplication3.Adapter.TaskRecyclerviewAdapter;
import com.jnu.myfirstapplication3.data.DataBank;
import com.jnu.myfirstapplication3.data.TaskItem;
import com.jnu.myfirstapplication3.interFace.TaskJoint;
import com.jnu.myfirstapplication3.data.TaskDataBank;


import java.util.ArrayList;


public class TypeTaskFragment extends Fragment {

    private RecyclerView taskRecyclerView;
    private ActivityResultLauncher<Intent> updateTaskLauncher;
    public TaskRecyclerviewAdapter taskRecyclerviewAdapter;
    private TaskJoint taskJoint;
    public int menuId;

    public String taskType;
    private TaskJoint taskjoint;
    private ArrayList<TaskItem> taskListAll;
    public ArrayList<TaskItem> taskListWithType;
    private final String FILE_NAME = "taskData.ser";

    public TypeTaskFragment() {
        // Required empty public constructor
    }
    public TypeTaskFragment(String taskType,int menuId){
        this.menuId = menuId;
        this.taskType = taskType;
    }

    public static TypeTaskFragment newInstance() {
        TypeTaskFragment fragment = new TypeTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phase_task, container, false);

        taskjoint = new TaskDataBank();     // 获取数据
        System.out.println(taskType);
        taskListAll = taskjoint.loadTaskItems(requireActivity().getApplicationContext(),FILE_NAME);
        taskListWithType = filterWithType(taskListAll,taskType);
        System.out.println(taskListAll);

        taskRecyclerView = rootView.findViewById(R.id.task_recyclerView);        // 设置RecyclerView和Adapter
        taskRecyclerviewAdapter = new TaskRecyclerviewAdapter(requireActivity(), taskListWithType,this.menuId);
        taskRecyclerView.setAdapter(taskRecyclerviewAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        registerForContextMenu(taskRecyclerView);
        updateTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        Intent intent = result.getData();
                        int position = intent.getIntExtra("position",0);
                        String title = intent.getStringExtra("title");
                        int points = Integer.parseInt(intent.getStringExtra("points"));
                        int numbers = Integer.parseInt(intent.getStringExtra("time"));
                        TaskItem task = taskListWithType.get(position);
                        task.setTaskTitle(title);
                        task.setTaskPoint(points);
                        task.setTaskNum(numbers);

                        taskRecyclerviewAdapter.notifyItemChanged(position);
                        taskJoint.saveTaskItems(requireActivity().getApplicationContext(),FILE_NAME, taskListWithType);
                    }
                    else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {
                    }
                });

        return rootView;
    }
    private static final int MENU_ITEM_UPDATE = 0;
    private static final int MENU_ITEM_DELETE = 1;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getGroupId() != menuId){
            return false;
        }
        switch (item.getItemId()) {
            case MENU_ITEM_UPDATE:
                Intent intentUpdate = new Intent(requireActivity(), TaskDetailsActivity.class);
                TaskItem selectTask = taskListWithType.get(item.getOrder());
                intentUpdate.putExtra("title",selectTask.getTaskTitle());
                intentUpdate.putExtra("points",String.valueOf(selectTask.getTaskPoint()));
                intentUpdate.putExtra("time",String.valueOf(selectTask.getTaskNum()));
                intentUpdate.putExtra("position",item.getOrder());
                updateTaskLauncher.launch(intentUpdate);
                break;
            case MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("删除任务");
                builder.setMessage("你确定删除这个任务？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = item.getOrder();

                        taskListWithType.remove(position);
                        taskRecyclerviewAdapter.notifyItemRemoved(position);

                    }
                });
                builder.setNegativeButton("否定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    private ArrayList<TaskItem> filterWithType(ArrayList<TaskItem> taskListAll,String taskType){
        ArrayList<TaskItem> taskFilterWithType = new ArrayList<>();
        for(TaskItem task:taskListAll){
            if(task.getTaskType().equals(taskType)){
                taskFilterWithType.add(task);
            }
        }
        return taskFilterWithType;
    }
}