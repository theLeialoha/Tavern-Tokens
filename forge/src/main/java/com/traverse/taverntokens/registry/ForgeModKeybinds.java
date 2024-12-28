package com.traverse.taverntokens.registry;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class ForgeModKeybinds extends ModKeybinds {

    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(ModKeybinds.OPEN_WALLET);
    }
}