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

public class EditGroupAdapter extends RealmRecyclerViewAdapter<GroupDBO, EditGroupAdapter.ViewHolder> {

	private final OnItemClickListener mListener;

	public EditGroupAdapter(@Nullable OrderedRealmCollection<GroupDBO> data, OnItemClickListener listener) {
		super(data, true);
		this.mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_edit_group_list, parent, false));

		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final GroupDBO item = getItem(position);

		holder.tvGroupName.setText(item.getName());

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onClick(item);
				}
			}
		});
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvGroupName;

		public ViewHolder(View itemView) {
			super(itemView);
			tvGroupName = itemView.findViewById(R.id.tv_edit_group_list_group_title);
		}
	}

	public interface OnItemClickListener {
		public void onClick(GroupDBO group);
	}
}
