package com.traverse.taverntokens.client;

import org.lwjgl.glfw.GLFW;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.client.screens.WalletScreen;
import com.traverse.taverntokens.client.screens.WalletScreenHandlerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class TavernTokensClient implements ClientModInitializer {
	
	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		References.LOGGER.info("Client Loaded");
		HandledScreens.register(TavernTokens.WALLET_SCREEN_HANDLER, WalletScreen::new);

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + References.MODID + ".wallet", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_R, // The keycode of the key
				"key.category." + References.MODID // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				
				// client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
				References.LOGGER.info(client.player.getEntityName() + ", Button pressed");
				client.player.openHandledScreen(new WalletScreenHandlerFactory());
		
			}
		}); 
	} 
}