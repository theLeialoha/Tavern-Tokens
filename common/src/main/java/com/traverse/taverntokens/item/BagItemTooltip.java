package com.traverse.taverntokens.item;

import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class BagItemTooltip implements TooltipComponent {
    private final NonNullList<WalletItemStack> items;

    public BagItemTooltip(NonNullList<WalletItemStack> nonNullList) {
        this.items = nonNullList;
    }

    public NonNullList<WalletItemStack> getItems() {
        return this.items;
    }
}
