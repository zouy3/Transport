package com.example.user.ohmygod.viewContainer;

import android.view.View;
import android.widget.Button;

import com.example.user.ohmygod.R;
import com.example.user.ohmygod.dataStructure.MessageListItem;

/**
 * Created by zhang_000 on 2015/5/2.
 */
public class MessageListItemContainer extends BaseContainer {
    private Button mBtnAccept;
    private Button mBtnReject;

    @Override
    public void bindView(View rootView) {
        super.bindView(rootView);
        mBtnAccept = (Button)rootView.findViewById(R.id.message_list_item_btn_accept);
        mBtnReject = (Button)rootView.findViewById(R.id.message_list_item_btn_reject);
    }

    public void setData(MessageListItem messageListItem) {
        mEmail.setText(messageListItem.getEmail());
    }

    public void setButtonTag(int index) {
        mBtnAccept.setTag(index);
        mBtnReject.setTag(index);
    }

    public void setListener(View.OnClickListener onClickListener) {
        mBtnAccept.setOnClickListener(onClickListener);
        mBtnReject.setOnClickListener(onClickListener);
    }
}
