package latera.kr.snowonmarch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.TextView;

import io.realm.Realm;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.util.Constants;
import latera.kr.snowonmarch.util.MyContactManager;
import latera.kr.snowonmarch.util.MySmsManager;
import latera.kr.snowonmarch.util.OnSynchronizationFinishedListener;

public class SplashActivity extends AppCompatActivity {
	private static final long DELAY = 1500;

	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;

	private TextView tvStateSms;
	private TextView tvStateContact;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		tvStateSms = findViewById(R.id.tv_splash_state_sms);
		tvStateContact = findViewById(R.id.tv_splash_state_contact);


		mPrefs = getPreferences(Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();

		// 초기 설정 필요한지 검사
		boolean initialized = mPrefs.getBoolean(Constants.PREFS_KEY_INITIALIZED, false);

		Realm.init(this);
		if (!initialized) {
			// 초기 설정
			tvStateContact.setText("synchronizing...");
			tvStateSms.setText("synchronizing...");

			new MySmsManager(this, new OnSynchronizationFinishedListener() {
				@Override
				public void onFinish(int syncID) {
					tvStateSms.setText("synchronized");

					new MyContactManager(SplashActivity.this, new OnSynchronizationFinishedListener() {
						@Override
						public void onFinish(int syncID) {
							tvStateContact.setText("synchronized");
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
