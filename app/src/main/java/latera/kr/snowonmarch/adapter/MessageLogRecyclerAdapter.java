package latera.kr.snowonmarch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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

		if (msg.isIsent()) {
			holder.tvTextSent.setText(msg.getBody());
			holder.tvTextSent.setVisibility(View.VISIBLE);
			holder.tvTextReceived.setVisibility(View.GONE);
		}
		else {
			holder.tvTextReceived.setText(msg.getBody());
			holder.tvTextReceived.setVisibility(View.VISIBLE);
			holder.tvTextSent.setVisibility(View.GONE);
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		TextView tvTextSent;
		TextView tvTextReceived;

		public ViewHolder(View itemView) {
			super(itemView);

			tvTextSent = itemView.findViewById(R.id.tv_item_message_log_text_sent);
			tvTextReceived = itemView.findViewById(R.id.tv_item_message_log_text_received);
		}
	}
}
