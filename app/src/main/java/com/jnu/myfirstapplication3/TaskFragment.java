package com.jnu.myfirstapplication3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.myfirstapplication3.data.RewardDataBank;
import com.jnu.myfirstapplication3.data.RewardItem;
import com.jnu.myfirstapplication3.data.TaskDataBank;
import com.jnu.myfirstapplication3.interFace.RewardJoint;
import com.jnu.myfirstapplication3.interFace.TaskJoint;
import com.jnu.myfirstapplication3.data.TaskItem;

import java.util.ArrayList;

public class TaskFragment extends Fragment{
    private final static  String[] taskType = {"每日任务", "每周任务"};
    private final String FILE_TASK_NAME = "taskData.ser";
    private final String FILE_REWARD_NAME = "rewardData.ser";
    private int pointSumText;
    private ActivityResultLauncher<Intent> addTaskLauncher;

    private ArrayList<TaskItem> taskList;
    private ArrayList<RewardItem> rewardList;
    private TaskJoint taskJoint;
    private RewardJoint rewardJoint;
    private TextView sumPointView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("MY_CUSTOM_ACTION")) {
                taskList = taskJoint.loadTaskItems(requireActivity(), FILE_TASK_NAME);
                rewardList = rewardJoint.loadRewardItems(requireActivity(),FILE_REWARD_NAME);
                pointSumText = getPointSum(taskList, rewardList);
                sumPointView.setText("   "+pointSumText);
            }
        }
    };
    public TaskFragment() {
        // Required empty public constructor
    }
    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, new IntentFilter("MY_CUSTOM_ACTION"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        taskJoint = new TaskDataBank();
        rewardJoint = new RewardDataBank();
        taskList = taskJoint.loadTaskItems(requireActivity(), FILE_TASK_NAME);
        rewardList = rewardJoint.loadRewardItems(requireActivity(),FILE_REWARD_NAME);

        pointSumText = getPointSum(taskList,rewardList);
        sumPointView = rootView.findViewById(R.id.point_sum_text);
        sumPointView.setText("   "+pointSumText);

        // Fragment + ViewPager2 + TableLayout
        ViewPager2 viewPager = rootView.findViewById(R.id.view_pager);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);

        PagerAdapter pagerAdapter = new PagerAdapter(this);        // 创建 FragmentPagerAdapter
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(taskType[position])).attach();       // 将 TabLayout 和 ViewPager 关联


        addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        addHandleTaskResult(result.getData());
                    }
                    else if (result.getResultCode() == requireActivity().RESULT_CANCELED) {
                    }
                });

        // 添加按钮响应函数
        ImageView myImageView = rootView.findViewById(R.id.addButton);
        myImageView.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), TaskDetailsActivity.class);
            addTaskLauncher.launch(intent);
        });

        return rootView;
    }
    private void addHandleTaskResult(Intent data) {
        String title = data.getStringExtra("title");
        int points = Integer.parseInt(data.getStringExtra("points"));
        int numbers = Integer.parseInt(data.getStringExtra("time"));
        String type = data.getStringExtra("type");

        TaskItem myTask = new TaskItem(title, points, numbers, 0, type);

        addTaskToList(myTask);
        saveTaskList();
    }

    private void addTaskToList(TaskItem task) {
        taskList.add(task);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof PhaseTaskFragment) {
                PhaseTaskFragment phaseTaskFragment = (PhaseTaskFragment) fragment;
                if (phaseTaskFragment.isMatchingTaskType(task.getTaskType())) {
                    phaseTaskFragment.addTaskAndNotifyAdapter(task);
                }
            }
        }
    }
    private void saveTaskList() {
        taskJoint.saveTaskItems(requireActivity().getApplicationContext(), FILE_TASK_NAME, taskList);
    }

    private static class PagerAdapter extends FragmentStateAdapter {

        private static final int NUM_TABS = 2;

        public PagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new PhaseTaskFragment(taskType[0],0);
                case 1:
                    return new PhaseTaskFragment(taskType[1],1);
                default:
                    throw new IllegalArgumentException("Invalid position");
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
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
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
        super.onDestroy();
    }

}