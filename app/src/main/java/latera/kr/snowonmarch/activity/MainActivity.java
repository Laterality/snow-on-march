package latera.kr.snowonmarch.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import latera.kr.snowonmarch.adapter.GroupRecyclerAdapter;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;
import latera.kr.snowonmarch.fragment.GroupFragment;
import latera.kr.snowonmarch.util.SynchronizeService;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MAIN_ACTIVITY";

	private FrameLayout flContent;
    private DrawerLayout mDrawerLayout;
    private ImageButton mIbtnMenu;
    private RecyclerView rvGroups;
    private Button btnEditGroup;
    private FloatingActionButton fabAddMember;

	private ActionBarDrawerToggle mDrawerToggle;

	private Realm mRealm;
	private GroupDBO mGroup;
	private int currentGroupId;

	private boolean backPressedOnce;

	@Override
	public void onStart() {
		super.onStart();

		// Start synchronization services
		startService(new Intent(this, SynchronizeService.class));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressedOnce = false;
        setContentView(R.layout.activity_main);
        mRealm = Realm.getDefaultInstance();

        flContent = findViewById(R.id.main_fl_content);
        mDrawerLayout = findViewById(R.id.main_dl_drawer);
        mIbtnMenu = findViewById(R.id.ibtn_main_menu);
        rvGroups = findViewById(R.id.main_drawer_rv_boxes);
        btnEditGroup = findViewById(R.id.btn_main_drawer_edit);
        fabAddMember = findViewById(R.id.fab_main_add_member);

        mIbtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.START))  {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
                else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View v) {
                mIbtnMenu.setImageDrawable(getDrawable(R.drawable.ic_menu_white_48px));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mIbtnMenu.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_48px));
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        btnEditGroup.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
		        startActivity(new Intent(MainActivity.this, GroupEditActivity.class));
	        }
        });

        fabAddMember.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
		        Intent i = new Intent(MainActivity.this, PersonFindActivity.class);
		        startActivityForResult(i, PersonFindActivity.REQUEST_FIND_PERSON);
	        }
        });

		setFragment(1);
		setUpGroup();

    }

    @Override
	public void onDestroy() {
    	super.onDestroy();
	    mRealm.close();
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PersonFindActivity.REQUEST_FIND_PERSON &&
				resultCode == PersonFindActivity.RESULT_PERSON_FOUND) {
			int id = data.getIntExtra(PersonFindActivity.RESULT_PERSON_ID, -1);

			if (id == -1) {
				// TODO: Invalid person id
			}
			else {
				mRealm.beginTransaction();
				mGroup.addPerson(mRealm.where(PersonDBO.class)
						.equalTo("id", id)
						.findFirst());
				mRealm.commitTransaction();
			}
		}
	}

	@Override
	public void onBackPressed() {
    	if (backPressedOnce) {
    		super.onBackPressed();
	    }
	    else {
		    backPressedOnce = true;
		    final Toast t = Toast.makeText(this, "한번 더 누르면 종료합니다",
				    Toast.LENGTH_LONG);
		    t.show();
		    new Handler().postDelayed(new Runnable() {
			    @Override
			    public void run() {
			    	backPressedOnce = false;
			    }
		    }, 2000);
	    }
	}

    private void setFragment(int groupId) {
    	currentGroupId = groupId;
    	mGroup = mRealm.where(GroupDBO.class)
			    .equalTo("id", groupId)
			    .findFirst();
	    GroupFragment frag = GroupFragment.getInstance(groupId);
	    getSupportFragmentManager()
			    .beginTransaction()
			    .add(flContent.getId(), frag)
			    .commit();
	    if (currentGroupId == 1) {
	    	fabAddMember.setVisibility(View.GONE);
	    }
	    else {
	    	fabAddMember.setVisibility(View.VISIBLE);
	    }
    }

    private void setUpGroup() {
		RealmResults<GroupDBO> groups = mRealm.where(GroupDBO.class)
				.findAll();
	    GroupRecyclerAdapter adapter = new GroupRecyclerAdapter(groups, new GroupRecyclerAdapter.OnItemClickListener() {
		    @Override
		    public void onItemClick(GroupDBO group) {
			    if (group.getId() != currentGroupId) {
				    setFragment(group.getId());
			    }
			    mDrawerLayout.closeDrawer(Gravity.START);
		    }
	    });
	    rvGroups.setAdapter(adapter);
	    rvGroups.setLayoutManager(new LinearLayoutManager(this));
    }
}
