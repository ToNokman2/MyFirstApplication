package com.jnu.myfirstapplication3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.myfirstapplication3.data.BookItem;
import com.jnu.myfirstapplication3.data.DataBank;

import java.util.ArrayList;

public class ShoppingListFragment extends Fragment {
    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        RecyclerView mainRecyclerview = rootview.findViewById(R.id.recycler_view);// 创建布局管理器
        mainRecyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));


        bookItems = new ArrayList<>();

        bookItems = new DataBank().LoadBookItems((MainActivity) requireActivity());//静态
        if (0 == bookItems.size()) {
            bookItems.add(new BookItem("软件项目管理案例教程（第4版）",R.drawable.book_2));
        }
        bookItemAdapter =new BookItemAdapter(bookItems);
        mainRecyclerview.setAdapter(bookItemAdapter);

        registerForContextMenu(mainRecyclerview);

        addItemLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        String name = data.getStringExtra("name");
                        bookItems.add(new BookItem(name,R.drawable.book_no_name));
                        bookItemAdapter.notifyItemInserted(bookItems.size());


                        new DataBank().SaveBookItems(requireActivity(),bookItems);

                        //获取返回的数据//在这塑可以根据需要进行进一步处理
                    }
                }
        );
        updateItemLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position = data.getIntExtra("position",0);
                        String name = data.getStringExtra("name");
                        BookItem bookItem = bookItems.get(position);
                        bookItem.setName(name);
                        bookItemAdapter.notifyItemChanged(position);

                        new DataBank().SaveBookItems(requireActivity(),bookItems);

                        //获取返回的数据//在这塑可以根据需要进行进一步处理
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                }
        );
        return rootview;
    }
    //将 shopItems 和 shopItemAdapter 定义为类的成员变量
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    private BookItemAdapter bookItemAdapter;
    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;
    private static final int MENU_ITEM_ADD = 0;
    private static final int MENU_ITEM_DELETE = 1;
    private static final int MENU_ITEM_UPDATE = 2;
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getOrder();  // 获取被点击的项的位置
        switch (item.getItemId()) {
            case MENU_ITEM_ADD:

                Intent intent = new Intent(requireActivity(),BookDetailsActivity.class);
                addItemLauncher.launch(intent);
                break;

            case MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton( "确定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        bookItems.remove(item.getOrder());
                        bookItemAdapter.notifyItemRemoved(item.getOrder());


                        new DataBank().SaveBookItems(requireActivity(),bookItems);
                    }

                });
                builder.setNegativeButton( "取消",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.create().show();
                break;
            case MENU_ITEM_UPDATE:
                Intent intentUpdate = new Intent(requireActivity(),BookDetailsActivity.class);
                BookItem bookItem = bookItems.get(item.getOrder());
                intentUpdate.putExtra("name",bookItem.getName());
                intentUpdate.putExtra("position",item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.ViewHolder> {

        private ArrayList<BookItem> bookItemArrayList;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;

            private final ImageView ImageViewNameItem;
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add( 0,0, this.getAdapterPosition(),"添加"+this.getAdapterPosition());
                menu.add(0,1, this.getAdapterPosition(),"删除"+this.getAdapterPosition());
                menu.add(0,2,this.getAdapterPosition(),"修改"+this.getAdapterPosition());
            }
            public ViewHolder(View bookitemView) {
                super(bookitemView);
                // Define click listener for the ViewHolder's View

                textViewName = bookitemView.findViewById(R.id.text_view_book_title);
                ImageViewNameItem = bookitemView.findViewById(R.id.bookView_item1);
                bookitemView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName() {
                return textViewName;
            }
            public ImageView getImageViewNameItem() {
                return ImageViewNameItem;
            }


        }


        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */
        public BookItemAdapter(ArrayList<BookItem> dataSet) {
            bookItemArrayList = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.shop_item_row, viewGroup, false);

            return new ViewHolder(view);
        }



        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextViewName().setText(bookItemArrayList.get(position).getName());
            viewHolder.getImageViewNameItem().setImageResource(bookItemArrayList.get(position).getImageId());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return bookItemArrayList.size();
        }
    }
}