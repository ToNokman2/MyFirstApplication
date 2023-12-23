package com.jnu.myfirstapplication3.data;

import java.io.Serializable;

public class RewardItem implements Serializable {
    private String rewardTitle;
    private int rewardPoint;
    private int rewardFinish;
    private String rewardType;

    public RewardItem() {
    }

    public RewardItem(String rewardTitle, int rewardPoint, int rewardFinish, String rewardType) {
        this.rewardTitle = rewardTitle;
        this.rewardPoint = rewardPoint;
        this.rewardFinish = rewardFinish;
        this.rewardType = rewardType;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        this.rewardTitle = rewardTitle;
    }

    public int getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(int rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public int getRewardFinish() {
        return rewardFinish;
    }

    public void setRewardFinish(int rewardFinish) {
        this.rewardFinish = rewardFinish;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

}