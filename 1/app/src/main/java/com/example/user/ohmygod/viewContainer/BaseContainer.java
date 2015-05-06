package com.example.user.ohmygod.viewContainer;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.user.ohmygod.R;
import com.example.user.ohmygod.dataStructure.BaseItem;

/**
 * Created by zhang_000 on 2015/5/2.
 */
public abstract class BaseContainer {
    protected TextView mEmail;
    protected Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    public void bindView(View rootView) {
        mEmail = (TextView)rootView.findViewById(R.id.email_address);
    }
}
