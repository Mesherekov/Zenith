package com.example.zenith.OnClickListeners;

import com.example.zenith.ItemFriends;

@FunctionalInterface
public interface SelectFriendsListener {
    void onItemFriendClick(ItemFriends item, int position);
}
