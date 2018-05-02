package latera.kr.snowonmarch.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
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
import android.widget.ImageButton;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import latera.kr.snowonmarch.adapter.MessagesByNumberAdapter;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;
import latera.kr.snowonmarch.util.MyContactManager;
import latera.kr.snowonmarch.util.MySmsManager;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MAIN_ACTIVITY";

    private DrawerLayout mDrawerLayout;
    private ImageButton mIbtnMenu;
    private RecyclerView mLvMessage;

	private ActionBarDrawerToggle mDrawerToggle;

	private Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRealm = Realm.getDefaultInstance();

        mDrawerLayout = findViewById(R.id.main_dl_drawer);
        mIbtnMenu = findViewById(R.id.ibtn_main_menu);
        mLvMessage = findViewById(R.id.lv_main_messages);

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

		setUpRecyclerView();
    }

    @Override
	public void onDestroy() {
    	super.onDestroy();
	    mRealm.close();
    }

    private void setUpRecyclerView() {
	    RealmResults<PersonDBO> people = mRealm.where(PersonDBO.class)
			    .greaterThan("recent", -1) // 문자 내역이 있는 연락처 filter
			    .sort("recent", Sort.DESCENDING) // 최근 수신일자 내림차순
			    .findAll();
	    MessagesByNumberAdapter adapter = new MessagesByNumberAdapter(people,
			    new MessagesByNumberAdapter.OnItemClickListener() {
		    @Override
		    public void onClick(View v, PersonDBO p) {
			    Intent i = new Intent(MainActivity.this, MessageLogActivity.class);
			    i.putExtra(MessageLogActivity.ARG_ADDRESS_SENDER, p.getAddress());
			    startActivity(i);
		    }
	    });
	    mLvMessage.setAdapter(adapter);
	    mLvMessage.setLayoutManager(new LinearLayoutManager(this));

    }
}
