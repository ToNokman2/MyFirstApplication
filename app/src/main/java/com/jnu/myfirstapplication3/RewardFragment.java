package com.jnu.myfirstapplication3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jnu.myfirstapplication3.Adapter.RewardRecyclerviewAdapter;
import com.jnu.myfirstapplication3.data.BookItem;
import com.jnu.myfirstapplication3.data.DataBank;
import com.jnu.myfirstapplication3.data.RewardDataBank;
import com.jnu.myfirstapplication3.data.RewardItem;
import com.jnu.myfirstapplication3.data.TaskDataBank;
import com.jnu.myfirstapplication3.data.TaskItem;
import com.jnu.myfirstapplication3.interFace.RewardJoint;
import com.jnu.myfirstapplication3.interFace.TaskJoint;

import java.util.ArrayList;

public class RewardFragment extends Fragment {
    private final String FILE_TASK_NAME = "taskData.ser";
    private final String FILE_REWARD_NAME = "rewardData.ser";
    private TaskJoint taskJoint;
    private RewardJoint rewardJoint;
    private ArrayList<TaskItem> taskList;
    private ArrayList<RewardItem> rewardList;
    private int pointSumText;
    private ActivityResultLauncher<Intent> addRewardLauncher;
    private ActivityResultLauncher<Intent> updateRewardLauncher;
    private RecyclerView rewardRecyclerView;
    public RewardRecyclerviewAdapter rewardRecyclerviewAdapter;

    private TextView sumPointView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("MY_CUSTOM_ACTION")) {
                taskList = taskJoint.loadTaskItems(requireActivity(), FILE_TASK_NAME);
                rewardList = rewardJoint.loadRewardItems(requireActivity(),FILE_REWARD_NAME);
                pointSumText = getPointSum(taskList,rewardList);
                sumPointView.setText("   "+pointSumText);
            }
        }
    };
    public RewardFragment() {
        // Required empty public constructor
    }
    public static RewardFragment newInstance() {
        RewardFragment fragment = new RewardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册广播接收器
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, new IntentFilter("MY_CUSTOM_ACTION"));
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reward, container, false);
        // 数据读取
        taskJoint = new TaskDataBank();
        rewardJoint = new RewardDataBank();
        taskList = taskJoint.loadTaskItems(requireActivity(), FILE_TASK_NAME);
        rewardList = rewardJoint.loadRewardItems(requireActivity(),FILE_REWARD_NAME);

        pointSumText = getPointSum(taskList,rewardList);
        sumPointView = rootView.findViewById(R.id.point_sum_text);
        sumPointView.setText("   "+pointSumText);
        // 设置RecyclerView和Adapter
        rewardRecyclerView = rootView.findViewById(R.id.reward_recyclerView);
        rewardRecyclerviewAdapter = new RewardRecyclerviewAdapter(requireActivity(),rewardList);
        rewardRecyclerView.setAdapter(rewardRecyclerviewAdapter);
        rewardRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        registerForContextMenu(rewardRecyclerView);

        addRewardLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        addHandleRewardResult(result.getData());
                    }
                    else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {
                    }
                });

        updateRewardLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        updateHandleRewardResult(result.getData());
                    } else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {
                    }
                }
        );
        ImageView myImageView = rootView.findViewById(R.id.addButton);      // 添加按钮响应函数
        myImageView.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), RewardDetailsActivity.class);
            addRewardLauncher.launch(intent);
        });
        return rootView;
    }
    private void addHandleRewardResult(Intent data) {
        String title = data.getStringExtra("title");
        int points = Integer.parseInt(data.getStringExtra("points"));
        String type = data.getStringExtra("type");

        RewardItem myReward = new RewardItem(title,points,0,type);
        addRewardToList(myReward);
        saveRewardList();
    }
    private void addRewardToList(RewardItem reward) {
        rewardList.add(reward);
        rewardRecyclerviewAdapter.notifyItemInserted(rewardList.size());
    }
    private void saveRewardList() {
        rewardJoint.saveRewardItems(requireActivity().getApplicationContext(), FILE_REWARD_NAME,rewardList);
    }
    private void updateHandleRewardResult(Intent data){
        int position = data.getIntExtra("position", 0);
        String title = data.getStringExtra("title");
        int points = Integer.parseInt(data.getStringExtra("points"));
        int numbers = Integer.parseInt(data.getStringExtra("time"));

        updateRewardInformation(position, title, points, numbers);
    }
    private void updateRewardInformation(int position, String title, int points, int numbers) {
        RewardItem reward = rewardList.get(position);
        reward.setRewardTitle(title);
        reward.setRewardPoint(points);
        reward.setRewardFinish(numbers);
        rewardRecyclerviewAdapter.notifyItemChanged(position);
        rewardJoint.saveRewardItems(requireActivity().getApplicationContext(), FILE_REWARD_NAME, rewardList);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getGroupId() != 3){
            return false;
        }
        switch (item.getItemId()) {
            case 0:
                Intent intentUpdate = new Intent(requireActivity(), RewardDetailsActivity.class);
                RewardItem selectReward = rewardList.get(item.getOrder());
                intentUpdate.putExtra("title",selectReward.getRewardTitle());
                intentUpdate.putExtra("points",String.valueOf(selectReward.getRewardPoint()));
                intentUpdate.putExtra("numbers",String.valueOf(selectReward.getRewardFinish()));
                intentUpdate.putExtra("position",item.getOrder());
                updateRewardLauncher.launch(intentUpdate);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("删除奖励");
                builder.setMessage("你确定删除这个奖励？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 刷新界面
                        rewardList.remove(item.getOrder());
                        rewardRecyclerviewAdapter.notifyItemRemoved(item.getOrder());
                        // 保存数据
                        rewardJoint.saveRewardItems(requireActivity().getApplicationContext(),FILE_REWARD_NAME,rewardList);
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
    private int getPointSum(ArrayList<TaskItem> taskList,ArrayList<RewardItem> rewardList){
        int sum = 0;
        for(TaskItem task:taskList){
            sum += task.getTaskPoint();
        }
        for(RewardItem reward:rewardList){
            sum -= reward.getRewardPoint() * reward.getRewardFinish();
        }
        return sum;
    }
    @Override
    public void onDestroy() {
        // 注销广播接收器
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
        super.onDestroy();
    }

}