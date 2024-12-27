package com.traverse.taverntokens.interfaces;

import com.traverse.taverntokens.wallet.WalletInventory;

public interface PlayerWithBagInventory {

    public WalletInventory walletInventory = null;

    default WalletInventory getWalletInventory() {
        return null;
    }

}
