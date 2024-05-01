package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.References;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static Item COPPER_COIN = new Item(new FabricItemSettings());
    public static Item IRON_COIN = new Item(new FabricItemSettings());
    public static Item GOLD_COIN = new Item(new FabricItemSettings());
    public static Item NETHERITE_COIN = new Item(new FabricItemSettings());
    public static Item DEBUG_ITEM = new DebugItem(new FabricItemSettings());
    public static  ItemGroup TAB = Registry.register(Registries.ITEM_GROUP, new Identifier(References.MODID,"taverntokens_item_group"),
    FabricItemGroup.builder().displayName(Text.translatable("taverntokens_item_group"))
            .icon(() -> new ItemStack(Items.BUNDLE))
            .entries((displayContext, entries) -> {
                entries.add(COPPER_COIN);
                entries.add(IRON_COIN);
                entries.add(GOLD_COIN);
                entries.add(NETHERITE_COIN);
                entries.add(DEBUG_ITEM);
            })
            .build());

    public static final TagKey<Item> VALID_CURRENCY = TagKey.of(RegistryKeys.ITEM, new Identifier(References.MODID, "valid_currency"));

    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier(References.MODID, "copper_coin"), COPPER_COIN);
        Registry.register(Registries.ITEM, new Identifier(References.MODID, "iron_coin"), IRON_COIN);
        Registry.register(Registries.ITEM, new Identifier(References.MODID, "gold_coin"), GOLD_COIN);
        Registry.register(Registries.ITEM, new Identifier(References.MODID, "netherite_coin"), NETHERITE_COIN);
        Registry.register(Registries.ITEM, new Identifier(References.MODID, "debug_item"), DEBUG_ITEM);
    }
}