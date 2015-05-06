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
import com.example.user.ohmygod.R;
import com.example.user.ohmygod.VolleySingleton;
import com.example.user.ohmygod.netservice.RequestedUrl;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity {

    View.OnClickListener onRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onForgetPwClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Login.this, FindPassword.class);
            startActivity(intent);
        }
    };

    /**
     * Server return JsonObj: {ret : "xxx"}
     * ret = 0: LogIn successful
     * ret = 1: Wrong password
     * ret = 2: Account not exists
     */
    Response.Listener<JSONObject> onResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("LogIn.java:onResponseListener", response.toString());
            try {
                if (response.getInt("ret") == 0) {
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);
                    finish();
                }

                else if (response.getInt("ret") == 1) {
                    Toast.makeText(Login.this, "密码错误", Toast.LENGTH_SHORT).show();
                }

                else if (response.getInt("ret") == 2) {
                    Toast.makeText(Login.this, "账号不存在", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Toast.makeText(Login.this, "网络连接失败", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onLogInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s1,s2;
            EditText PHONE=(EditText) findViewById(R.id.phone);
            s1= PHONE.getText().toString();

            EditText PASSWD=(EditText) findViewById(R.id.passwd);
            s2= PASSWD.getText().toString();

            if (s1.length() == 0) {
                Toast.makeText(Login.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (s2.length() == 0) {
                Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * Sent JsonObj format: {ID: "xxx", pw: "xxx"}
             */
            JSONObject jreq = new JSONObject();
            try {
                jreq.put("ID", s1).put("pw", s2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            VolleySingleton.getInstance(Login.this).addToRequestQueue(
                    new JsonObjectRequest(RequestedUrl.logInUrl(), jreq, onResponseListener, onErrorListener));
        }
    };

    private void viewMaker() {
        //Bind two button with listener and jump to Activity reset
        Button res, forgot, Login;
        res = (Button) findViewById(R.id.res);
        forgot = (Button) findViewById(R.id.forgot);
        Login = (Button) findViewById(R.id.login);
        // tv = (TextView) findViewById(R.id.action);
        // tv.setText("登陆");
        res.setOnClickListener(onRegisterClickListener);
        forgot.setOnClickListener(onForgetPwClickListener);
        Login.setOnClickListener(onLogInClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewMaker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.login, menu);
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
