package com.jnu.myfirstapplication3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private String []tabHeaderStrings = {"任务","奖励","我的"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycleview);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        FragmentAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabHeaderStrings[position])
        ).attach();
    }

    private class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 3;

        public FragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);

        }


        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TaskFragment();
                case 1:
                    return new ShoppingListFragment();
                case 2:
                    return new WodeFragment();
                default:
                    return null;
            }
        }


        public int getItemCount() {
            return NUM_TABS;
        }
    }
    /* private ArrayList<ShopItem> shopItems= new ArrayList<>();
    private ShopItemAdapter shopItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_main);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        shopItems = new DataBank().LoadShopItems(MainActivity.this);

        if(0 == shopItems.size()) {
            shopItems.add(new ShopItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
            shopItems.add(new ShopItem("创新工程实践", R.drawable.book_no_name));
            shopItems.add(new ShopItem("信息安全数学基础（第2版）", R.drawable.book_1));
        }

        String []itemNames = new String[]{"商品1","商品2","商品3"};
        shopItemAdapter = new ShopItemAdapter(shopItems);
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

                        new DataBank().SaveShopItems(this.getApplicationContext(),shopItems);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );

        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position = data.getIntExtra("position",0);
                        String name = data.getStringExtra("name");
                        ShopItem shopItem = shopItems.get(position);
                        shopItem.setName(name);
                        shopItemAdapter.notifyItemChanged(position);

                        new DataBank().SaveShopItems(MainActivity.this,shopItems);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );

    }

    ActivityResultLauncher<Intent>addItemLauncher;
    ActivityResultLauncher<Intent>updateItemLauncher;
    @Override
    public boolean onContextItemSelected(MenuItem item){

        switch (item.getItemId()){
            case 0:
                Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);

                addItemLauncher.launch(intent);
                break;

            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        shopItems.remove(item.getOrder());
                        shopItemAdapter.notifyItemRemoved(item.getOrder());

                        new DataBank().SaveShopItems(MainActivity.this,shopItems);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform any necessary actions for "No" button
                    }
                });
                builder.create().show();
                break;
            case 2:
                Intent intentUpdate = new Intent(MainActivity.this, BookDetailsActivity.class);
                ShopItem shopItem = shopItems.get(item.getOrder());
                intentUpdate.putExtra("name",shopItem.getName());
                intentUpdate.putExtra("position",item.getOrder());
                updateItemLauncher.launch(intentUpdate);
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
     */
}