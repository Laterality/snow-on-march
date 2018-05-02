package latera.kr.snowonmarch.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.adapter.MessageLogAdapter;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class MessageLogActivity extends AppCompatActivity {
	public static final String ARG_ADDRESS_SENDER = "address_sender";

	private RecyclerView rvMessage;

	private String mAddress;
	private Realm mRealm;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_log);

		mRealm = Realm.getDefaultInstance();

		rvMessage = findViewById(R.id.rv_message_log_messages);

		mAddress = getIntent().getStringExtra(ARG_ADDRESS_SENDER);

		setupRv();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mRealm.close();
	}

	private void setupRv() {
		RealmResults<MessageDBO> result = mRealm.where(MessageDBO.class).equalTo("address", mAddress)
				.sort("dateSent", Sort.ASCENDING)
				.findAll();

		MessageLogAdapter adapter = new MessageLogAdapter(result);
		rvMessage.setAdapter(adapter);
		rvMessage.setLayoutManager(new LinearLayoutManager(this));
	}
}
