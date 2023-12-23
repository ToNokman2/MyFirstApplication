package com.jnu.myfirstapplication3.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.jnu.myfirstapplication3.data.RewardDataBank;
import com.jnu.myfirstapplication3.data.RewardItem;
import com.jnu.myfirstapplication3.interFace.RewardJoint;

import java.util.ArrayList;

public class RewardRecyclerviewAdapter extends RecyclerView.Adapter<RewardRecyclerviewAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<RewardItem> rewardList;
    private RewardJoint rewardJoint;
    private final String FILE_REWARD_NAME = "rewardData.ser";
    public RewardRecyclerviewAdapter(Context context,ArrayList<RewardItem> rewardList) {
        this.context = context;
        this.rewardList = rewardList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.reward_item_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RewardItem rewardItem = rewardList.get(position);
        holder.rewardTitle.setText(rewardItem.getRewardTitle());

        boolean Single = false;
        if (rewardItem.getRewardType().equals("单次")) {
            holder.rewardFinish.setText(rewardItem.getRewardFinish() + "/1");
            Single = true;
        } else {
            holder.rewardFinish.setText(rewardItem.getRewardFinish() + "/∞");
            Single = false;
        }

        holder.rewardPoint.setText("-" + rewardItem.getRewardPoint());
        boolean finalSingle = Single;

        holder.finishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("兑换奖励");
                builder.setMessage("你确定花费" + rewardItem.getRewardPoint() + "?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rewardJoint = new RewardDataBank();
                        ArrayList<RewardItem> rewardAllList = rewardJoint.loadRewardItems(context.getApplicationContext(), FILE_REWARD_NAME);
                        for(int i = 0; i < rewardAllList.size(); i++){
                            RewardItem item = rewardAllList.get(i);
                            if (item.getRewardTitle().equals(rewardItem.getRewardTitle())) {
                                item.setRewardFinish(item.getRewardFinish() + 1);
                                if (item.getRewardType().equals("单次")) {
                                    rewardAllList.remove(i);
                                    notifyItemRemoved(position);
                                }
                                break;
                            }
                        }
                        rewardJoint.saveRewardItems(context, FILE_REWARD_NAME, rewardAllList);
                        Intent intent = new Intent("MY_CUSTOM_ACTION");
                        intent.putExtra("data", "invalidate");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        rewardItem.setRewardFinish(rewardItem.getRewardFinish()+1);
                        if(rewardItem.getRewardType().equals("单次")){
                            rewardList.remove(position);
                            notifyItemRemoved(position);
                        }
                        else{
                            rewardItem.setRewardFinish(rewardItem.getRewardFinish() + 1);
                            holder.rewardFinish.setText(rewardItem.getRewardFinish() + "/∞");
                        }
                    }
                });
                // 如果按下否定，什么都不做
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView rewardTitle;
        TextView rewardFinish;
        TextView rewardPoint;
        ImageView finishImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            finishImage = itemView.findViewById(R.id.reward_item_img);
            rewardTitle = itemView.findViewById(R.id.reward_item_title);
            rewardFinish = itemView.findViewById(R.id.reward_num_finish);
            rewardPoint = itemView.findViewById(R.id.reward_num_point);
            // 设置监听者为自己
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(3,0,this.getAdapterPosition(),"修改");
            menu.add(3,1,this.getAdapterPosition(),"删除");
        }
    }
}