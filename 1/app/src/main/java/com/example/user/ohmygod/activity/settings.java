package com.example.user.ohmygod.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.ohmygod.R;


public class settings extends Activity {

    ActionBar actionBar;
    View customView;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //set actionBar to the center
        /*
        actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        customView = getLayoutInflater().inflate(R.layout.actionbar_login,null);

        actionBar.setCustomView(customView,new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT));

        ActionBar.LayoutParams mp = (ActionBar.LayoutParams)customView.getLayoutParams();
        //mp.gravity=mp.gravity &~Gravity.HORIZONTAL_GRAVITY_MASK;
        mp.gravity=mp.gravity &~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;

        actionBar.setCustomView(customView,mp);

        tv = (TextView) findViewById(R.id.action);
        tv.setText("设置");
        */

        Intent intent=getIntent();
        String NAME=intent.getStringExtra("name").toString();
        ((TextView)findViewById(R.id.username)).setText(NAME);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
