package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class FabricModItems extends ModItems {

    public static void register() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TavernTokens.MODID, "copper_coin"), COPPER_COIN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TavernTokens.MODID, "iron_coin"), IRON_COIN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TavernTokens.MODID, "gold_coin"), GOLD_COIN);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TavernTokens.MODID, "netherite_coin"),
                NETHERITE_COIN);

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation(TavernTokens.MODID, "taverntokens_item_group"), CREATIVE_TAB);
    }
}