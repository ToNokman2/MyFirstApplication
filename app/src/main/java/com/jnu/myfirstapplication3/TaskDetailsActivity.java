package com.jnu.myfirstapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class TaskDetailsActivity extends AppCompatActivity {
    private String selectedText;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task_details_activity);
        EditText editTitle = findViewById(R.id.editTaskName);
        EditText editPoint = findViewById(R.id.editTaskPoints);
        EditText editTime  = findViewById(R.id.editTaskTime);
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("title") != null) {
                TextView textView = findViewById(R.id.titleText);
                textView.setText("修改任务");

            }
            editTitle.setText(intent.getStringExtra("title"));
            editPoint.setText(intent.getStringExtra("points"));
            editTime.setText(intent.getStringExtra("time"));
            position = intent.getIntExtra("position",-1);
        }

        ImageView goBackImage = findViewById(R.id.gobackButton);
        goBackImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TaskDetailsActivity.this.finish();
            }
        });

        // 设置Type响应函数
        Spinner editType  = findViewById(R.id.spinnerTaskType);
        editType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedText = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedText = "每日任务";
            }
        });



        Button buttonOk = findViewById(R.id.btnSubmit);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED,intent);

                if(editTitle.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"请您输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editPoint.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请您输入任务点数", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra("title",editTitle.getText().toString());
                intent.putExtra("points",editPoint.getText().toString());
                intent.putExtra("type",selectedText);
                if(editTime.getText().toString().equals("")){
                    intent.putExtra("time","10");
                }
                else{
                    intent.putExtra("time",editTime.getText().toString());
                }
                setResult(Activity.RESULT_OK,intent);
                TaskDetailsActivity.this.finish();
            }
        });
    }
}