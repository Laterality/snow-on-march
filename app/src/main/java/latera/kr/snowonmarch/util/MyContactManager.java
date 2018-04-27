package latera.kr.snowonmarch.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ListIterator;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class MyContactManager extends AsyncTask {

	private final Context mContext;
	private final OnSynchronizationFinishedListener mListener;

	public MyContactManager(Context context, OnSynchronizationFinishedListener listener) {
		mContext = context;
		mListener = listener;
	}
	private void synchronize(Context context) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
		};

		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		int idxName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		int idxAddress = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

		Realm realm = Realm.getDefaultInstance();
		realm.beginTransaction();

		PersonDBO person;
		RealmResults<MessageDBO> messagesSent;

		// Get max of id value
		Number maxId = realm.where(PersonDBO.class).max("id");
		int idNext = maxId == null ? 1 : maxId.intValue() + 1;

		cursor.moveToFirst();
		do {
			// Insert person object
			person = realm.copyToRealm(new PersonDBO(idNext++, cursor.getString(idxName), cursor.getString(idxAddress)));


			// Find messages which are sent by the person
			messagesSent = realm.where(MessageDBO.class).equalTo("address", cursor.getString(idxAddress)).findAll();
			person.addMessages(messagesSent.listIterator());

			realm.copyToRealm(person);
		} while(cursor.moveToNext());

		// sender가 정해지지 않은 메시지 검색
		RealmResults<MessageDBO> messagesHasNoSenderInContact = realm.where(MessageDBO.class)
				.isNull("sender").findAll();

		ListIterator<MessageDBO> it = messagesHasNoSenderInContact.listIterator();
		MessageDBO msg;
		PersonDBO sender;
		while (it.hasNext()) {
			msg = it.next();
			// address로 검색
			sender = realm.where(PersonDBO.class).equalTo("address", msg.getAddress()).findFirst();
			if (sender == null) {
				// sender가 없는 경우, 새로 생성
				sender = realm.copyToRealm(new PersonDBO(idNext++, msg.getAddress(), msg.getAddress()));
			}
			// sender 지정
			msg.setSender(sender);

		}

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
			mListener.onFinish(OnSynchronizationFinishedListener.SYNCH_CONTACT);
		}
	}
}
