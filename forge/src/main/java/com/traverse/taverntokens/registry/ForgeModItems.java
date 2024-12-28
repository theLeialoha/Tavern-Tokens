package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.ForgeTavernTokens;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            ForgeTavernTokens.MODID);
    public static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            ForgeTavernTokens.MODID);

    public static final RegistryObject<Item> COPPER_COIN_REGISTRY = ITEMS.register("copper_coin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_COIN_REGISTRY = ITEMS.register("iron_coin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COIN_REGISTRY = ITEMS.register("gold_coin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_COIN_REGISTRY = ITEMS.register("netherite_coin",
            () -> new Item(new Item.Properties()));

    public static RegistryObject<CreativeModeTab> CREATIVE_TAB = TAB.register("taverntokens_item_group",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + ForgeTavernTokens.MODID))
                    .icon(() -> new ItemStack(Items.BUNDLE))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(COPPER_COIN_REGISTRY.get());
                        output.accept(IRON_COIN_REGISTRY.get());
                        output.accept(GOLD_COIN_REGISTRY.get());
                        output.accept(NETHERITE_COIN_REGISTRY.get());
                    }).build());

    public static void register() {
        ModItems.COPPER_COIN = () -> COPPER_COIN_REGISTRY.get();
        ModItems.IRON_COIN = () -> IRON_COIN_REGISTRY.get();
        ModItems.GOLD_COIN = () -> GOLD_COIN_REGISTRY.get();
        ModItems.NETHERITE_COIN = () -> NETHERITE_COIN_REGISTRY.get();
        ModItems.VALID_CURRENCY = ItemTags.create(new ResourceLocation(ForgeTavernTokens.MODID, "valid_currency"));
    }
}