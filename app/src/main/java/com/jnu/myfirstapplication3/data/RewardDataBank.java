package com.jnu.myfirstapplication3.data;

import android.content.Context;

import com.jnu.myfirstapplication3.interFace.RewardJoint;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RewardDataBank implements RewardJoint {

    @Override
    public ArrayList<RewardItem> loadRewardItems(Context context, String fileName) {
        ArrayList<RewardItem> rewardData = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            rewardData = (ArrayList<RewardItem>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rewardData;
    }

    @Override
    public void saveRewardItems(Context context, String fileName, ArrayList<RewardItem> rewardData) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(rewardData);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}