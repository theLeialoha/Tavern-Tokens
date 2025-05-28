package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.utils.PlatformHook;
import com.traverse.taverntokens.utils.registry.ModRegistrationProvider;
import com.traverse.taverntokens.utils.registry.ModRegistryObject;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

public final class ModCreativeTabs {
    public static final ModRegistrationProvider<CreativeModeTab> CREATIVE = ModRegistrationProvider
            .of(BuiltInRegistries.CREATIVE_MODE_TAB, TavernTokens.MODID);

    public static final ModRegistryObject<CreativeModeTab> MONEY_BAG = CREATIVE.register("taverntokens_item_group",
            PlatformHook.get().newCreativeTab()
                    .title(Component.translatable("itemGroup." + TavernTokens.MODID))
                    .icon(() -> new ItemStack(ModItems.MONEY_BAG.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MONEY_BAG.get());
                        output.accept(ModItems.COPPER_COIN.get());
                        output.accept(ModItems.IRON_COIN.get());
                        output.accept(ModItems.GOLD_COIN.get());
                        output.accept(ModItems.NETHERITE_COIN.get());
                    }).build());

    public static ModRegistrationProvider<CreativeModeTab> bootStrap() {
        return CREATIVE;
    }
}