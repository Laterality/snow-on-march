package latera.kr.snowonmarch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class InitialSmsSynchronizeTask extends AsyncTask {

	private static final String TAG = "SMS_MANAGER";

	private final Context mContext;
	private final OnSynchronizationFinishedListener mListener;

	public InitialSmsSynchronizeTask(Context context, OnSynchronizationFinishedListener listener) {
		this.mContext = context;
		this.mListener = listener;
	}

	private void synchronize(Context context) {
		// public static final String INBOX = "content://sms/inbox";
		// public static final String SENT = "content://sms/sent";
		// public static final String DRAFT = "content://sms/draft";

		String[] projection = new String[]{ Telephony.Sms._ID, Telephony.Sms.ADDRESS,
				Telephony.Sms.DATE, Telephony.Sms.BODY, Telephony.Sms.TYPE };
		Uri uri = Telephony.Sms.CONTENT_URI;

		// Query sms
		Cursor cursor = context.getContentResolver().
				query(uri, projection,
						null, null, null);

		int idxId = cursor.getColumnIndex(Telephony.Sms._ID);
		int idxAddress = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
		int idxDate = cursor.getColumnIndex(Telephony.Sms.DATE);
		int idxBody = cursor.getColumnIndex(Telephony.Sms.BODY);
		int idxType = cursor.getColumnIndex(Telephony.Sms.TYPE);

		long latest = 0;
		Realm realm = Realm.getDefaultInstance();
		realm.beginTransaction();
		MessageDBO message;
		if (cursor.moveToFirst()) {
			do {
				if (cursor.getLong(idxDate) > latest) { latest = cursor.getLong(idxDate); }
				message = new MessageDBO(cursor.getString(idxId), cursor.getString(idxAddress),
						cursor.getString(idxBody), cursor.getLong(idxDate),
						cursor.getInt(idxType) == Telephony.Sms.MESSAGE_TYPE_SENT);

				realm.copyToRealm(message);

			}while(cursor.moveToNext());
		}

		context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
				.edit()
				.putLong(Constants.PREFS_KEY_LAST_SYNC_DATE_SMS, latest)
				.apply();

		cursor.close();
		realm.commitTransaction();
		realm.close();
	}

	@Override
	protected Object doInBackground(Object[] objects) {
		synchronize(mContext);
		return null;
	}

	@Override
	protected void onPostExecute(Object o) {
		if (mListener != null) {
			mListener.onFinish(OnSynchronizationFinishedListener.SYNCH_SMS);
		}
	}
}
