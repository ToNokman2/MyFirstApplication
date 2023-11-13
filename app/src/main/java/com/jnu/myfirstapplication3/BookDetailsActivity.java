package com.jnu.myfirstapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookDetailsActivity extends AppCompatActivity {

    private int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);
        Intent intent = getIntent();
        if (intent != null){
            String name = intent.getStringExtra("name");
            position = intent.getIntExtra("position",-1);
            if (null != name) {
                EditText editTextItemName = findViewById(R.id.editText_item_name);
                editTextItemName.setText(name);
            }
        }
        Button buttonOK = findViewById(R.id.button_item_details_ok);
        Button buttonNO = findViewById(R.id.button_item_details_no);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editTextItemName = findViewById(R.id.editText_item_name);
                intent.putExtra("name",editTextItemName.getText().toString());
                intent.putExtra("position",position);
                intent.putExtra("bookCover", "book_no_name.png"); // 添加默认的书籍封面
                setResult(Activity.RESULT_OK,intent);
                BookDetailsActivity.this.finish();
            }
        });
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                BookDetailsActivity.this.finish();
            }
        });
    }
}