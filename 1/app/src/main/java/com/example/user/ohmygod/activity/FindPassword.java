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


public class FindPassword extends Activity {

    EditText mE_Mail;
    EditText mVcode;

    /**
     * Server returns JsonObj: {return_code: "xxx"}
     * return_code = 4000: get Vcode successfully
     * return_code = 4001: get Vcode unsuccessfully
     */
    Response.Listener<JSONObject> onGetVcodeListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("Register.onGetVcodeListener", response.toString());
            try {
                if (response.getInt("return_code") == 4000) {
                    Toast.makeText(FindPassword.this, "验证码已发送至邮箱: \n" + mE_Mail.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else if (response.getInt("return_code") == 4001) {
                    Toast.makeText(FindPassword.this, "验证码已发送至邮箱: \n" + mE_Mail.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Server returns JsonObj: {return_code: "xxx"}
     * ret = 5000: verification successful
     * ret = 5001: unsuccessful
     */
    Response.Listener<JSONObject> onVerifyListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("Register.onVerifyListener", response.toString());

            try {
                int retValue = response.getInt("return_code");
                if (retValue == 5000) {
                    Toast.makeText(FindPassword.this, "验证成功", Toast.LENGTH_SHORT).show();
                    LocalData.ID = mE_Mail.getText().toString();
                    Intent intent = new Intent(FindPassword.this, SetPassword.class);
                    intent.putExtra("isFind", true);
                    startActivity(intent);
                }
                else if (retValue == 5001) {
                    Toast.makeText(FindPassword.this, "验证不成功", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(FindPassword.this, "网络连接失败", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onClickVerifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String emailStr = mE_Mail.getText().toString();
            String vcodeStr = mVcode.getText().toString();

            if (emailStr.length() == 0) {
                Toast.makeText(FindPassword.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                return;
            }
            if (vcodeStr.length() == 0) {
                Toast.makeText(FindPassword.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * Sent JsonObj format: {id: "xxx", "verify": "xxx"}
             */
            JSONObject jObj = new JSONObject();
            try {
                jObj.put("id", emailStr).put("verify", vcodeStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jreg = new JsonObjectRequest(RequestedUrl.verifyMailUrl_findPw(), jObj, onVerifyListener, onErrorListener);
            VolleySingleton.getInstance(FindPassword.this).addToRequestQueue(jreg);
        }
    };

    View.OnClickListener onClickGetVcodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = mE_Mail.getText().toString();
            if (email.length() == 0) {
                Toast.makeText(FindPassword.this, "请输入注册邮箱", Toast.LENGTH_SHORT).show();
            }
            else {
                /**
                 * Sent JsonObj format: {id: "xxx"}
                 */
                JSONObject jObj = new JSONObject();
                try {
                    jObj.put("id", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jreg = new JsonObjectRequest(RequestedUrl.getVcodeUrl_findPw(), jObj, onGetVcodeListener, onErrorListener);
                VolleySingleton.getInstance(FindPassword.this).addToRequestQueue(jreg);
            }
        }
    };

    private void makeView() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // TextView tv;
        // tv = (TextView) findViewById(R.id.action);
        // tv.setText("找回");

        mE_Mail = (EditText)findViewById(R.id.findPw_ET_mail);
        mVcode = (EditText)findViewById(R.id.findPw_ET_vcode);

        Button btnConfirm ,btnBack, btnGetVcode;
        btnConfirm = (Button) findViewById(R.id.find_confirm);
        btnGetVcode = (Button) findViewById(R.id.findPw_btn_getVcode);

        btnGetVcode.setOnClickListener(onClickGetVcodeListener);
        btnConfirm.setOnClickListener(onClickVerifyListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        // setCustomActionBar();
        makeView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.find_password, menu);
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
