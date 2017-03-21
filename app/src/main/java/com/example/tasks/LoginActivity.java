package com.example.tasks;

import org.w3c.dom.Text;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	TextView LoginName;
	TextView LoginPwd;
	Button Login;
	TextView Regist1;
	String  LN;
	String LP;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		LoginName=(TextView) findViewById(R.id.LoginName);
		LoginPwd=(TextView) findViewById(R.id.LoginPwd);
		Login=(Button) findViewById(R.id.Login);
		Regist1=(TextView) findViewById(R.id.Regist1);


		// 修改状态栏颜色，4.4+生效
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus();
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.drawable.iphone);//通知栏所需颜色







		Login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub





				
				LN=LoginName.getText().toString();
				LP=LoginPwd.getText().toString();
				
				AVUser.logInInBackground(LN, LP, new LogInCallback<AVUser>() {
		            @Override
		            public void done(AVUser avUser, AVException e) {
		            	
		            	if (avUser!=null) {
		            		Intent intent=new Intent(LoginActivity.this, MainActivity.class);
		            		startActivity(intent);
		            		finish();
							
						}

		            }
		        });

				
				
				
			}
		});
		
		
		
		Regist1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
				startActivity(intent);
				
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
