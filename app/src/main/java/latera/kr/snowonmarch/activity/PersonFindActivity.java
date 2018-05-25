package latera.kr.snowonmarch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import io.realm.Realm;
import io.realm.RealmResults;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.adapter.MemberRecyclerAdapter;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class PersonFindActivity extends AppCompatActivity {

	public static final int REQUEST_FIND_PERSON = 10;
	public static final int RESULT_PERSON_FOUND = 1;
	public static final int RESULT_PERSON_NOT_FOUND = -1;

	public static final String RESULT_PERSON_ID = "person_id";

	private static final String TAG = "ACTIVITY_FIND_PERSON";

	private EditText etInput;
	private ImageButton ibtnClear;
	private RecyclerView rvResults;

	private Realm mRealm;
	private MemberRecyclerAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_person);

		mRealm = Realm.getDefaultInstance();

		etInput = findViewById(R.id.et_find_person_input);
		ibtnClear = findViewById(R.id.ibtn_find_person_clear_input);
		rvResults = findViewById(R.id.rv_find_person_results);

		etInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				// TODO: update here!
				if (charSequence.length() > 0) {
					ibtnClear.setVisibility(View.VISIBLE);
				}
				else {
					ibtnClear.setVisibility(View.GONE);
				}
				update();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		ibtnClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etInput.setText("");
			}
		});

		setupRv();
	}

	private void setupRv() {
		RealmResults<PersonDBO> people = mRealm.where(PersonDBO.class)
				.findAll();
		mAdapter = new MemberRecyclerAdapter(people, new MemberRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onClick(PersonDBO person) {
				Intent i = new Intent();
				i.putExtra(RESULT_PERSON_ID, person.getId());
				setResult(RESULT_PERSON_FOUND, i);
				finish();
			}
		});
		LinearLayoutManager lm = new LinearLayoutManager(this);
		rvResults.setAdapter(mAdapter);
		rvResults.setLayoutManager(lm);
		DividerItemDecoration did = new DividerItemDecoration(rvResults.getContext(),
				lm.getOrientation());
		rvResults.addItemDecoration(did);
	}

	private void update() {
		String keyword = etInput.getText().toString();
		if (keyword.isEmpty()) {
			mAdapter.updateData(mRealm.where(PersonDBO.class)
					.findAllAsync());
		}
		else {
			mAdapter.updateData(mRealm.where(PersonDBO.class)
					.like("name", "*" + keyword + "*")
					.findAllAsync());
		}

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

}
