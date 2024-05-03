package com.traverse.taverntokens;

import com.traverse.taverntokens.networking.PacketHandler;
import com.traverse.taverntokens.registry.ModItems;
import com.traverse.taverntokens.wallet.WalletScreenHandler;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class TavernTokens implements ModInitializer {

	public static ScreenHandlerType<WalletScreenHandler> WALLET_SCREEN_HANDLER;

	@Override
	public void onInitialize() {
		References.LOGGER.info("Insert Coins to Continue");
		ModItems.registerItems();
		PacketHandler.registerClient();
		PacketHandler.registerServer();

		WALLET_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(References.MODID, "wallet_screen"),
			new ScreenHandlerType<>(WalletScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
	}
}