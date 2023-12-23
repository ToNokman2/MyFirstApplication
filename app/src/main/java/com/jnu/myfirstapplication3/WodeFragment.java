package com.jnu.myfirstapplication3;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.jnu.myfirstapplication3.data.RewardDataBank;
import com.jnu.myfirstapplication3.data.RewardItem;
import com.jnu.myfirstapplication3.data.TaskDataBank;
import com.jnu.myfirstapplication3.data.TaskItem;
import com.jnu.myfirstapplication3.interFace.RewardJoint;
import com.jnu.myfirstapplication3.interFace.TaskJoint;

import java.util.ArrayList;

public class WodeFragment extends Fragment {
    private final String FILE_TASK_NAME = "taskData.ser";
    private final String FILE_REWARD_NAME = "rewardData.ser";

    public WodeFragment() {
        // Required empty public constructor
    }

    public static WodeFragment newInstance() {
        WodeFragment fragment = new WodeFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_wode, container, false);
        Button clearDataButton = rootView.findViewById(R.id.ClearData);

        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        return rootView;
    }
    private void showDialog(String title,String message) {
        // 创建AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(title)
                .setMessage(message);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());


        builder.setTitle("清空缓存")
                .setMessage("是否确定清空所有缓存？");

        // 设置确认按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllData();
                dialog.dismiss();
            }
        });

        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void deleteAllData() {
        TaskJoint taskJoint = new TaskDataBank();
        RewardJoint rewardJoint = new RewardDataBank();
        ArrayList<TaskItem> emptyTaskList = new ArrayList<>();
        ArrayList<RewardItem> emptyRewardList = new ArrayList<>();
        taskJoint.saveTaskItems(requireActivity(),FILE_TASK_NAME,emptyTaskList);
        rewardJoint.saveRewardItems(requireActivity(),FILE_REWARD_NAME,emptyRewardList);
        showDialog("清空成功","请重启软件");
    }
}
