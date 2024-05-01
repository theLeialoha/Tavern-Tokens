package com.traverse.taverntokens;

import com.traverse.taverntokens.registry.ModItems;
import com.traverse.taverntokens.screens.WalletScreenHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class TavernTokens implements ModInitializer {

	// public static final ScreenHandlerType<WalletScreenHandler> WALLET_SCREEN_HANDLER_2 = Registry.register(
	// 	Registries.SCREEN_HANDLER, new Identifier(References.MODID, "wallet_screen"),
	// 		new ScreenHandlerType<>(WalletScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
	// 	// new ExtendedScreenHandlerType<>(
	// 	// 	(syncId, inventory, buf) -> new WalletScreenHandler(syncId, inventory.player))
	// );

	public static ScreenHandlerType<WalletScreenHandler> WALLET_SCREEN_HANDLER;

	@Override
	public void onInitialize() {
		References.LOGGER.info("Insert Coins to Continue");
		ModItems.registerItems();

		WALLET_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(References.MODID, "wallet_screen"),
				((syncId, inventory) -> new WalletScreenHandler(syncId, inventory)));
	}
}