package com.example.user.ohmygod;

import android.util.Log;

import com.example.user.ohmygod.dataStructure.FriendItemComparator;
import com.example.user.ohmygod.dataStructure.FriendListItem;
import com.example.user.ohmygod.dataStructure.MessageListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhang_000 on 2015/4/29.
 */
public class LocalData {
    public static String ID = "zhangqichen1@163.com";
    public static String password;
    public static double longitude = 0;
    public static double latitude = 0;

    public static Set<String> MyFriendSet = new HashSet<String>();
    public static Map<String, FriendListItem> MyFriendOnLine = new HashMap<String, FriendListItem>();
    public static ArrayList<FriendListItem> friendListItems = new ArrayList<FriendListItem>();
    public static ArrayList<MessageListItem> messageListItems = new ArrayList<MessageListItem>();

    private static void sortFriendList() {
        Collections.sort(friendListItems, new FriendItemComparator());
    }

    public static void insertFriend(String id, double longitude, double latitude, String IP) {
        // TODO: judge whether id is a friend
        // insert into or modify localData
        Log.i("LocalData:insertFriend", id + ' ' + IP);
        FriendListItem friendListItem;
        boolean isContained = MyFriendOnLine.containsKey(id);
        if (isContained) {
            friendListItem = LocalData.MyFriendOnLine.get(id);
        }
        else {
            MyFriendOnLine.put(id, new FriendListItem());
            friendListItem = LocalData.MyFriendOnLine.get(id);
            friendListItem.setEmail(id);
        }
        friendListItem.setIP(IP);
        friendListItem.setLongitude(longitude);
        friendListItem.setLatitude(latitude);
        if (!isContained)
            friendListItems.add(friendListItem);
        sortFriendList();
    }

    /*
    // initialize
    static {
        for (int i = 0; i < 10; ++i) {
            FriendListItem friendListItem = new FriendListItem();
            friendListItem.setLatitude(0);
            friendListItem.setLongitude(0);
            friendListItem.setEmail("zhangqichen1@163.com");
            friendListItems.add(friendListItem);

            MessageListItem messageListItem = new MessageListItem();
            messageListItem.setEmail("zhangqichen1@163.com");
            messageListItem.setGreeting("Hello");
            messageListItems.add(messageListItem);
        }
    }
    */
}
