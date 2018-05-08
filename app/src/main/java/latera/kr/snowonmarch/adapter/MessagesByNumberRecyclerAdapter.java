package latera.kr.snowonmarch.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;
import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.dbo.MessageDBO;
import latera.kr.snowonmarch.dbo.PersonDBO;

/**
 * Created by Jinwoo Shin on 2018-04-20.
 */

public class MessagesByNumberRecyclerAdapter extends RealmRecyclerViewAdapter<PersonDBO, MessagesByNumberRecyclerAdapter.ViewHolder> {

	private static final String TAG = "MAIN_RECYCLERVIEW";

	private final OnItemClickListener mListener;

    public MessagesByNumberRecyclerAdapter(OrderedRealmCollection<PersonDBO> data, OnItemClickListener listener) {
    	super(data.sort("recent", Sort.DESCENDING), true);
	    Log.d(TAG, "items: " + data.size());
	    mListener = listener;
    }

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	View itemView = LayoutInflater.from(parent.getContext())
			    .inflate(R.layout.item_message_by_number, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final PersonDBO item = getItem(position);
		holder.person = item;
		holder.tvSender.setText(item.getName());
		MessageDBO recent = item.getRecentMessage();
		if (recent != null) {
			holder.tvMessage.setText(recent.getBody());
		}
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mListener.onClick(view, item);
			}
		});
	}

	@Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfile;
        TextView tvSender;
        TextView tvMessage;
        PersonDBO person;

	    public ViewHolder(final View itemView) {
		    super(itemView);
		    ivProfile = itemView.findViewById(R.id.iv_item_messages_by_number_sender_img);
		    tvSender = itemView.findViewById(R.id.tv_item_message_by_number_sender_name);
		    tvMessage = itemView.findViewById(R.id.tv_item_message_by_number_sender_excerpt);
	    }
    }

    public interface OnItemClickListener {
    	public void onClick(View v, PersonDBO p);
    }
}
