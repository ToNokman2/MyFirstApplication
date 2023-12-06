package com.jnu.myfirstapplication3;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.myfirstapplication3.data.TaskDataBank;
import com.jnu.myfirstapplication3.interFace.TaskJoint;
import com.jnu.myfirstapplication3.data.TaskItem;

import java.util.ArrayList;

public class TaskFragment extends Fragment{
    private final static  String[] taskType = {"每日任务", "每周任务"};
    private final String FILE_NAME = "taskData.ser";
    private ActivityResultLauncher<Intent> addTaskLauncher;
    private ArrayList<TaskItem> taskList;
    private TaskJoint taskjoint;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        taskjoint = new TaskDataBank();
        taskList = taskjoint.loadTaskItems(requireActivity(),FILE_NAME);

        // Fragment + ViewPager2 + TableLayout
        ViewPager2 viewPager = rootView.findViewById(R.id.view_pager);
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);

        PagerAdapter pagerAdapter = new PagerAdapter(this);        // 创建 FragmentPagerAdapter
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Tab " + (position + 1))
        ).attach();
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(taskType[position])).attach();       // 将 TabLayout 和 ViewPager 关联

        // 添加任务
        addTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        Intent intent = result.getData();
                        String title = intent.getStringExtra("title");
                        int points = Integer.parseInt(intent.getStringExtra("points"));
                        int numbers = Integer.parseInt(intent.getStringExtra("numbers"));
                        String type = intent.getStringExtra("type");

                        TaskItem myTask = new TaskItem(title,points,numbers,0,type);        // 格式化

                        taskList.add(myTask);
                        System.out.println(taskList);
                        taskjoint.saveTaskItems(requireActivity().getApplicationContext(),FILE_NAME,taskList);      // 保存数据

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
                    return new TypeTaskFragment(taskType[0],0);
                case 1:
                    return new TypeTaskFragment(taskType[1],1);
                default:
                    throw new IllegalArgumentException("Invalid position");
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }

}