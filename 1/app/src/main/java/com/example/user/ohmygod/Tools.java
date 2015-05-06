package com.example.user.ohmygod;

import com.example.user.ohmygod.dataStructure.FriendListItem;

/**
 * Created by zhang_000 on 2015/5/2.
 */
public class Tools {
    public static double distance(FriendListItem a, FriendListItem b) {
        return distance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static double distanceFromMe(FriendListItem a) {
        return distance(a.getLatitude(), a.getLongitude(), LocalData.latitude, LocalData.longitude);
    }
}
