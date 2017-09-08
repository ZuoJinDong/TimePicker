package com.zjd.timepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private TextView tv_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_time= (TextView) findViewById(R.id.tv_time);
        timePicker= (TimePicker) findViewById(R.id.timePicker);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_time:
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tv_time.setText("选择时间"+"\n"+timePicker.getDate()+"\n"+timePicker.getDateStr()+"\n"+"当前时间"+"\n"+new Date().getTime()+"\n"+format.format(new Date()));
                break;
        }
    }
}
