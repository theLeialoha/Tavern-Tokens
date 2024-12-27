package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.screens.WalletScreen;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public abstract class FabricModMenus extends ModMenus {

    public static void register() {
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(TavernTokens.MODID, "wallet_screen"),
                WALLET_SCREEN_HANDLER);

        MenuScreens.<WalletContainerMenu, WalletScreen>register(WALLET_SCREEN_HANDLER,
                (gui, inventory, title) -> new WalletScreen(gui, inventory, title));
    }
}