package latera.kr.snowonmarch.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;

import io.realm.Realm;
import latera.kr.snowonmarch.dbo.MessageDBO;

public class MySmsManager extends AsyncTask {

	private final Context mContext;
	private final OnSynchronizationFinishedListener mListener;

	public MySmsManager(Context context, OnSynchronizationFinishedListener listener) {
		this.mContext = context;
		this.mListener = listener;
	}

	private void synchronize(Context context) {
		// public static final String INBOX = "content://sms/inbox";
		// public static final String SENT = "content://sms/sent";
		// public static final String DRAFT = "content://sms/draft";
		String[] projection = new String[]{
				Telephony.Sms.ADDRESS, Telephony.Sms.DATE, Telephony.Sms.BODY};
		Uri uri = Telephony.Sms.CONTENT_URI;
		Cursor cursor = context.getContentResolver().
				query(uri, projection,
						null, null, null);
		int idxAddress = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
		int idxDate = cursor.getColumnIndex(Telephony.Sms.DATE);
		int idxBody = cursor.getColumnIndex(Telephony.Sms.BODY);

		Realm realm = Realm.getDefaultInstance();
		realm.beginTransaction();
		MessageDBO message;
		if (cursor.moveToFirst()) {
			do {
				message = new MessageDBO(cursor.getString(idxAddress),
						cursor.getString(idxBody), cursor.getLong(idxDate));

				realm.copyToRealm(message);

			}while(cursor.moveToNext());
		}

		cursor.close();
		realm.commitTransaction();
		realm.close();
	}

	public static void onReceiveSms(MessageDBO msg) {
		Realm realm = Realm.getDefaultInstance();

		realm.beginTransaction();
		realm.copyToRealm(msg);
		realm.commitTransaction();
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
