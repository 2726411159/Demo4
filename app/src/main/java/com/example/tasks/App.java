package com.example.tasks;



import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import android.app.Application;

public class App extends Application {
	public static int hour2;
	public static int minute2;

	@Override
	public void onCreate() {
		super.onCreate();
		AVOSCloud.initialize(this, "vGbbd5fJWvEU8J2hkltkatDT-gzGzoHsz",
				"Rado1D43bCUv8pxWxWMzvrmC");



		AVObject testObject = new AVObject("TestObject");
		testObject.put("user", "bar");
		testObject.saveInBackground();

	}
}
