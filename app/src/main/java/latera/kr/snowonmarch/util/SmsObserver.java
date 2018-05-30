package latera.kr.snowonmarch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;

import java.util.List;

public class SmsObserver extends ContentObserver {

	private static final String TAG = "SENT_SMS_OBSERVER";
	private final Context context;

	public SmsObserver(Handler handler, Context appContext) {
		super(handler);
		this.context = appContext;
	}

	public void onChange(boolean selfChange, Uri uri) {
		super.onChange(selfChange);
		Log.d(TAG, "onChange: " + uri.toString());

		List<String> segments = uri.getPathSegments();
		for(String s : segments) { Log.d(TAG, "seg: " + s); }
		boolean matches = segments.get(segments.size() - 1).matches("^(\\d)+$");
		Log.d(TAG, "uri matches: " + matches);
		if (!matches) {
			return;
		}

		// Get shared prefs
		SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME,
				Context.MODE_PRIVATE);

		String[] projection = new String[]{ Telephony.Sms._ID, Telephony.Sms.ADDRESS,
				Telephony.Sms.DATE, Telephony.Sms.BODY, Telephony.Sms.TYPE };

		// Query sms
		Cursor cursor = context.getContentResolver().
				query(uri, projection,
						null, null, null);
		Log.d(TAG, "queried: " + cursor.getCount());

		int idxId = cursor.getColumnIndex(Telephony.Sms._ID);
		int idxAddr = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
		int idxBody = cursor.getColumnIndex(Telephony.Sms.BODY);
		int idxDate = cursor.getColumnIndex(Telephony.Sms.DATE);
		int idxType = cursor.getColumnIndex(Telephony.Sms.TYPE);

		long latest = 0;
		if (cursor.moveToFirst()) {
			do {
				Log.d(TAG, "_id: " + cursor.getString(idxId));
				if (cursor.getLong(idxDate) > latest) { latest = cursor.getLong(idxDate); }
				new SmsObserverSynchronizeTask(cursor.getString(idxId), cursor.getString(idxAddr),
						cursor.getString(idxBody), cursor.getLong(idxDate),
						cursor.getInt(idxType)).execute();
			} while(cursor.moveToNext());

			// Update latest sms sync timestamp
			prefs.edit()
					.putLong(Constants.PREFS_KEY_LAST_SYNC_DATE_SMS, latest)
					.apply();
		}

		cursor.close();
	}
}
