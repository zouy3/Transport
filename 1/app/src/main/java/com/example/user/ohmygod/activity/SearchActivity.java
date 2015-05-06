package com.example.user.ohmygod.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

import com.example.user.ohmygod.netservice.UDPbroadCast;
import com.example.user.ohmygod.view.SectorView;

public class SearchActivity extends Activity{

    UDPbroadCast.UDPbroadcastResponse onUDPbcRes = new UDPbroadCast.UDPbroadcastResponse() {
        @Override
        public void onResponse() {
            setResult(Main.REFRESH_REQUEST);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SectorView sectorView;
        sectorView = new SectorView(this);
        setContentView(sectorView);

        new UDPbroadCast(onUDPbcRes, getIntent().getStringExtra("sentData")).start();
    }
}
