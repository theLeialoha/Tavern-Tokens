package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.ForgeTavernTokens;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ForgeModTags extends ModTags {

    TagKey<Item> VALID_CURRENCY = ItemTags.create(new ResourceLocation(ForgeTavernTokens.MODID, "valid_currency"));
    TagKey<Item> BYPASS_CHECKS = ItemTags.create(new ResourceLocation(ForgeTavernTokens.MODID, "bypass_checks"));
    TagKey<Item> WALLET_KEEP = ItemTags.create(new ResourceLocation(ForgeTavernTokens.MODID, "wallet_keep"));

    public static void register() {
    }
}