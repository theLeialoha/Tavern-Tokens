package com.traverse.taverntokens.wallet;

import net.minecraft.screen.slot.Slot;

public class WalletSlot extends Slot {
    public WalletSlot(WalletInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override 
    public int getMaxItemCount() {
        return ((WalletInventory) this.inventory).getMaxItemCount();
    }
}
