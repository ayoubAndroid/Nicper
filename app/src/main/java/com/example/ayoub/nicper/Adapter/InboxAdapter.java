package com.example.ayoub.nicper.Adapter;

/**
 * Created by hr2 on 22/06/2016.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.ayoub.nicper.MainActivity.chat.ChatActivity;
import com.example.ayoub.nicper.Object.Message.LastMessage;
import com.example.ayoub.nicper.Object.Message.Time;
import com.example.ayoub.nicper.R;
import com.example.ayoub.nicper.StaticValue;

import java.util.List;


public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder>{
    private List<LastMessage> mDataSet;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;



    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewLastMessage;
        public TextView mTextViewUsername;
        private TextView mTextviewTime;
        private RelativeLayout layout;

        public ViewHolder(View v) {
            super(v);
            mTextViewLastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
            mTextViewUsername = (TextView) itemView.findViewById(R.id.username);
            mTextviewTime = (TextView) itemView.findViewById(R.id.time);
            layout = (RelativeLayout) itemView.findViewById(R.id.listMessageLayout);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device id
     */
    public InboxAdapter(List<LastMessage> dataSet, String id) {
        mDataSet = dataSet;
        mId = id;
    }

    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_RIGHT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_messages, parent, false);
        } else if(viewType == CHAT_LEFT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_messages, parent, false);
        }else {
            return null;
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        LastMessage message  = mDataSet.get(position);
        String temp = message.getUserId();
        if (temp.equals(mId))
            return CHAT_RIGHT;
        else {
            return CHAT_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position < mDataSet.size()) {
            final LastMessage chat = mDataSet.get(position);
            if (chat != null) {
                if (chat.getMessage() != null) {
                    String message  = chat.getMessage();
                    if(message.length() > 100){
                        message = message.substring(0, 100) + " ...";
                    }
                    holder.mTextViewLastMessage.setText(message);
                    holder.mTextViewUsername.setText(chat.getUsername());
                    holder.mTextviewTime.setText(getStringTime(chat.getTime()));
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), ChatActivity.class);
                            intent.putExtra("id", chat.getOtherUserId());
                            intent.putExtra("username", chat.getOtherUsername());
                            view.getContext().startActivity(intent);
                        }
                    });

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private String getStringTime(Time messageTime){
        StaticValue staticValue = new StaticValue();
        Time currentTime = staticValue.getTimeZone();
        if(messageTime.getMonth() != currentTime.getMonth()){
            return (currentTime.getMonth() - messageTime.getMonth())+" month ago";
        }else if(messageTime.getDay() != currentTime.getDay()){
            return (currentTime.getDay() - messageTime.getDay())+" day ago";
        }else if(messageTime.getHour() != currentTime.getHour()){
            return (currentTime.getHour() - messageTime.getHour())+" hour ago";
        }else if(messageTime.getMinute() != currentTime.getMinute()){
            return (currentTime.getMinute() - messageTime.getMinute())+" minute ago";
        }else{
            return "now";
        }
    }
}