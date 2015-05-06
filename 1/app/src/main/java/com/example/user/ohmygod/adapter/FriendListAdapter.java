package com.example.user.ohmygod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.user.ohmygod.LocalData;
import com.example.user.ohmygod.R;
import com.example.user.ohmygod.viewContainer.FriendListItemContainer;

public class FriendListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public FriendListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public interface ClickTransferButton {
        public void onTransferButtonClick(int position);
    }

    @Override
    public int getCount() {
        return LocalData.friendListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return LocalData.friendListItems.get(position);
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
                case R.id.friend_list_item_btn_send_file:
                    // TODO: open external storage
                    ((ClickTransferButton)mContext).onTransferButtonClick(tag);
                    break;
                case R.id.friend_list_item_btn_delete_friend:
                    Toast.makeText(mContext, "DeleteFriend:" + String.valueOf(tag), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendListItemContainer friendListItemContainer;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.friend_item, parent, false);
            friendListItemContainer = new FriendListItemContainer();
            friendListItemContainer.bindView(convertView);
            friendListItemContainer.setData(LocalData.friendListItems.get(position));
            friendListItemContainer.setButtonTag(position);
            friendListItemContainer.setListener(onCLickListener);
            convertView.setTag(friendListItemContainer);
        }
        else {
            friendListItemContainer = (FriendListItemContainer)convertView.getTag();
            friendListItemContainer.setData(LocalData.friendListItems.get(position));
            friendListItemContainer.setButtonTag(position);
            friendListItemContainer.setListener(onCLickListener);
        }
        return convertView;
    }
}
