package com.example.user.ohmygod.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.ohmygod.FileTransport.Reciver;
import com.example.user.ohmygod.FileTransport.Sender;
import com.example.user.ohmygod.LocalData;
import com.example.user.ohmygod.adapter.FriendListAdapter;
import com.example.user.ohmygod.fragment.FriendFragment;
import com.example.user.ohmygod.fragment.MessageFragment;
import com.example.user.ohmygod.fragment.ProgressFragment;
import com.example.user.ohmygod.R;
import com.example.user.ohmygod.netservice.ReceivePacket;
import com.example.user.ohmygod.netservice.TCPReceive;
import com.example.user.ohmygod.netservice.UdpReceiveAndtcpSend;

import org.json.JSONException;
import org.json.JSONObject;


public class Main extends Activity implements
        FriendFragment.Callbacks, ProgressFragment.Callbacks,
        MessageFragment.Callbacks, FriendListAdapter.ClickTransferButton {

    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    public static final int REFRESH_REQUEST = 100;

    private int mFileTransferDestPos;

    Button mBtnFriendList;
    Button mBtnProgressList;
    Button mBtnMessageList;

    FriendFragment friendFragment;
    ProgressFragment progressFragment;
    MessageFragment messageFragment;

    String mReplyData;

    ReceivePacket onReceivePacket = new ReceivePacket() {
        @Override
        public void onResponse(String sourceIP, String data) {
            String id;
            double longitude;
            double latitude;
            try {
                JSONObject jObj = new JSONObject(data);
                id = jObj.getString("id");
                longitude = jObj.getDouble("longitude");
                latitude = jObj.getDouble("latitude");
            } catch (JSONException e) {
                e.printStackTrace();
                // error: discard this packet
                return;
            }
            Log.i("Main:onReceivePacket", sourceIP + ' ' + data);
            LocalData.insertFriend(id, longitude, latitude, sourceIP);
            handler.sendEmptyMessage(0);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            friendFragment.getAdapter().notifyDataSetChanged();
        }
    };

    private void switchFriend() {
        mBtnFriendList.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        mBtnFriendList.setTextColor(getResources().getColor(android.R.color.background_dark));
        mBtnProgressList.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        mBtnProgressList.setTextColor(getResources().getColor(android.R.color.white));
        mBtnMessageList.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        mBtnMessageList.setTextColor(getResources().getColor(android.R.color.white));
                /* 跳转 friend friendFragment */
        if (friendFragment == null) {
            friendFragment = new FriendFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.frame, friendFragment).commit();
    }

    private void switchProgress() {
        mBtnFriendList.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        mBtnFriendList.setTextColor(getResources().getColor(android.R.color.white));
        mBtnProgressList.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        mBtnProgressList.setTextColor(getResources().getColor(android.R.color.background_dark));
        mBtnMessageList.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        mBtnMessageList.setTextColor(getResources().getColor(android.R.color.white));
                /* 跳转 progress friendFragment */
        if (progressFragment == null) {
            progressFragment = new ProgressFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.frame, progressFragment).commit();
    }

    private void switchMessage() {
        mBtnFriendList.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        mBtnFriendList.setTextColor(getResources().getColor(android.R.color.white));
        mBtnProgressList.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        mBtnProgressList.setTextColor(getResources().getColor(android.R.color.white));
        mBtnMessageList.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        mBtnMessageList.setTextColor(getResources().getColor(android.R.color.background_dark));
                /* 跳转 message friendFragment */
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.frame, messageFragment).commit();
    }

    private void makeView() {
        /* initialize the three button */
        mBtnFriendList = (Button) findViewById(R.id.friend);
        mBtnProgressList = (Button) findViewById(R.id.progress);
        mBtnMessageList = (Button) findViewById(R.id.message);

        //好友 button 监听器
        mBtnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFriend();
            }
        });

        //进度 button 监听器
        mBtnProgressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchProgress();
            }
        });

        //消息button 监听器
        mBtnMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMessage();
            }
        });

        switchFriend();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeView();

        JSONObject jObj = new JSONObject();
        try {
            // TODO: to debug, we use ID + time as ID.
            jObj.put("id", LocalData.ID + System.currentTimeMillis()).put("longitude", LocalData.longitude).put("latitude", LocalData.latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mReplyData = jObj.toString();

        new UdpReceiveAndtcpSend(onReceivePacket, mReplyData).start();
        new TCPReceive(onReceivePacket).start();
        startListen();

    }

    private void startListen()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Reciver reciver = new Reciver(Main.this);
                reciver.listen();
            }
        }).start();
    }

    @Override
    public void onTransferButtonClick(int position) {
        mFileTransferDestPos = position;
        // TODO: open external storage
        Toast.makeText(this, "SendFile:" + String.valueOf(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onFriendItemSelected(Integer id) {
        // TODO: do nothing now
    }

    @Override
    public void onProgressItemSelected(Integer id) {
        // TODO: pause or resume
    }

    @Override
    public void onMessageItemSelected(Integer id) {
        // TODO: do nothing now
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // TODO: handle file transfer

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.addFriend) {
            Intent intent = new Intent(this, AddFriendActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_refresh) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("sentData", mReplyData);
            startActivityForResult(intent, REFRESH_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REFRESH_REQUEST:
                Toast.makeText(this, "Search Complete", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    // data: uri
                    if (data == null) {
                        Toast.makeText(this, "error while selecting file", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Uri uri = data.getData();
           //         Log.i("path", photoUri.toString());
   //                 Log.i("path", photoUri.getPath());

                    final String picPath = uri.getPath();
     /*               String [] pojo = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(photoUri, pojo, null, null, null);
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    Log.i("index", columnIndex + "");
                    Log.i("cursor", cursor.toString());
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    cursor.close();*/
                    // TODO: picPath is the path of selected image
                    // TODO: start transfer
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Sender sender = new Sender(Main.this);
                            sender.send(LocalData.friendListItems.get(mFileTransferDestPos).getIP(), picPath);
                        }
                    }).start();
                    Toast.makeText(this, mFileTransferDestPos + '\n' + picPath + '\n' + LocalData.friendListItems.get(mFileTransferDestPos).getIP(), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
