package com.jnu.myfirstapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RewardDetailsActivity extends AppCompatActivity {
    private String selectedText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_details);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editPoint = findViewById(R.id.editPoint);

        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("title") != null) {
                TextView textView = findViewById(R.id.titleText);
                textView.setText("修改任务");
            }
            editTitle.setText(intent.getStringExtra("title"));
            editPoint.setText(intent.getStringExtra("points"));
            position = intent.getIntExtra("position",-1);
        }
        // 对返回按钮的设置
        ImageView goBackImage = findViewById(R.id.gobackButton);
        goBackImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 点击返回时，直接返回
                RewardDetailsActivity.this.finish();
            }
        });
        // 设置下拉框的选择响应函数
        Spinner editType  = findViewById(R.id.spinnerRewardType);
        editType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedText = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedText = "单次";
            }
        });

        // 对确定按钮的处理
        Button buttonOk = findViewById(R.id.btnSubmit);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED,intent);
                if(editTitle.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editPoint.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请输入点数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(editPoint.getText().toString()) > 999){
                    Toast.makeText(getApplicationContext(),"最大为999", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra("title",editTitle.getText().toString());
                intent.putExtra("points",editPoint.getText().toString());
                intent.putExtra("type",selectedText);


                intent.putExtra("position",position);
                setResult(Activity.RESULT_OK,intent);
                RewardDetailsActivity.this.finish();
            }
        });
    }
}