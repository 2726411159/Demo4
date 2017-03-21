package com.example.tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AliasActivity;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.okhttp.internal.framed.FrameReader;

import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

public class NoteActivity extends Activity implements OnClickListener {
    private TextView tv;
    private LinearLayout ll;
    private TextView tv1;
    private TextView tv2;
    private Switch aSwitch;
    private EditText editText;
    AlarmManager aManager;
    int hourOfDay1;
    int minute1;
    Calendar currentTime = Calendar.getInstance();

    int hourOfDay3;
    int minute3;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



//        hourOfDay2 = getIntent().getExtras().getString("hourOfDay");
//        minute2 = getIntent().getExtras().getString("minute");





        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_note);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title1);
        hourOfDay3=App.hour2;
        minute3=App.minute2;
        tv = (TextView) findViewById(R.id.quxiao);
        ll = (LinearLayout) findViewById(R.id.time);

        tv1 = (TextView) findViewById(R.id.xianshi);
        tv2 = (TextView) findViewById(R.id.wancheng);
        aSwitch = (Switch) findViewById(R.id.switch1);
        editText = (EditText) findViewById(R.id.shijian);

        ll.setOnClickListener(this);
        tv.setOnClickListener(this);

        tv2.setOnClickListener(this);
        ll.setVisibility(View.INVISIBLE);

        int color = Color.WHITE;

        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.beijing7);//通知栏所需颜色
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll.setVisibility(View.VISIBLE);
                    // aSwitch.setBackground(getResources().getDrawable(R.drawable.checked));
                } else {
                    ll.setVisibility(View.INVISIBLE);
                    // aSwitch.setBackground(getResources().getDrawable(R.drawable.unchecked));
                    tv1.setText("");
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
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.quxiao:
                final Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.wancheng:
                final String note = editText.getText().toString();
                String xianshi = tv1.getText().toString();
                if (!TextUtils.isEmpty(note)) {
                    final AVObject avObject = new AVObject("NoteList");
                    avObject.put("note", note);
                    avObject.put("time", xianshi);
                    avObject.put("markId", AVUser.getCurrentUser().getObjectId());
                    avObject.saveInBackground(new SaveCallback() {


                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                 id=avObject.getObjectId();
                                if(hourOfDay1!=0&minute1!=0 ) {
                                    // 指定启动AlarmActivity组件
                                    Intent intent = new Intent(NoteActivity.this
                                            , AlarmActivity.class);
                                    //intent.putExtra("note", note);
                                    intent.putExtra("id", id);
                                    // 创建PendingIntent对象
                                    PendingIntent pi = PendingIntent.getActivity(
                                            NoteActivity.this, 0, intent, 0);
                                    Calendar c = Calendar.getInstance();
                                    c.setTimeInMillis(System.currentTimeMillis());
                                    // 根据用户选择时间来设置Calendar对象
                                    c.set(Calendar.HOUR, hourOfDay1);
                                    c.set(Calendar.MINUTE, minute1);
                                    // 设置AlarmManager将在Calendar对应的时间启动指定组件
                                    AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    aManager.set(AlarmManager.RTC_WAKEUP
                                            , c.getTimeInMillis(), pi);
                                    // 显示闹铃设置成功的提示信息

                                }
                                Toast.makeText(NoteActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                                android.os.Handler handler = new android.os.Handler();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent1 = new Intent(NoteActivity.this, MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                };
                                handler.postDelayed(runnable, 2000);
                            } else {
                                Toast.makeText(NoteActivity.this, "添加失败 请检查你的网络", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(NoteActivity.this, "请将内容输入完整", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.time:

                final AlertDialog dialog = new AlertDialog.Builder(NoteActivity.this)
                        .create();
                dialog.show();
                DatePicker picker = new DatePicker(NoteActivity.this);
                picker.setDate(2016, 6);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {


                    @Override
                    public void onDatePicked(final String date) {
                        dialog.dismiss();
                        Calendar CurrentTime = Calendar.getInstance();
                        new TimePickerDialog(NoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tv1.setText(
                                        date + "\t" +
                                                (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) +
                                                ":" +
                                                (minute < 10 ? "0" + minute :
                                                        minute));
                                hourOfDay1=hourOfDay;
                                minute1=minute;
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
