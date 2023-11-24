package com.jnu.myfirstapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookDetailsActivity extends AppCompatActivity {
    private int position=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Intent intent = getIntent();
        if(intent != null){
            String name = intent.getStringExtra("name");

            if(null != name ){
                position = intent.getIntExtra("position",-1);
                EditText editTextItemName= findViewById(R.id.edittext_item_name);
                editTextItemName.setText(name);
            }
        }

        Button buttonOk= findViewById(R.id.button_item_details_ok);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editTextItemName= findViewById(R.id.edittext_item_name);

                intent.putExtra( "name" , editTextItemName.getText().toString());
                intent.putExtra( "position" , position);
                setResult(Activity.RESULT_OK, intent) ;

                BookDetailsActivity.this.finish();
            }
        });
    }
}