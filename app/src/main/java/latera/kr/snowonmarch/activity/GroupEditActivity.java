package latera.kr.snowonmarch.activity;

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
		Log.d(TAG, "found to edit: " + groups.size());
		EditGroupRecyclerAdapter adapter = new EditGroupRecyclerAdapter(groups, new EditGroupRecyclerAdapter.OnItemClickListener() {
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
	public void onOk(String name, int color) {
		Log.d(TAG, "We should group as named " + name + " with color " + color);
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
