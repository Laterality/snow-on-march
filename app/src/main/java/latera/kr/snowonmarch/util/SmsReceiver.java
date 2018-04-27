package latera.kr.snowonmarch.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsMessage;
import android.util.Log;

import org.joda.time.DateTime;

import latera.kr.snowonmarch.dbo.MessageDBO;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "RECEIVER";

	@Override
	public void onReceive(Context context, @NonNull Intent intent)
	{
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			Log.d(TAG, "SMS Received");
			Bundle b = intent.getExtras();
			String format = intent.getStringExtra("format");
			Object[] messages = (Object[]) b.get("pdus");
			SmsMessage[] smsMessages = new SmsMessage[messages.length];

			for(int i = 0 ; i < messages.length; i++)
			{
				if(Build.VERSION.SDK_INT >= 23)
				{
					smsMessages[i] = SmsMessage.createFromPdu((byte[])messages[i], format);
				}
				else
				{
					smsMessages[i] = SmsMessage.createFromPdu((byte[])messages[i]);
				}
			}

			Log.d(TAG, "Received Time: " + new DateTime(smsMessages[0].getTimestampMillis()).toString());
			Log.d(TAG, "From: " + smsMessages[0].getDisplayOriginatingAddress());
			Log.d(TAG, "Content: " + smsMessages[0].getMessageBody());
			MySmsManager.onReceiveSms(new MessageDBO(smsMessages[0].getDisplayOriginatingAddress(),
					smsMessages[0].getMessageBody(), smsMessages[0].getTimestampMillis()));

		}
	}
}
