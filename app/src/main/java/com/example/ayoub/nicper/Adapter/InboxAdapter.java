package com.example.ayoub.nicper.Adapter;

/**
 * Created by hr2 on 22/06/2016.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.ayoub.nicper.MainActivity.chat_message.ChatActivity;
import com.example.ayoub.nicper.MainActivity.chat_message.ListMessage;
import com.example.ayoub.nicper.Object.Message.LastMessage;
import com.example.ayoub.nicper.Object.Message.Message;
import com.example.ayoub.nicper.R;

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
        private RippleView rippleLayoutListMessage;

        public ViewHolder(View v) {
            super(v);
            mTextViewLastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
            mTextViewUsername = (TextView) itemView.findViewById(R.id.username);
            rippleLayoutListMessage = (RippleView) itemView.findViewById(R.id.rippleLayoutListMessage);
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
        final LastMessage chat = mDataSet.get(position);
        if(chat != null) {
            if (chat.getMessage() != null) {
                holder.mTextViewLastMessage.setText(chat.getMessage());
                holder.mTextViewUsername.setText(chat.getUsername());
                holder.rippleLayoutListMessage.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent intent = new Intent(rippleView.getContext(), ChatActivity.class);
                        intent.putExtra("id", chat.getOtherUserId());
                        intent.putExtra("username", chat.getOtherUsername());
                        rippleView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}