package com.example.tasks;

import com.avos.avoscloud.AVUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

	Handler handler = new Handler();

	Runnable runnable = new Runnable() {

		public void run() {
			Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();

		}

	};
	
	Runnable runnable1 = new Runnable() {

		public void run() {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 跳转到首页
        	handler.postDelayed(runnable1, 2000);
        } else {
            //缓存用户对象为空时，可打开用户注册界面…
        	handler.postDelayed(runnable, 2000);
        }

	}
}
