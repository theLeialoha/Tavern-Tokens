package com.traverse.taverntokens.registry;

import com.traverse.taverntokens.ForgeTavernTokens;
import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.screens.WalletScreen;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = ForgeTavernTokens.MODID)
public class ForgeModMenus extends ModMenus {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES, ForgeTavernTokens.MODID);

    static {
        CONTAINERS.register("wallet_screen", () -> WALLET_SCREEN_HANDLER);
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.MENU_TYPES, helper -> {
            helper.register(new ResourceLocation(TavernTokens.MODID, "wallet_screen"), WALLET_SCREEN_HANDLER);
        });
    }

    public static void register() {
        MenuScreens.<WalletContainerMenu, WalletScreen>register(WALLET_SCREEN_HANDLER,
                (gui, inventory, title) -> new WalletScreen(gui, inventory, title));
    }
}