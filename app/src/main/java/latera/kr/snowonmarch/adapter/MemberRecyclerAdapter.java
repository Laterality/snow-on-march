package latera.kr.snowonmarch.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.dbo.GroupDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

public class MemberRecyclerAdapter extends RealmRecyclerViewAdapter<PersonDBO, MemberRecyclerAdapter.ViewHolder> {

	private final OnItemClickListener mListener;

	public MemberRecyclerAdapter(@Nullable GroupDBO data, OnItemClickListener listener) {
		super(data.getPeople(), true);
		this.mListener = listener;
	}

	public MemberRecyclerAdapter(@Nullable OrderedRealmCollection<PersonDBO> data, OnItemClickListener listener) {
		super(data, true);
		this.mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext())
		.inflate(R.layout.item_edit_group_member, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final PersonDBO person = getItem(position);

		holder.tvName.setText(person.getName());
		if (person.getName().equals(person.getAddress())) {
			holder.tvAddr.setVisibility(View.INVISIBLE);
		}
		else {
			holder.tvAddr.setVisibility(View.VISIBLE);
			holder.tvAddr.setText(person.getAddress());
		}

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onClick(person);
				}
			}
		});
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvName;
		TextView tvAddr;

		public ViewHolder(View itemView) {
			super(itemView);
			tvName = itemView.findViewById(R.id.tv_item_edit_group_member_name);
			tvAddr = itemView.findViewById(R.id.tv_item_edit_group_member_address);
		}
	}

	public interface OnItemClickListener {
		public void onClick(PersonDBO person);
	}
}
