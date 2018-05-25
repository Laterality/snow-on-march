package latera.kr.snowonmarch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.adapter.EditGroupRecyclerAdapter;
import latera.kr.snowonmarch.adapter.GroupRecyclerAdapter;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dialog.AddGroupDialog;

public class GroupEditActivity extends AppCompatActivity implements AddGroupDialog.AddGroupDialogListener{

	private static final String TAG = "GROUP_EDIT_ACTIVITY";
	private static final String TAG_DIALOG_FRAGMENT_ADD_GROUP = "ADD_GROUP_DIALOG_FRAGMENT";

	public static int REQUEST_SELECT_GROUP = 1;
	public static int RESULT_GROUP_SELECTED = 10;
	public static String ACTION_SELECT_GROUP = "REQUEST_SELECT";
	public static String RESULT_SELECTED_GROUP_ID = "SELECTED_GROUP_ID";

	private RecyclerView rvGroups;
	private FloatingActionButton fabAddGroup;

	private Realm mRealm;
	private boolean isSelectMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_group);
		mRealm = Realm.getDefaultInstance();

		rvGroups = findViewById(R.id.rv_edit_group_groups);
		fabAddGroup = findViewById(R.id.fab_edit_group_add_group);

		fabAddGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AddGroupDialog dialog = new AddGroupDialog();
				dialog.show(getSupportFragmentManager(), TAG_DIALOG_FRAGMENT_ADD_GROUP);
			}
		});

		String action = getIntent().getAction();

		if (action != null && action.equals(ACTION_SELECT_GROUP)) {
			isSelectMode = true;
		}
		else { isSelectMode = false; }

		setUpRecyclerview();
	}

	private void setUpRecyclerview() {
		// Find all groups except Default group(Home)
		RealmResults<GroupDBO> groups = mRealm.where(GroupDBO.class)
				.notEqualTo("id", 1)
				.findAll();
		GroupRecyclerAdapter adapter = new GroupRecyclerAdapter(groups, new GroupRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(GroupDBO group) {
				if (isSelectMode) {
					Intent i = new Intent();
					i.putExtra(RESULT_SELECTED_GROUP_ID, group.getId());
					setResult(RESULT_GROUP_SELECTED, i);
					finish();
				}
				else {
					Intent i = new Intent(GroupEditActivity.this, GroupMemberEditActivity.class);
					i.putExtra(GroupMemberEditActivity.ARG_GROUP_ID, group.getId());
					startActivity(i);
				}
			}
		});
		rvGroups.setAdapter(adapter);
		rvGroups.setLayoutManager(new LinearLayoutManager(this));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mRealm.close();
		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public void onOk(String name, int color) {
		try {
			Number maxId = mRealm.where(GroupDBO.class).max("id");
			int id = 1;
			if (maxId != null) { id = maxId.intValue() + 1; }

			mRealm.beginTransaction();
			mRealm.copyToRealm(new GroupDBO(id, name, color));
			mRealm.commitTransaction();
			Toast.makeText(this, R.string.text_group_created, Toast.LENGTH_LONG).show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
