package com.dsunny.subway.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.dsunny.subway.SubwayApp;

/**
 * @author m Activity基类
 * 
 */
public class BaseActivity extends Activity {

	protected Context mContext;
	protected SubwayApp mApp;

	public BaseActivity() {
		this.mContext = this;
		mApp = SubwayApp.getInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
