package com.traverse.taverntokens;

import java.nio.file.Path;

import com.traverse.taverntokens.registry.FabricModKeybinds;
import com.traverse.taverntokens.registry.ModCreativeTabs;
import com.traverse.taverntokens.registry.ModItems;
import com.traverse.taverntokens.registry.ModMenus;
import com.traverse.taverntokens.utils.registry.ModRegistrationProvider;
import com.traverse.taverntokens.utils.registry.ModRegistryEntry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;

public class FabricTavernTokens extends TavernTokens implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        init();

        register(ModItems.bootStrap());
        register(ModMenus.bootStrap());
        register(ModCreativeTabs.bootStrap());
    }

    @Override
    public void onInitializeClient() {
        initClient();

        FabricModKeybinds.register();
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }

    private static <V> void register(ModRegistrationProvider<V> bootStrap) {
        Registry<V> registery = bootStrap.getRegistery();
        for (ModRegistryEntry<V> entry : bootStrap.getEntries()) {
            Registry.register(registery, entry.location, entry.value);
        }
    }

}