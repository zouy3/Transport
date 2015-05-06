package com.example.user.ohmygod.viewContainer;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.ohmygod.R;

/**
 * Created by zhang_000 on 2015/5/2.
 */
public class ProgressListItemContainer extends BaseContainer {

    TextView mTVfilename;
    ProgressBar mPBtransferProgress;

    @Override
    public void bindView(View rootView) {
        super.bindView(rootView);
        mTVfilename = (TextView)rootView.findViewById(R.id.progress_item_filename);
        mPBtransferProgress = (ProgressBar)rootView.findViewById(R.id.progress_item_progress);
    }
}
