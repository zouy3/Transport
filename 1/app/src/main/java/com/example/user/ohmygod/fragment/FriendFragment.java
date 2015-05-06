package com.example.user.ohmygod.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.user.ohmygod.R;
import com.example.user.ohmygod.adapter.FriendListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendFragment extends ListFragment {

    private Callbacks mCallbacks;
    private BaseAdapter mAdapter;

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public interface Callbacks
    {
        public void onFriendItemSelected(Integer id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAdapter = new FriendListAdapter(getActivity());
        setListAdapter(mAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ProgressFragment.Callbacks))
        {
            throw new IllegalStateException("FriendFragment" +
                    "所在的Activity必须实现Callbacks");
        }
        mCallbacks=(Callbacks)activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallbacks=null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        super.onListItemClick(listView,view,position,id);
        mCallbacks.onFriendItemSelected(position);
    }

    public void setActivateOnItemClick(boolean activateOnItemClick)
    {
        getListView().setChoiceMode (
                activateOnItemClick?ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE );
    }
}