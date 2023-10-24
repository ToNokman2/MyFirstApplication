package com.jnu.myfirstapplication3;

import android.app.Activity;
import android.content.Intent;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
        shopItems.add(new ShopItem("软件项目管理案例教程（第4版）",R.drawable.book_2));
        shopItems.add(new ShopItem("创新工程实践",R.drawable.book_no_name));
        shopItems.add(new ShopItem("信息安全数学基础（第2版）",R.drawable.book_1));

        String []itemNames = new String[]{"商品1","商品2","商品3"};
        ShopItemAdapter shopItemAdapter = new ShopItemAdapter(shopItems);
        mainRecyclerView.setAdapter(shopItemAdapter);

        registerForContextMenu(mainRecyclerView);

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");

                        shopItems.add(new ShopItem(name, R.drawable.book_2));
                        shopItemAdapter.notifyItemInserted(shopItems.size());

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );
    }

    ActivityResultLauncher<Intent>addItemLauncher;

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item
                .getMenuInfo();
        switch (item.getItemId()){
            case 0:
                Intent intent = new Intent(MainActivity.this,ShopItemDetailsActivity.class);
                addItemLauncher.launch(intent);
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

            private final ImageView imageViewItem;

            @Override
            public void onCreateContextMenu(ContextMenu menu,View v,
                                            ContextMenu.ContextMenuInfo menuInfo){
                menu.setHeaderTitle("具体操作");
                menu.add(0,0, this.getAdapterPosition(),"添加"+this.getAdapterPosition());
                menu.add(0,1, this.getAdapterPosition(),"删除"+this.getAdapterPosition());
                menu.add(0,2, this.getAdapterPosition(),"修改"+this.getAdapterPosition());
            }


            public ViewHolder(View ShopitemView){
                super(ShopitemView);

                textViewName = ShopitemView.findViewById(R.id.textview_item_name);
                imageViewItem = ShopitemView.findViewById(R.id.imageview_item);
                ShopitemView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName(){
                return textViewName;
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
            viewHolder.getImageViewItem().setImageResource(shopItemArrayList.get(position).getImageId());
        }

        @Override
        public int getItemCount(){
            return shopItemArrayList.size();
        }
    }
}