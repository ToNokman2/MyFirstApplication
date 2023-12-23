package com.jnu.myfirstapplication3.interFace;

import android.content.Context;

import com.jnu.myfirstapplication3.data.RewardItem;

import java.util.ArrayList;

public interface RewardJoint {
    ArrayList<RewardItem> loadRewardItems(Context context, String fileName);
    void saveRewardItems(Context context, String fileName, ArrayList<RewardItem> rewardData);
}