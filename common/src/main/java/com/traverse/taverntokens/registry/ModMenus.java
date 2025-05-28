package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.utils.registry.ModRegistrationProvider;
import com.traverse.taverntokens.utils.registry.ModRegistryObject;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
    public static final ModRegistrationProvider<MenuType<?>> MENUS = ModRegistrationProvider.of(BuiltInRegistries.MENU, TavernTokens.MODID);

    public static final ModRegistryObject<MenuType<WalletContainerMenu>> WALLET_SCREEN_HANDLER = MENUS.register("wallet_screen_handler", create(WalletContainerMenu::new));

    private static <T extends AbstractContainerMenu> MenuType<T> create(MenuType.MenuSupplier<T> supplier) {
        return new MenuType<T>(supplier, FeatureFlags.DEFAULT_FLAGS);
    }

    public static ModRegistrationProvider<MenuType<?>> bootStrap() {
        return MENUS;
    }

}