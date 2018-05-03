package latera.kr.snowonmarch.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import latera.kr.snowonmarch.adapter.GroupAdapter;
import latera.kr.snowonmarch.adapter.MessagesByNumberAdapter;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;
import latera.kr.snowonmarch.fragment.GroupFragment;
import latera.kr.snowonmarch.util.MyContactManager;
import latera.kr.snowonmarch.util.MySmsManager;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MAIN_ACTIVITY";

	private FrameLayout flContent;
    private DrawerLayout mDrawerLayout;
    private ImageButton mIbtnMenu;
    private RecyclerView rvGroups;

	private ActionBarDrawerToggle mDrawerToggle;

	private Realm mRealm;
	private int currentGroupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRealm = Realm.getDefaultInstance();

        flContent = findViewById(R.id.main_fl_content);
        mDrawerLayout = findViewById(R.id.main_dl_drawer);
        mIbtnMenu = findViewById(R.id.ibtn_main_menu);
        rvGroups = findViewById(R.id.main_drawer_rv_boxes);

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

		setFragment(1);
		setUpGroup();

    }

    @Override
	public void onDestroy() {
    	super.onDestroy();
	    mRealm.close();
    }

    private void setFragment(int groupId) {
    	currentGroupId = groupId;
	    GroupFragment frag = GroupFragment.getInstance(groupId);
	    getSupportFragmentManager()
			    .beginTransaction()
			    .add(flContent.getId(), frag)
			    .commit();
    }

    private void setUpGroup() {
		RealmResults<GroupDBO> groups = mRealm.where(GroupDBO.class)
				.findAll();
	    GroupAdapter adapter = new GroupAdapter(groups, new GroupAdapter.OnItemClickListener() {
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
