package com.example.user.ohmygod.dataStructure;

import com.example.user.ohmygod.Tools;

import java.util.Comparator;

public class FriendItemComparator implements Comparator <FriendListItem> {
    @Override
    public int compare(FriendListItem lhs, FriendListItem rhs) {
        if (Tools.distanceFromMe(lhs) - Tools.distanceFromMe(rhs) > 0) {
            return -1;
        }
        else if (Tools.distanceFromMe(lhs) - Tools.distanceFromMe(rhs) < 0) {
            return 1;
        }
        else return 0;
    }
}
