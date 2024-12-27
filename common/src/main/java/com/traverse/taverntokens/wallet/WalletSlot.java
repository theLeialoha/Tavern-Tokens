package com.traverse.taverntokens.wallet;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class WalletSlot extends Slot {
    public WalletSlot(WalletInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return ((WalletInventory) this.container).getMaxStackSize();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ((WalletInventory) this.container).isValidItem(stack) && this.isHighlightable();
    }

    @Override
    public boolean isHighlightable() {
        return ((WalletInventory) this.container).isHighlightable(this.index);
    }
}
