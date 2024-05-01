package com.traverse.taverntokens.client.screens;

import com.traverse.taverntokens.screens.WalletScreenHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class WalletScreenHandlerFactory implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.literal("Wallet");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new WalletScreenHandler(syncId, inv);
    }
}