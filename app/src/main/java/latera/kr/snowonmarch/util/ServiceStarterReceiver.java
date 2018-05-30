package latera.kr.snowonmarch.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

public class ServiceStarterReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, @NonNull Intent intent)
	{
		Log.d("RECEIVER", "onReceive");
		context.startService(new Intent(context, SynchronizeService.class));
	}
}
