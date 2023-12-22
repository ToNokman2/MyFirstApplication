package com.jnu.myfirstapplication3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
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
    private static final int REQUEST_CODE_TASK_DETAILS = 1;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phase_task, container, false);

        // 初始化任务列表数据
        initTaskList();

        // 设置RecyclerView和Adapter
        setupRecyclerView(rootView);

        return rootView;
    }

    private void initTaskList() {
        taskjoint = new TaskDataBank();
        taskListAll = taskjoint.loadTaskItems(requireActivity().getApplicationContext(), FILE_NAME);
        taskListWithType = filterWithType(taskListAll, taskType);
    }

    private void setupRecyclerView(View rootView) {
        taskRecyclerView = rootView.findViewById(R.id.task_recyclerView);
        taskRecyclerviewAdapter = new TaskRecyclerviewAdapter(requireActivity(), taskListWithType, this.menuId);
        taskRecyclerView.setAdapter(taskRecyclerviewAdapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        registerForContextMenu(taskRecyclerView);
        updateTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                this::handleUpdateTaskResult);
    }

    private void handleUpdateTaskResult(ActivityResult result) {
        if (result.getResultCode() == requireActivity().RESULT_OK) {
            Intent intent = result.getData();
            int position = intent.getIntExtra("position", 0);
            String title = intent.getStringExtra("title");
            int points = Integer.parseInt(intent.getStringExtra("points"));
            int numbers = Integer.parseInt(intent.getStringExtra("time"));
            updateTaskInformation(position, title, points, numbers);
        } else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {
        }
    }

    private void updateTaskInformation(int position, String title, int points, int numbers) {
        TaskItem task = taskListWithType.get(position);
        task.setTaskTitle(title);
        task.setTaskPoint(points);
        task.setTaskNum(numbers);

        if (taskJoint != null) {
            taskRecyclerviewAdapter.notifyItemChanged(position);
            taskJoint.saveTaskItems(requireActivity().getApplicationContext(), FILE_NAME, taskListWithType);
        }
    }
    private static final int MENU_ITEM_DELETE = 1;
    private static final int MENU_ITEM_UPDATE = 0;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() != menuId) {
            return false;
        }
        switch (item.getItemId()) {
            case MENU_ITEM_UPDATE:
                launchUpdateTaskActivity(item.getOrder());
                break;
            case MENU_ITEM_DELETE:
                showDeleteConfirmationDialog(item.getOrder());
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void launchUpdateTaskActivity(int order) {
        Intent intentUpdate = new Intent(requireActivity(), TaskDetailsActivity.class);
        TaskItem selectTask = taskListWithType.get(order);
        intentUpdate.putExtra("title", selectTask.getTaskTitle());
        intentUpdate.putExtra("points", String.valueOf(selectTask.getTaskPoint()));
        intentUpdate.putExtra("time", String.valueOf(selectTask.getTaskNum()));
        intentUpdate.putExtra("position", order);
        updateTaskLauncher.launch(intentUpdate);
    }

    private void showDeleteConfirmationDialog(int order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("删除任务");
        builder.setMessage("你确定删除这个任务？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            deleteTask(order);
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 取消删除操作
        });
        builder.create().show();
    }

    private void deleteTask(int order) {
        taskListWithType.remove(order);
        taskRecyclerviewAdapter.notifyItemRemoved(order);
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