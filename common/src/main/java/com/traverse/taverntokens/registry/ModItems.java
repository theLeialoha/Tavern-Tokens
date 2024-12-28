package com.traverse.taverntokens.registry;

import java.util.function.Supplier;

import com.traverse.taverntokens.TavernTokens;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class ModItems {

    public static Supplier<Item> COPPER_COIN = () -> new Item(new Item.Properties());
    public static Supplier<Item> IRON_COIN = () -> new Item(new Item.Properties());
    public static Supplier<Item> GOLD_COIN = () -> new Item(new Item.Properties());
    public static Supplier<Item> NETHERITE_COIN = () -> new Item(new Item.Properties());

    public static CreativeModeTab CREATIVE_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable("itemGroup." + TavernTokens.MODID))
            .icon(() -> new ItemStack(Items.BUNDLE))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(COPPER_COIN.get());
                output.accept(IRON_COIN.get());
                output.accept(GOLD_COIN.get());
                output.accept(NETHERITE_COIN.get());
            })
            .build();

    public static TagKey<Item> VALID_CURRENCY = TagKey.create(Registries.ITEM,
            new ResourceLocation(TavernTokens.MODID, "valid_currency"));
}