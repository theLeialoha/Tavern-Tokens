package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FabricModItems extends ModItems {

    public static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, new ResourceLocation(TavernTokens.MODID, "taverntokens_item_group"));

    public static void register() {
        var MONEY_BAG_ITEM = Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(TavernTokens.MODID, "money_bag"),
                MONEY_BAG.get());
        var COPPER_COIN_ITEM = Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(TavernTokens.MODID, "copper_coin"),
                COPPER_COIN.get());
        var IRON_COIN_ITEM = Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(TavernTokens.MODID, "iron_coin"),
                IRON_COIN.get());
        var GOLD_COIN_ITEM = Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(TavernTokens.MODID, "gold_coin"),
                GOLD_COIN.get());
        var NETHERITE_COIN_ITEM = Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(TavernTokens.MODID, "netherite_coin"),
                NETHERITE_COIN.get());

        MONEY_BAG = () -> MONEY_BAG_ITEM;
        COPPER_COIN = () -> COPPER_COIN_ITEM;
        IRON_COIN = () -> IRON_COIN_ITEM;
        GOLD_COIN = () -> GOLD_COIN_ITEM;
        NETHERITE_COIN = () -> NETHERITE_COIN_ITEM;

        CREATIVE_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP,
                FabricItemGroup.builder().title(Component.translatable("itemGroup." + TavernTokens.MODID))
                        .icon(() -> new ItemStack(Items.BUNDLE)).displayItems((itemDisplayParameters, output) -> {
                            output.accept(MONEY_BAG.get());
                            output.accept(COPPER_COIN.get());
                            output.accept(IRON_COIN.get());
                            output.accept(GOLD_COIN.get());
                            output.accept(NETHERITE_COIN.get());
                        }).build());
    }
}