package latera.kr.snowonmarch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.util.Constants;
import latera.kr.snowonmarch.util.MyContactManager;
import latera.kr.snowonmarch.util.MySmsManager;

public class SplashActivity extends AppCompatActivity {
	private static final long DELAY = 1500;

	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);


		mPrefs = getPreferences(Context.MODE_PRIVATE);

		// 초기 설정 필요한지 검사
		boolean initialized = mPrefs.getBoolean(Constants.PREFS_KEY_INITIALIZED, false);

		if (!initialized) {
			// 초기 설정
			MySmsManager.synchronize(this);
			MyContactManager.synchronize(this);

			// 초기 설정 후 init. flag를 true로 변경
			mEditor.putBoolean(Constants.PREFS_KEY_INITIALIZED, true);
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		}, DELAY);

	}
}
