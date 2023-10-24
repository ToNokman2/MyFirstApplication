package com.jnu.myfirstapplication3;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.myfirstapplication3.data.ShopItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_main);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ShopItem> shopItems= new ArrayList<>();
        shopItems.add(new ShopItem("青椒",1.5,R.drawable.qingjiao));
        shopItems.add(new ShopItem("萝卜",2.5, R.drawable.luobo));
        shopItems.add(new ShopItem("白菜",3.5, R.drawable.baicai));

        String []itemNames = new String[]{"商品1","商品2","商品3"};
        ShopItemAdapter shopItemAdapter = new ShopItemAdapter(shopItems);
        mainRecyclerView.setAdapter(shopItemAdapter);

        registerForContextMenu(mainRecyclerView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item
                .getMenuInfo();
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case 0:
                break;

            case 1:
                break;

            case 2:
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
    public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder>{
        private ArrayList<ShopItem> shopItemArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
            private final TextView textViewName;
            private final TextView textViewPrice;
            private final ImageView imageViewItem;

            @Override
            public void onCreateContextMenu(ContextMenu menu,View v,
                                            ContextMenu.ContextMenuInfo menuInfo){
                menu.setHeaderTitle("具体操作");
                menu.add(0,0, Menu.NONE,"添加");
                menu.add(0,1, Menu.NONE,"删除");
                menu.add(0,2, Menu.NONE,"修改");
            }


            public ViewHolder(View ShopitemView){
                super(ShopitemView);

                textViewName = ShopitemView.findViewById(R.id.textview_item_name);
                textViewPrice = ShopitemView.findViewById(R.id.textview_item_price);
                imageViewItem = ShopitemView.findViewById(R.id.imageview_item);
                ShopitemView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName(){
                return textViewName;
            }
            public TextView getTextViewPrice(){
                return textViewPrice;
            }
            public ImageView getImageViewItem(){
                return imageViewItem;
            }
        }
        public ShopItemAdapter(ArrayList<ShopItem> shopItems){
            shopItemArrayList = shopItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.shop_item_row,viewGroup,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder,final int position){
            viewHolder.getTextViewName().setText(shopItemArrayList.get(position).getName());
            viewHolder.getTextViewPrice().setText(shopItemArrayList.get(position).getPrice()+"");
            viewHolder.getImageViewItem().setImageResource(shopItemArrayList.get(position).getImageId());
        }

        @Override
        public int getItemCount(){
            return shopItemArrayList.size();
        }
    }
}