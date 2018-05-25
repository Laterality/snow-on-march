package latera.kr.snowonmarch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.adapter.MessageLogRecyclerAdapter;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class MessageLogActivity extends AppCompatActivity {
	public static final String ARG_ADDRESS_SENDER = "address_sender";

	private TextView tvTitle;
	private RecyclerView rvMessage;
	private Toolbar toolbar;

	private String mAddress;
	private Realm mRealm;
	private MessageLogRecyclerAdapter mAdapter;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_log);

		mRealm = Realm.getDefaultInstance();

		rvMessage = findViewById(R.id.rv_message_log_messages);
		tvTitle = findViewById(R.id.tv_message_log_title);
		toolbar = findViewById(R.id.tb_message_log_toolbar);

		mAddress = getIntent().getStringExtra(ARG_ADDRESS_SENDER);

		setupRv();
		rvMessage.scrollToPosition(mAdapter.getItemCount() - 1);
		tvTitle.setText(mAddress);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_message_log, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_add_to_group) {
			Intent i = new Intent(this, GroupEditActivity.class);
			i.setAction(GroupEditActivity.ACTION_SELECT_GROUP);
			startActivityForResult(i, GroupEditActivity.REQUEST_SELECT_GROUP);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mRealm.close();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GroupEditActivity.REQUEST_SELECT_GROUP) {
			int groupId = data.getIntExtra(GroupEditActivity.RESULT_SELECTED_GROUP_ID, -1);
			if (groupId > 1) {
				// add the user to group
				mRealm.beginTransaction();
				GroupDBO group = mRealm.where(GroupDBO.class)
						.equalTo("id", groupId)
						.findFirst();
				PersonDBO person = mRealm.where(PersonDBO.class)
						.equalTo("address", mAddress)
						.findFirst();

				if (person == null) {
					Toast.makeText(this, "연락처에서 찾을 수 없습니다",
							Toast.LENGTH_LONG)
							.show();
				}
				else {
					group.addPerson(person);
					Toast.makeText(this, "추가했습니다", Toast.LENGTH_SHORT)
							.show();
				}
				mRealm.commitTransaction();
			}
		}
	}

	private void setupRv() {
		RealmResults<MessageDBO> result = mRealm.where(MessageDBO.class).equalTo("address", mAddress)
				.sort("dateSent", Sort.ASCENDING)
				.findAll();

		mAdapter = new MessageLogRecyclerAdapter(result);
		rvMessage.setAdapter(mAdapter);
		rvMessage.setLayoutManager(new LinearLayoutManager(this));
	}
}
