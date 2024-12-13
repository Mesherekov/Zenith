package com.example.zenith.OnClickListeners;

import com.example.zenith.Item;

@FunctionalInterface
public interface SelectListener {
    void onItemClick(Item item, int position);
}
