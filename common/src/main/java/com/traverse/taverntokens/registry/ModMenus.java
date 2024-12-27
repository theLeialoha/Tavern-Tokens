package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public abstract class ModMenus {

    public static final MenuType<WalletContainerMenu> WALLET_SCREEN_HANDLER = create(WalletContainerMenu::new);

    public static void register() {
    }

    private static <T extends AbstractContainerMenu> MenuType<T> create(MenuType.MenuSupplier<T> supplier) {
        return new MenuType<T>(supplier, FeatureFlags.DEFAULT_FLAGS);
    }
}