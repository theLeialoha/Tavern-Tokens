package com.traverse.taverntokens;

import java.nio.file.Path;

import com.traverse.taverntokens.registry.FabricModKeybinds;
import com.traverse.taverntokens.registry.ModCreativeTabs;
import com.traverse.taverntokens.registry.ModItems;
import com.traverse.taverntokens.registry.ModMenus;
import com.traverse.taverntokens.registry.ModTags;
import com.traverse.taverntokens.screens.WalletScreen;
import com.traverse.taverntokens.utils.registry.ModRegistrationProvider;
import com.traverse.taverntokens.utils.registry.ModRegistryEntry;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class FabricTavernTokens extends TavernTokens implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        init();

        register(ModItems.bootStrap());
        register(ModCreativeTabs.bootStrap());
        ModTags.register();
    }

    @Override
    public void onInitializeClient() {
        initClient();

        registerMenus(ModMenus.bootStrap());
        FabricModKeybinds.register();
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }

    private static void registerMenus(ModRegistrationProvider<MenuType<?>> bootStrap) {
        register(bootStrap);

        MenuScreens.<WalletContainerMenu, WalletScreen>register(ModMenus.WALLET_SCREEN_HANDLER.get(), (gui, inventory, title) -> new WalletScreen(gui, inventory, title));
    }

    private static <V> void register(ModRegistrationProvider<V> bootStrap) {
        Registry<V> registery = bootStrap.getRegistery();
        for (ModRegistryEntry<V> entry : bootStrap.getEntries()) {
            Registry.register(registery, entry.location, entry.value);
        }
    }


}