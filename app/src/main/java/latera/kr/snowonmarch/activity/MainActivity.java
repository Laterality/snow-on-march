package latera.kr.snowonmarch.activity;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import latera.kr.snowonmarch.adapter.MessagesByNumberAdapter;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.util.MyContactManager;
import latera.kr.snowonmarch.util.MySmsManager;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ImageButton mIbtnMenu;
    private ListView mLvMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mLvMessage.setAdapter(new MessagesByNumberAdapter(this));

    }
}
