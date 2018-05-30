package latera.kr.snowonmarch.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;

public class SynchronizeService extends Service {

	@Override
	public void onCreate() {
		final Context context = getApplicationContext();
		SmsObserver observer = new SmsObserver(new Handler(), context);
		context.getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI,
				true, observer);

	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {


		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}
}
