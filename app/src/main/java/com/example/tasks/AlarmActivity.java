package com.example.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by ywq on 2016/6/2.
 */
public class AlarmActivity extends Activity {


    MediaPlayer alarmMusic;
    String note;
    String id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixing);


         id = getIntent().getExtras().getString("id");


//        Toast.makeText(this,id,Toast.LENGTH_LONG).show();


        final AVQuery<AVObject> query = new AVQuery<>("NoteList");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                for (AVObject avobect : list) {
                    note = avobect.getString("note");
                }

//                note = list.get(0).getString("note");


                // 加载指定音乐，并为之创建MediaPlayer对象
                alarmMusic = MediaPlayer.create(AlarmActivity.this, R.raw.ls3);
                alarmMusic.setLooping(true);
                // 播放音乐
                alarmMusic.start();
                // 创建一个对话框

                new AlertDialog.Builder(AlarmActivity.this)
                        .setTitle("提醒")
                        .setMessage(note)

                        .setPositiveButton(
                                "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 停止音乐
                                        alarmMusic.stop();
                                        // 结束该Activity
                                        finish();
                                    }
                                }
                        )
                        .show();

            }
        });

    }

}
