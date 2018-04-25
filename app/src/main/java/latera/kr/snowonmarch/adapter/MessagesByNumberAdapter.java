package latera.kr.snowonmarch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import latera.kr.snowonmarch.R;
import latera.kr.snowonmarch.object.TextMessageObject;

/**
 * Created by Jinwoo Shin on 2018-04-20.
 */

public class MessagesByNumberAdapter extends BaseAdapter {

    private static final int SIZE = 24;
    private Context mContext;
    private TextMessageObject mMessage;

    public MessagesByNumberAdapter(Context context) {
        mContext = context;
        mMessage = new TextMessageObject("Secondary line text Lorem ipsum dapibus, neque id cursus",
                "John Doe", "012 3456 7890");
    }


    @Override
    public int getCount() {
        return this.SIZE;
    }

    @Override
    public Object getItem(int i) {
        return mMessage;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_message_by_number, null);
            holder.ivProfile = view.findViewById(R.id.iv_item_messages_by_number_sender_img);
            holder.tvSender = view.findViewById(R.id.tv_item_message_by_number_sender_name);
            holder.tvMessage = view.findViewById(R.id.tv_item_message_by_number_sender_excerpt);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        holder.tvSender.setText(mMessage.senderName);
        holder.tvMessage.setText(mMessage.content);

        return view;
    }

    private class ViewHolder {
        public ImageView ivProfile;
        public TextView tvSender;
        public TextView tvMessage;
    }
}
