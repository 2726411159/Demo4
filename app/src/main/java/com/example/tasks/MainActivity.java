package com.example.tasks;

import android.R.array;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class MainActivity extends Activity implements OnItemClickListener {
    private ListView listView;
    private ListView listView1;
    private ImageView img;
    private DrawerLayout mdDrawerLayout;
    private int a = 0;
    String a1;
    // 声明适配器
    private ArrayAdapter<String> arrayAdapter;

    String b = "";

    private List<String> notes;
    private List<String> times;
    private List<String> ids;
    private SearchView searchView;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);


        listView = (ListView) findViewById(R.id.left_drawer);
        listView1 = (ListView) findViewById(R.id.drawer);
        notes = new ArrayList<>();
        times = new ArrayList<>();
        ids = new ArrayList<>();
        img = (ImageView) findViewById(R.id.menu);
        searchView= (SearchView) findViewById(R.id.searchView1);

        mdDrawerLayout = (DrawerLayout) findViewById(R.id.dl);




        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.beijing6);//通知栏所需颜色

        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (a == 0) {
                    mdDrawerLayout.openDrawer(listView);


                    a = 1;
                } else {
                    mdDrawerLayout.closeDrawer(listView);
                    a = 0;
                }
            }
        });

        // 第一步 ：新建一个适配器 ArrayAdapter（上下文，当前listview加载的每一个列表项所对应的布局文件,数据源）
        // 第二步：适配器添加数据源
        final String[] array = {"新建事项", "个人中心", "退出登录"};
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, array);
        // 第三步：视图加载适配器
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);


        AVQuery<AVObject> query = new AVQuery<>("NoteList");
        query.whereEqualTo("markId", AVUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list.size() > 0) {


                    for (AVObject avObject : list) {

                        notes.add(avObject.getString("note"));
                        times.add(avObject.getString("time"));
                        ids.add(avObject.getObjectId());


                    }

                    listView1.setAdapter(new MyAdapter());


                }
            }
        });







//        AVQuery<AVObject> query1 = new AVQuery<>("NoteList");
//        query1.whereEqualTo("note",searchView.getTextAlignment());
//        query.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (list.size() > 0) {
//
//
//                    for (AVObject avObject : list) {
//
//                        notes.add(avObject.getString("note"));
//                        notes.add(avObject.getString("note"));
//                        times.add(avObject.getString("time"));
//                        ids.add(avObject.getObjectId());
//
//
//
//                    }
//
//                    listView1.setAdapter(new MyAdapter());
//
//
//                }
//            }
//        });



        listView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("note", notes.get(position));
                intent.putExtra("time", times.get(position));
                intent.putExtra("objectId", ids.get(position));
                startActivity(intent);
                finish();


            }
        });


        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("是否删除？");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AVObject object = new AVObject("NoteList");
                            object.setObjectId( ids.get(position));
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e==null){
                                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }else {
                                        Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.show();
                return true;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub

        switch (position) {
            case 0:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case 1:
                Intent intent1 = new Intent(this, PersonalActivity.class);
                startActivity(intent1);
                finish();

                break;
            case 2:


//                AVUser currentUser = AVUser.getCurrentUser();
//                currentUser = null;
                AVUser.logOut();// 清除缓存用户对象
                AVUser currentUser = AVUser.getCurrentUser();// 现在的 currentUser 是 null 了


                Toast.makeText(MainActivity.this, "正在退出", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    public void run() {
                        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                };
                handler.postDelayed(runnable, 2000);

                break;

            default:
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return times.size();
        }

        @Override
        public Object getItem(int position) {
            return times.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.list_item1, null);
                TextView tv_note = (TextView) convertView.findViewById(R.id.notes);
                TextView tv_time = (TextView) convertView.findViewById(R.id.times);

                tv_note.setText(notes.get(position));
                tv_time.setText(times.get(position));

            }
            return convertView;
        }
    }





}
