package latera.kr.snowonmarch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.dbo.MessageDBO;

public class MessageLogRecyclerAdapter extends RealmRecyclerViewAdapter<MessageDBO, MessageLogRecyclerAdapter.ViewHolder> {

	public MessageLogRecyclerAdapter(OrderedRealmCollection<MessageDBO> data) {
		super(data, true);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_log, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final MessageDBO msg = getItem(position);
		holder.tvText.setText(msg.getBody());

		if (msg.isIsent()) {
			holder.tvText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
		}
		else {
			holder.tvText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvText;

		public ViewHolder(View itemView) {
			super(itemView);

			tvText = itemView.findViewById(R.id.tv_item_message_log_text);
		}
	}
}
