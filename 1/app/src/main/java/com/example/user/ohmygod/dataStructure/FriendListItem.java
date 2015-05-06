package com.example.user.ohmygod.dataStructure;

/**
 * Created by zhang_000 on 2015/5/2.
 */
public class FriendListItem extends BaseItem{
    private double mLongitude;
    private double mLatitude;
    private String mIP;

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLongitude(double x) {
        mLongitude = x;
    }

    public void setLatitude(double x) {
        mLatitude = x;
    }

    public void setIP(String IP) {
        mIP = IP;
    }

    public String getIP() {
        return mIP;
    }

}
