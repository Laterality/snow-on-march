package latera.kr.snowonmarch.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmResults;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.adapter.EditGroupAdapter;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dialog.AddGroupDialog;

public class GroupEditActivity extends AppCompatActivity implements AddGroupDialog.AddGroupDialogListener{

	private static final String TAG = "GROUP_EDIT_ACTIVITY";
	private static final String TAG_DIALOG_FRAGMENT_ADD_GROUP = "ADD_GROUP_DIALOG_FRAGMENT";

	private RecyclerView rvGroups;
	private FloatingActionButton fabAddGroup;

	private Realm mRealm;

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

		setUpRecyclerview();
	}

	private void setUpRecyclerview() {
		// Find all groups except Default group(Home)
		RealmResults<GroupDBO> groups = mRealm.where(GroupDBO.class)
				.notEqualTo("id", 1)
				.findAll();
		EditGroupAdapter adapter = new EditGroupAdapter(groups, new EditGroupAdapter.OnItemClickListener() {
			@Override
			public void onClick(GroupDBO group) {
				Log.d(TAG, "Clicked group: " + group.getId());
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
	public void onOk(String name, String color) {
		Log.d(TAG, "We should group as named " + name + " with color " + color);
	}
}
