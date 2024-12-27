package com.traverse.taverntokens.registry;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.traverse.taverntokens.TavernTokens;

import net.minecraft.client.KeyMapping;

public abstract class ModKeybinds {

    public static KeyMapping OPEN_WALLET = new KeyMapping(
            "key." + TavernTokens.MODID + ".wallet", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_R, // The keycode of the key
            "key.category." + TavernTokens.MODID // The translation key of the keybinding's category.
    );

    public static void register() {
    }

}
