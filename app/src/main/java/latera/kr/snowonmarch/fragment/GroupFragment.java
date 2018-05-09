package latera.kr.snowonmarch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.activity.MessageLogActivity;
import latera.kr.snowonmarch.adapter.MessagesByNumberRecyclerAdapter;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class GroupFragment extends Fragment {
	private static final String TAG = "";
	private static final String ARG_GROUP_ID = "group_id";
	public static final int SHOW_ALL = -1;

	private Realm mRealm;
	private int mGroupId;

	private RecyclerView rvList;

	public static GroupFragment getInstance(int groupId) {
		GroupFragment frag = new GroupFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_GROUP_ID, groupId);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		if (mRealm == null) { mRealm = Realm.getDefaultInstance(); }

		mGroupId = getArguments().getInt(ARG_GROUP_ID);

		View v = inflater.inflate(R.layout.fragment_group, container, false);

		rvList = v.findViewById(R.id.lv_main_messages);

		setUpRecyclerView();
		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (!mRealm.isClosed()) {
			mRealm.close();
		}
	}

	private void setUpRecyclerView() {
		OrderedRealmCollection<PersonDBO> people = null;
		if (mGroupId == SHOW_ALL)
		{
			people = mRealm.where(PersonDBO.class)
					.greaterThan("recent", -1) // 문자 내역이 있는 연락처 filter
					.sort("recent", Sort.DESCENDING) // 최근 수신일자 내림차순
					.findAll();
		}
		else {
			GroupDBO groupFound = mRealm.where(GroupDBO.class)
					.equalTo("id", mGroupId)
					.findFirst();
			if (groupFound == null) {
				Log.d(TAG, "group not exist");
			}
			else {
				people = groupFound.getPeople();
			}
		}

		MessagesByNumberRecyclerAdapter adapter = new MessagesByNumberRecyclerAdapter(people,
				new MessagesByNumberRecyclerAdapter.OnItemClickListener() {
					@Override
					public void onClick(View v, PersonDBO p) {
						Intent i = new Intent(getActivity(), MessageLogActivity.class);
						i.putExtra(MessageLogActivity.ARG_ADDRESS_SENDER, p.getAddress());
						startActivity(i);
					}
				});
		rvList.setAdapter(adapter);
		rvList.setLayoutManager(new LinearLayoutManager(getActivity()));

	}
}
