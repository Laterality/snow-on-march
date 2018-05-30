package latera.kr.snowonmarch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.realm.Realm;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.util.Constants;
import latera.kr.snowonmarch.util.InitialSmsSynchronizeTask;
import latera.kr.snowonmarch.util.InitialContactSynchronizeTask;
import latera.kr.snowonmarch.util.OnSynchronizationFinishedListener;
import latera.kr.snowonmarch.util.SynchronizeService;

public class SplashActivity extends AppCompatActivity {
	private static final long DELAY = 1000;

	private ProgressBar pbSync;
	private TextView tvSync;

	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		pbSync = findViewById(R.id.pb_splash_sync_progress);
		tvSync = findViewById(R.id.tv_splash_sync_state);

		mPrefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();

		// 초기 설정 필요한지 검사
		boolean initialized = mPrefs.getBoolean(Constants.PREFS_KEY_INITIALIZED, false);

		Realm.init(this);
		if (!initialized) {
			// 초기 설정
			pbSync.setVisibility(View.VISIBLE);
			tvSync.setVisibility(View.VISIBLE);
			tvSync.setText(R.string.text_init_setting);

			// Start
			new InitialSmsSynchronizeTask(this, new OnSynchronizationFinishedListener() {
				@Override
				public void onFinish(int syncID) {

					new InitialContactSynchronizeTask(SplashActivity.this, new OnSynchronizationFinishedListener() {
						@Override
						public void onFinish(int syncID) {
							// 초기 설정 후 init. flag를 true로 변경
							mEditor.putBoolean(Constants.PREFS_KEY_INITIALIZED, true);
							mEditor.apply();
							moveToMain();
						}
					}).execute();
				}
			}).execute();
		}
		else {
			moveToMain();
		}

	}

	private void moveToMain() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		}, DELAY);
	}
}
