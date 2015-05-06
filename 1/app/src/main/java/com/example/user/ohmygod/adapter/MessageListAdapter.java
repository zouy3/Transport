package com.example.user.ohmygod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.user.ohmygod.LocalData;
import com.example.user.ohmygod.R;
import com.example.user.ohmygod.viewContainer.MessageListItemContainer;

public class MessageListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context mContext;

    public MessageListAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return LocalData.messageListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return LocalData.messageListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: network
    private View.OnClickListener onCLickListener = new View.OnClickListener() {
        // TODO: handle click
        @Override
        public void onClick(View v) {
            int tag = (Integer)v.getTag();
            switch (v.getId()) {
                case R.id.message_list_item_btn_accept:
                    Toast.makeText(mContext, "AC:" + String.valueOf(tag), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.message_list_item_btn_reject:
                    Toast.makeText(mContext, "RJ:" + String.valueOf(tag), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageListItemContainer messageListItemContainer;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_item, parent, false);
            messageListItemContainer = new MessageListItemContainer();
            messageListItemContainer.bindView(convertView);
            messageListItemContainer.setData(LocalData.messageListItems.get(position));
            messageListItemContainer.setButtonTag(position);
            messageListItemContainer.setListener(onCLickListener);
            convertView.setTag(messageListItemContainer);
        }
        else {
            messageListItemContainer = (MessageListItemContainer)convertView.getTag();
            messageListItemContainer.setData(LocalData.messageListItems.get(position));
            messageListItemContainer.setListener(onCLickListener);
            messageListItemContainer.setButtonTag(position);
        }
        return convertView;
    }
}
