package com.example.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.Calendar;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by ywq on 2016/5/31.
 */
public class EditActivity extends Activity implements View.OnClickListener {

    private EditText editText;
    private TextView tv;
    private Switch aSwitch;
    private View layout;
    private TextView tv1;
    private TextView tv2;
    int hourOfDay2;
    int minute2;

    String notes;
    String times;
    String id;
    Calendar currentTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_note1);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title2);
        editText = (EditText) findViewById(R.id.shijian1);
        tv = (TextView) findViewById(R.id.xianshi1);
        aSwitch = (Switch) findViewById(R.id.switch2);
        tv1 = (TextView) findViewById(R.id.quxiao1);
        tv2 = (TextView) findViewById(R.id.wancheng1);

        layout = findViewById(R.id.time1);


        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        layout.setOnClickListener(this);


        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.beijing7);//通知栏所需颜色


        notes = getIntent().getExtras().getString("note");
        times = getIntent().getExtras().getString("time");
        id = getIntent().getExtras().getString("objectId");

        editText.setText(notes);
        tv.setText(times);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    layout.setVisibility(View.VISIBLE);

                } else {
                    layout.setVisibility(View.INVISIBLE);

                    tv.setText("");


                }

            }
        });


    }

    private void setTranslucentStatus() {


        Window window = this.getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quxiao1:
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;



            case R.id.wancheng1:


                // 指定启动AlarmActivity组件
//                Intent intent1 = new Intent(EditActivity.this
//                        , NoteActivity.class);
//
//
//
//                intent1.putExtra("hourOfDay", hourOfDay2);
//                intent1.putExtra("minute", minute2);
                App.hour2 = hourOfDay2;
                App.minute2 = minute2;
                final String note = editText.getText().toString();
                final String xianshi = tv.getText().toString();
                if (!TextUtils.isEmpty(note)) {
                    final AVObject avObject = new AVObject("NoteList");
                    avObject.setObjectId(id);
                    avObject.put("note", notes);
                    avObject.put("time", times);
                    avObject.put("markId", AVUser.getCurrentUser().getObjectId());
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                avObject.put("note", note);
                                avObject.put("time", xianshi);
                                avObject.saveInBackground();
                                Toast.makeText(EditActivity.this, "编辑成功", Toast.LENGTH_LONG).show();
                                android.os.Handler handler = new android.os.Handler();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent1 = new Intent(EditActivity.this, MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                };
                                handler.postDelayed(runnable, 2000);

                            } else {
                                Toast.makeText(EditActivity.this, "编辑失败 请检查你的网络", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(EditActivity.this, "请将内容输入完整", Toast.LENGTH_LONG).show();

                }


                break;


            case R.id.time1:

                final AlertDialog dialog = new AlertDialog.Builder(EditActivity.this)
                        .create();
                dialog.show();
                DatePicker picker = new DatePicker(EditActivity.this);
                picker.setDate(2016, 6);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(final String date) {
                        dialog.dismiss();
//                        final TimePickerDialog timePickerDialog =
                        Calendar CurrentTime = Calendar.getInstance();

                        new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tv.setText(
                                        date + "\t" +
                                                (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) +
                                                ":" +
                                                (minute < 10 ? "0" + minute :
                                                        minute));

                                hourOfDay2 = hourOfDay;
                                minute2 = minute;


                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY)
                                , currentTime.get(Calendar.MINUTE), false)
                                .show();
                    }
                });
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setContentView(picker, params);
                dialog.getWindow().setGravity(Gravity.CENTER);


                break;



            default:
                break;
        }
    }
}
