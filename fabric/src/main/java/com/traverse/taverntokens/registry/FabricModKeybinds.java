package com.traverse.taverntokens.registry;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class FabricModKeybinds extends ModKeybinds {

    public static void register() {
        KeyBindingHelper.registerKeyBinding(ModKeybinds.OPEN_WALLET);
    }
}