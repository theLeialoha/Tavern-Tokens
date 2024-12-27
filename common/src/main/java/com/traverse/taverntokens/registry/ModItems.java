package com.traverse.taverntokens.registry;

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

    public static Item COPPER_COIN = new Item(new Item.Properties());
    public static Item IRON_COIN = new Item(new Item.Properties());
    public static Item GOLD_COIN = new Item(new Item.Properties());
    public static Item NETHERITE_COIN = new Item(new Item.Properties());

    public static CreativeModeTab CREATIVE_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable("itemGroup." + TavernTokens.MODID))
            .icon(() -> new ItemStack(Items.BUNDLE))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(COPPER_COIN);
                output.accept(IRON_COIN);
                output.accept(GOLD_COIN);
                output.accept(NETHERITE_COIN);
            })
            .build();

    public static final TagKey<Item> VALID_CURRENCY = TagKey.create(Registries.ITEM,
            new ResourceLocation(TavernTokens.MODID, "valid_currency"));

    public static void register() {
    }
}