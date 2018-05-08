package latera.kr.snowonmarch.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.security.acl.Group;

import io.realm.Realm;
import io.realm.RealmResults;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.adapter.EditMemberAdapter;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class GroupMemberEditActivity extends AppCompatActivity {

	public static final String ARG_GROUP_ID = "GROUP_ID";

	private static final String TAG = "ACTIVITY_EDIT_MEMBER";
	private static final int INVALID_GROUP_ID = -1;

	private RecyclerView rvMembers;
	private FloatingActionButton fabAddMember;

	private int groupId;
	private Realm mRealm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		groupId = getIntent().getIntExtra(ARG_GROUP_ID, INVALID_GROUP_ID);
		mRealm = Realm.getDefaultInstance();

		if (groupId == INVALID_GROUP_ID) {
			new AlertDialog.Builder(this)
					.setMessage(R.string.text_invalid_access)
					.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.dismiss();
						}
					})
					.create()
					.show();
		}

		rvMembers = findViewById(R.id.rv_edit_group_member_members);
		fabAddMember = findViewById(R.id.fab_edit_group_member_add_member);

		fabAddMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO: find person and add to group
			}
		});


		setupRv();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mRealm.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupRv() {
		GroupDBO group = mRealm.where(GroupDBO.class)
				.equalTo("id", groupId)
				.findFirst();

		EditMemberAdapter adapter = new EditMemberAdapter(group, new EditMemberAdapter.OnItemClickListener() {
			@Override
			public void onClick(PersonDBO person) {
				Log.d(TAG, "clicked person: " + person.getName());
			}
		});
		rvMembers.setAdapter(adapter);
		rvMembers.setLayoutManager(new LinearLayoutManager(this));
	}
}
