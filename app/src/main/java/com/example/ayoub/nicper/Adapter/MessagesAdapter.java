package com.example.ayoub.nicper.Adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayoub.nicper.Object.Message.LastMessage;
import com.example.ayoub.nicper.Object.Message.Message;
import com.example.ayoub.nicper.R;

import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private List<Message> mDataSet;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;
    private Context context;

    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.text_message);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "Geomanist-Regular.otf");
            mTextView.setTypeface(tf);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device id
     */
    public MessagesAdapter(List<Message> dataSet, String id, Context context) {
        mDataSet = dataSet;
        mId = id;
        this.context = context;
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == CHAT_RIGHT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
        } else if (viewType == CHAT_LEFT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_other, parent, false);
        }else{
            v = null;
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        String temp = mDataSet.get(position).getUserId();
        if (temp != null && (mDataSet.get(position).getUserId()).equals(mId)) {
            return CHAT_RIGHT;
        }else{
            return CHAT_LEFT;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position < mDataSet.size()) {
            Message chat = mDataSet.get(position);
            if(chat != null)
                holder.mTextView.setText(chat.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}