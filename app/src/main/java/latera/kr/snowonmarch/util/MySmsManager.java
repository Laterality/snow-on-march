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
import latera.kr.snowonmarch.dbo.PersonDBO;

public class MySmsManager extends AsyncTask {

	private static final String TAG = "SMS_MANAGER";

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
				Telephony.Sms.ADDRESS, Telephony.Sms.DATE, Telephony.Sms.BODY, Telephony.Sms.TYPE};
		Uri uri = Telephony.Sms.CONTENT_URI;
		Cursor cursor = context.getContentResolver().
				query(uri, null,
						null, null, null);
		int idxAddress = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
		int idxDate = cursor.getColumnIndex(Telephony.Sms.DATE);
		int idxBody = cursor.getColumnIndex(Telephony.Sms.BODY);
		int idxType = cursor.getColumnIndex(Telephony.Sms.TYPE);

		Realm realm = Realm.getDefaultInstance();
		realm.beginTransaction();
		MessageDBO message;
		if (cursor.moveToFirst()) {
			do {
				message = new MessageDBO(cursor.getString(idxAddress),
						cursor.getString(idxBody), cursor.getLong(idxDate),
						cursor.getInt(idxType) == Telephony.Sms.MESSAGE_TYPE_SENT);

				realm.copyToRealm(message);

			}while(cursor.moveToNext());
		}

		cursor.close();
		realm.commitTransaction();
		realm.close();
	}

	public static void onReceiveSms(String address, String body, long date) {
		Realm realm = Realm.getDefaultInstance();

		PersonDBO sender = realm.where(PersonDBO.class)
				.equalTo("address", address).findFirst();
		realm.beginTransaction();
		MessageDBO msg = realm.copyToRealm(new MessageDBO(address, body, date));
		if  (sender != null) {
			msg.setSender(sender);
			sender.addMessage(msg);
		}
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
