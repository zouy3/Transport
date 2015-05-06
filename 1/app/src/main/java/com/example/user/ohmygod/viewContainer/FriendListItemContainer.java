package com.example.user.ohmygod.viewContainer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.ohmygod.R;
import com.example.user.ohmygod.Tools;
import com.example.user.ohmygod.dataStructure.FriendListItem;

public class FriendListItemContainer extends BaseContainer {
    protected TextView mDistance;
    protected Button mBtnSendFile;
    protected Button mBtnDeleteFriend;

    @Override
    public void bindView(View rootView) {
        super.bindView(rootView);
        mDistance = (TextView)rootView.findViewById(R.id.friend_list_item_distance);
        mBtnSendFile = (Button)rootView.findViewById(R.id.friend_list_item_btn_send_file);
        mBtnDeleteFriend = (Button)rootView.findViewById(R.id.friend_list_item_btn_delete_friend);
    }

    public void setData(FriendListItem friendListItem) {
        // TODO: get distance. BaiduSDK
        mDistance.setText("0");
        mEmail.setText(friendListItem.getEmail());
    }

    public void setButtonTag(int index) {
        mBtnSendFile.setTag(index);
        mBtnDeleteFriend.setTag(index);
    }

    public void setListener(View.OnClickListener onClickListener) {
        mBtnSendFile.setOnClickListener(onClickListener);
        mBtnDeleteFriend.setOnClickListener(onClickListener);
    }
}
