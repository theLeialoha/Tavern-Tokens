package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.item.BagItem;
import com.traverse.taverntokens.utils.registry.ModRegistrationProvider;
import com.traverse.taverntokens.utils.registry.ModRegistryObject;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

public final class ModItems {
    public static final ModRegistrationProvider<Item> ITEMS = ModRegistrationProvider.of(BuiltInRegistries.ITEM, TavernTokens.MODID);

    public static final ModRegistryObject<Item> MONEY_BAG = ITEMS.register("money_bag", new BagItem(new Item.Properties().stacksTo(1)));
    public static final ModRegistryObject<Item> COPPER_COIN = ITEMS.register("copper_coin", new Item(new Item.Properties()));
    public static final ModRegistryObject<Item> IRON_COIN = ITEMS.register("iron_coin", new Item(new Item.Properties()));
    public static final ModRegistryObject<Item> GOLD_COIN = ITEMS.register("gold_coin", new Item(new Item.Properties()));
    public static final ModRegistryObject<Item> NETHERITE_COIN = ITEMS.register("netherite_coin", new Item(new Item.Properties()));

    public static ModRegistrationProvider<Item> bootStrap() {
        return ITEMS;
    }

    public static CreativeModeTab CREATIVE_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable("itemGroup." + TavernTokens.MODID))
            .icon(() -> new ItemStack(MONEY_BAG.get()))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(MONEY_BAG.get());
                output.accept(COPPER_COIN.get());
                output.accept(IRON_COIN.get());
                output.accept(GOLD_COIN.get());
                output.accept(NETHERITE_COIN.get());
            })
            .build();
}