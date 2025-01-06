package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static TagKey<Item> VALID_CURRENCY = TagKey.create(Registries.ITEM,
            new ResourceLocation(TavernTokens.MODID, "valid_currency"));
    public static TagKey<Item> BYPASS_CHECKS = TagKey.create(Registries.ITEM,
            new ResourceLocation(TavernTokens.MODID, "bypass_checks"));
    public static TagKey<Item> WALLET_KEEP = TagKey.create(Registries.ITEM,
            new ResourceLocation(TavernTokens.MODID, "wallet_keep"));

    public static void register() {
    }

}
