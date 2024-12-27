package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.TavernTokens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class WalletScreenMenuProvider implements MenuProvider {

    @Override
    public WalletContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new WalletContainerMenu(syncId, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen." + TavernTokens.MODID + ".wallet");
    }
}