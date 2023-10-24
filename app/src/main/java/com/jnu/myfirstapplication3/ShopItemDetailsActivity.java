package com.jnu.myfirstapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShopItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Button buttonOK = findViewById(R.id.button_item_details_ok);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editTextItemName = findViewById(R.id.editText_item_name);
                EditText editTextItemPrice = findViewById(R.id.editText_item_price);
                intent.putExtra("name",editTextItemName.getText().toString());
                intent.putExtra("price",editTextItemPrice.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                ShopItemDetailsActivity.this.finish();
            }
        });
    }
}