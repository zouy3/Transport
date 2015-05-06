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
import com.example.user.ohmygod.viewContainer.ProgressListItemContainer;

/**
 * Created by zhang_000 on 2015/5/5.
 */
public class ProgressListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;

    public ProgressListAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProgressListItemContainer progressListItemContainer;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.progress_item, parent, false);
            progressListItemContainer = new ProgressListItemContainer();
            progressListItemContainer.bindView(convertView);
            convertView.setTag(progressListItemContainer);
        }
        else {
            progressListItemContainer = (ProgressListItemContainer)convertView.getTag();
        }
        return convertView;
    }
}
