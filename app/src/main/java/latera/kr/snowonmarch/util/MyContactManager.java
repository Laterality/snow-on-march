package latera.kr.snowonmarch.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class MyContactManager {

	public static void synchronize(Context context) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
		};

		Cursor people = context.getContentResolver().query(uri, projection, null, null, null);

		int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		Log.d("CONTACTS_TEST", "scanned: " + people.getCount());
		people.moveToFirst();
		for (int i = 0; i < 10; i++) {
			String msg = people.getString(indexName) +": " + people.getString(indexNumber);
			Log.d("CONTACTS_TEST", msg);
			people.moveToNext();
		}
		people.close();
	}
}
