package com.jnu.myfirstapplication3.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    private ArrayList<ShopItem> data;
    final String DATA_FILENAME = "shop items.data";
    public ArrayList<ShopItem> LoadShopItems(Context context){
        ArrayList<ShopItem>data = new ArrayList<>();
        try {
            FileInputStream fileIn = context.openFileInput(DATA_FILENAME);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            data = (ArrayList<ShopItem>)objectIn.readObject();
            objectIn.close();
            fileIn.close();

            Log.d("Serialization" , "数据保存成功.累计：:"+data.size());
    } catch (ClassNotFoundException | IOException e) {
        e.printStackTrace();
        Log.d("TAG", "数据保存失败");
        }
        return data;
    }

    public void SaveShopItems(Context context, ArrayList<ShopItem> shopItems) {
        try {
            FileOutputStream fileOut = context.openFileOutput(DATA_FILENAME,Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(shopItems);
            out.close();
            fileOut.close();

            Log.d("Serialization","Data is serialized and saved.");
        }catch (IOException e){
            e.printStackTrace();

        }
    }
}
