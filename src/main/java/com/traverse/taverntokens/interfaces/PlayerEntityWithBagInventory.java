package com.traverse.taverntokens.interfaces;

import com.traverse.taverntokens.wallet.WalletInventory;

public interface PlayerEntityWithBagInventory {

    default WalletInventory getWalletInventory() {
        return null;
    }

}
