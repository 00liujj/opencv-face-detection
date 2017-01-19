package com.example.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MyViewActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = new MyView(this);

		//隐藏标题栏部分（程序名字）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//隐藏状态栏部分（电池电量、时间等部分）
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);
	}
}
