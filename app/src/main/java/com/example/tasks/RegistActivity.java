package com.example.tasks;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends Activity {
	EditText RegistName;
	EditText RegistPwd;
	EditText RegistPwd2;
	Button Regist;
	String RName;
	String RPwd;
	String RPwd2;
	String regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		RegistName = (EditText) findViewById(R.id.RegistName);
		RegistPwd = (EditText) findViewById(R.id.RegistPwd);
		RegistPwd2 = (EditText) findViewById(R.id.RegistPwd2);
		Regist = (Button) findViewById(R.id.Regist);



		// 修改状态栏颜色，4.4+生效
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus();
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.drawable.iphone);//通知栏所需颜色

		Regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				RName = RegistName.getText().toString();
				RPwd = RegistPwd.getText().toString();
				RPwd2 = RegistPwd2.getText().toString();

				if (!"".equals(RName) && !"".equals(RPwd) && !"".equals(RPwd2)) {
					if (!RPwd.equals(RPwd2)) {

						Toast.makeText(RegistActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();


					} else {

						AVUser user = new AVUser();// 新建 AVUser 对象实例
						user.setUsername(RName);// 设置用户名
						user.setPassword(RPwd);// 设置密码
						user.signUpInBackground(new SignUpCallback() {
							@Override
							public void done(AVException e) {
								if (e == null) {
									// 注册成功
									Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
									Handler handler = new Handler();
									Runnable runnable = new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
											startActivity(intent);
											finish();

										}
									};
									handler.postDelayed(runnable, 2000);
								}
							}
						});
					}
				} else {
					Toast.makeText(RegistActivity.this, "请将注册信息输入完整！", Toast.LENGTH_LONG).show();
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
}
