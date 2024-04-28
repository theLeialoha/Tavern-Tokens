package com.traverse.taverntokens.interfaces;

import com.traverse.taverntokens.util.WalletInventory;

public interface PlayerEntityWithBagInventory {

    default WalletInventory getWalletInventory() {
        return null;
    }

}
