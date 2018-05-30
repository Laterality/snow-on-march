package latera.kr.snowonmarch.util;

import android.os.AsyncTask;
import android.provider.Telephony;
import android.util.Log;

import io.realm.Realm;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class SmsObserverSynchronizeTask extends AsyncTask {

	private static final String TAG = "SmsObserverSync";

	private final String id;
	private final String address;
	private final String body;
	private final long date;
	private final int type;

	public SmsObserverSynchronizeTask(String id, String addr, String body, long date, int type) {
		this.id = id;
		this.address = addr;
		this.body = body;
		this.date = date;
		this.type = type;
	}

	@Override
	protected Object doInBackground(Object[] objects) {
		onReceiveSms();
		return null;
	}

	private void onReceiveSms() {
		Realm realm = Realm.getDefaultInstance();

		PersonDBO sender = realm.where(PersonDBO.class)
				.equalTo("address", address).findFirst();
		realm.beginTransaction();
		MessageDBO msg = realm.where(MessageDBO.class).equalTo("id", id)
				.findFirst();
		// Check if message already exists
		Log.d(TAG, "sent? " + (type == Telephony.Sms.MESSAGE_TYPE_SENT));
		if (msg == null) {
			msg = realm.copyToRealm(new MessageDBO(id, address, body, date,
					type == Telephony.Sms.MESSAGE_TYPE_SENT));
		}
		else {
			msg.setSent(type == Telephony.Sms.MESSAGE_TYPE_SENT);
		}
		if  (sender != null) {
			msg.setSender(sender);
			sender.addMessage(msg);
		}
		realm.commitTransaction();
	}
}
