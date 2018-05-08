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
import latera.kr.snowonmarch.view.CircleButtonView;

public class GroupRecyclerAdapter extends RealmRecyclerViewAdapter<GroupDBO, GroupRecyclerAdapter.ViewHolder> {

	private final OnItemClickListener mListener;

	public GroupRecyclerAdapter(@Nullable OrderedRealmCollection<GroupDBO> data, OnItemClickListener listener) {
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

		holder.tvGroupName.setText(group.getName());
		holder.cbvBg.setColor(group.getBackground());
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
		CircleButtonView cbvBg;
		TextView tvGroupName;

		public ViewHolder(View itemView) {
			super(itemView);
			tvGroupName = itemView.findViewById(R.id.tv_item_group_list_group_name);
			cbvBg = itemView.findViewById(R.id.cbv_item_group_list_color);
		}
	}

	public interface OnItemClickListener {
		public void onItemClick(GroupDBO group);
	}
}
