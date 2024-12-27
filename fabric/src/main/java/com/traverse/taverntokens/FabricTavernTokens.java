package com.traverse.taverntokens;

import java.nio.file.Path;

import com.traverse.taverntokens.networking.FabricPacketHandler;
import com.traverse.taverntokens.registry.FabricModItems;
import com.traverse.taverntokens.registry.FabricModKeybinds;
import com.traverse.taverntokens.registry.FabricModMenus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class FabricTavernTokens extends TavernTokens implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        init();

        FabricModItems.register();
        FabricPacketHandler.registerServer();
    }

    @Override
    public void onInitializeClient() {
        initClient();

        FabricModMenus.register();
        FabricModKeybinds.register();
        FabricPacketHandler.registerClient();
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }
}