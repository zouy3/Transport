package com.example.user.ohmygod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.user.ohmygod.LocalData;
import com.example.user.ohmygod.R;
import com.example.user.ohmygod.VolleySingleton;
import com.example.user.ohmygod.netservice.RequestedUrl;

import org.json.JSONException;
import org.json.JSONObject;


public class SetPassword extends Activity {
    EditText mPw;
    EditText mPwRepeat;
    private boolean isFind = false;

    private void viewMaker() {

        mPw = (EditText)findViewById(R.id.setPw_ET_pw);
        mPwRepeat = (EditText)findViewById(R.id.setPw_ET_pw_repeat);

        Button confirm = (Button)findViewById(R.id.setPw_btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String pwStr = mPw.getText().toString();
            String pwRepStr = mPwRepeat.getText().toString();
            if (pwStr.length() == 0 || pwRepStr.length() == 0) {
                Toast.makeText(SetPassword.this, "密码以及确认密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (!pwStr.equals(pwRepStr)) {
                Toast.makeText(SetPassword.this, "密码以和确认密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jObj = new JSONObject();
            try {
                jObj.put("id", LocalData.ID).put("password", pwStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jreg = new JsonObjectRequest(isFind ? RequestedUrl.setPwUrl_findPw() : RequestedUrl.setPwUrl_reg(),
                    jObj, onResponseListener, onErrorListener);
            VolleySingleton.getInstance(SetPassword.this).addToRequestQueue(jreg);
            }
        });
    }

    Response.Listener<JSONObject> onResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("SetPw.onResponseListener", response.toString());
            try {
                int retValue = response.getInt("return_code");
                if (retValue == 3000 || retValue == 6000) {
                    Toast.makeText(SetPassword.this, "设置成功", Toast.LENGTH_SHORT).show();
                    LocalData.password = mPw.getText().toString();
                    Intent intent = new Intent(SetPassword.this, Main.class);
                    startActivity(intent);
                    finish();
                }
                else if (retValue == 3001 || retValue == 6001) {
                    Toast.makeText(SetPassword.this, "密码设置不成功，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                else throw new Exception("unrecognized ret value");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Toast.makeText(SetPassword.this, "网络连接失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        viewMaker();
        isFind = getIntent().getBooleanExtra("isFind", false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.reset, menu);
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
        else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
