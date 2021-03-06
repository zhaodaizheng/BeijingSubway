package com.dsunny.subway.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.dsunny.subway.R;
import com.dsunny.subway.bean.SearchResult;
import com.dsunny.subway.constant.BaiduConst;
import com.dsunny.subway.constant.Message;
import com.dsunny.subway.engine.BaiduVoice;
import com.dsunny.subway.engine.TransPath;
import com.dsunny.subway.util.Utils;

/**
 * @author m 检索页面
 * 
 */
public class SearchActivity extends BaseActivity {
	public static final String TAG = "SearchActivity";

	private TextView tv_title;
	private EditText et_start, et_end, et_result;
	private Button btn_start, btn_end, btn_search, btn_talk;
	private BaiduASRDigitalDialog baiduDialog;
	private GestureOverlayView gesture;
	private GestureLibrary gLibrary;
	private DialogRecognitionListener drl;
	private View.OnClickListener ocl;
	private OnGesturePerformedListener ogpl;
	private boolean isBackPressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initViews();
		initDatas();
		initListener();
		registerListener();
	}

	@Override
	public void onBackPressed() {
		Timer timer = null;
		if (!isBackPressed) {
			isBackPressed = true;
			Utils.toast(mContext, Message.MSG_WARN_EXIT);
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isBackPressed = false;
				}
			}, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

	/**
	 * 初始化页面控件
	 */
	private void initViews() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_end = (Button) findViewById(R.id.btn_end);
		et_start = (EditText) findViewById(R.id.et_start);
		et_end = (EditText) findViewById(R.id.et_end);
		et_result = (EditText) findViewById(R.id.et_result);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_talk = (Button) findViewById(R.id.btn_talk);
		gesture = (GestureOverlayView) findViewById(R.id.gesture);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		isBackPressed = false;
		gLibrary = GestureLibraries.fromRawResource(mContext, R.raw.gestures);
		gLibrary.load();
	}

	/**
	 * 定义Listener变量
	 */
	private void initListener() {
		ocl = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_start:
						break;

					case R.id.btn_end:
						break;

					case R.id.btn_search:
						String startSName = et_start.getText().toString().trim();
						String endSName = et_end.getText().toString().trim();
						TransPath transPath = new TransPath();
						processResult(transPath.getTransPaths(startSName, endSName));
						break;

					case R.id.btn_talk:
						et_result.setText(null);
						showBaiduDialog();
						break;

					default:
						break;
				}
			}
		};

		drl = new DialogRecognitionListener() {

			@Override
			public void onResults(Bundle results) {
				ArrayList<String> rs = results != null ? results
				        .getStringArrayList(RESULTS_RECOGNITION) : null;
				if (rs != null && rs.size() > 0) {
					et_result.setText(rs.get(0));
					TransPath transPath = new TransPath();
					List<String> lstSNames = BaiduVoice.getInstance().getInputSNames(rs.get(0));
					processResult(transPath.getTransPaths(lstSNames.get(0), lstSNames.get(1)));
				}
			}
		};

		ogpl = new OnGesturePerformedListener() {

			@Override
			public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
				List<Prediction> lstPredictions = gLibrary.recognize(gesture);
				if (!lstPredictions.isEmpty()) {
					Prediction p0 = lstPredictions.get(0);
					if (p0.score > 8.0) {
						if (p0.name.equals(Message.GESTURE_ABOUTME)) {
							Intent intent = new Intent(mContext, AboutMeActivity.class);
							startActivity(intent);
						} else if (p0.name.equals(Message.GESTURE_FORWARD)) {

						} else if (p0.name.equals(Message.GESTURE_BACKWARD)) {

						}
					}
				}
			}
		};
	}

	/**
	 * 注册Listener
	 */
	private void registerListener() {
		btn_start.setOnClickListener(ocl);
		btn_end.setOnClickListener(ocl);
		btn_search.setOnClickListener(ocl);
		btn_talk.setOnClickListener(ocl);
		gesture.addOnGesturePerformedListener(ogpl);
	}

	/**
	 * 调用百度语音
	 */
	private void showBaiduDialog() {
		if (baiduDialog != null) {
			baiduDialog.dismiss();
		}
		Bundle params = new Bundle();
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, BaiduConst.API_KEY);
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, BaiduConst.SECRET_KEY);
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME,
		        BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG);

		baiduDialog = new BaiduASRDigitalDialog(mContext, params);
		baiduDialog.setDialogRecognitionListener(drl);
		baiduDialog.getParams().putInt(BaiduASRDigitalDialog.PARAM_PROP,
		        VoiceRecognitionConfig.PROP_INPUT);
		baiduDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE,
		        VoiceRecognitionConfig.LANGUAGE_CHINESE);
		baiduDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_START_TONE_ENABLE, true);
		baiduDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_END_TONE_ENABLE, true);
		baiduDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_TIPS_TONE_ENABLE, true);
		baiduDialog.show();
	}

	/**
	 * @param errCode
	 *            错误信息ID
	 */
	private void processResult(SearchResult sr) {
		switch (sr.code) {
			case Message.CHECK_OK:
				Intent intent = new Intent(mContext, ResultActivity.class);
				intent.putExtra(Message.KEY_RESULT, sr);
				startActivity(intent);
				break;
			case Message.ERR_START_EMPTY:
				Utils.toast(mContext, Message.MSG_ERR_START_EMPTY);
				break;
			case Message.ERR_END_EMPTY:
				Utils.toast(mContext, Message.MSG_ERR_END_EMPTY);
				break;
			case Message.ERR_SAME_STATION:
				Utils.toast(mContext, Message.MSG_ERR_SAME_STATION);
				break;
			case Message.ERR_START_NOT_EXIST:
				Utils.toast(mContext, Message.MSG_ERR_START_NOT_EXIST);
				break;
			case Message.ERR_END_NOT_EXIST:
				Utils.toast(mContext, Message.MSG_ERR_END_NOT_EXIST);
				break;
			default:
				break;
		}
	}

}
