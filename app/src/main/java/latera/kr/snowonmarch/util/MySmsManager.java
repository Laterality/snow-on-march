package latera.kr.snowonmarch.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;

import io.realm.Realm;
import latera.kr.snowonmarch.dbo.MessageDBO;

public class MySmsManager {

	public static void synchronize(Context context) {
		SmsManager smsManager = SmsManager.getDefault();

		// public static final String INBOX = "content://sms/inbox";
		// public static final String SENT = "content://sms/sent";
		// public static final String DRAFT = "content://sms/draft";
		String[] projection = new String[]{
				Telephony.Sms.ADDRESS, Telephony.Sms.DATE, Telephony.Sms.BODY};
		Uri uri = Telephony.Sms.CONTENT_URI;
		Cursor cursor = context.getContentResolver().
				query(uri, projection,
						null, null, null);

		if (cursor.moveToFirst()) {
			String msg;
			int cnt = 0;
			do {
				msg = "";
				for (int i = 0 ; i < cursor.getColumnCount(); i++) {
					msg += cursor.getColumnName(i) + ": " + cursor.getString(i) + " ";
				}
				Log.d("SMS_TEST", msg);
				cnt++;
			}while(cursor.moveToNext() && cnt < 10);
		}

		cursor.close();
	}

	public static void onReceivSms(MessageDBO msg) {
		Realm realm = Realm.getDefaultInstance();

		realm.beginTransaction();
		realm.copyToRealm(msg);
		realm.commitTransaction();
	}
}
