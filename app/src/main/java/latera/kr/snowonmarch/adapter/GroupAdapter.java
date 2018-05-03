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

public class GroupAdapter extends RealmRecyclerViewAdapter<GroupDBO, GroupAdapter.ViewHolder> {

	private final OnItemClickListener mListener;

	public GroupAdapter(@Nullable OrderedRealmCollection<GroupDBO> data, OnItemClickListener listener) {
		super(data, true);
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false);
		ViewHolder holder = new ViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final GroupDBO group = getItem(position);
		holder.group = group;
		holder.tvGroupName.setText(group.getName());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onItemClick(group);
				}
			}
		});
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		GroupDBO group;
		TextView tvGroupName;

		public ViewHolder(View itemView) {
			super(itemView);
			tvGroupName = itemView.findViewById(R.id.tv_item_group_list_group_name);
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(GroupDBO group);
	}
}
