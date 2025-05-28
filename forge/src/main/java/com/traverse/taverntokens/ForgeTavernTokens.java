package com.traverse.taverntokens;

import java.nio.file.Path;
import java.util.function.Supplier;

import com.traverse.taverntokens.registry.ForgeModKeybinds;
import com.traverse.taverntokens.registry.ModCreativeTabs;
import com.traverse.taverntokens.registry.ModItems;
import com.traverse.taverntokens.registry.ModMenus;
import com.traverse.taverntokens.screens.WalletScreen;
import com.traverse.taverntokens.utils.registry.ModRegistrationProvider;
import com.traverse.taverntokens.utils.registry.ModRegistryEntry;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegisterEvent;

@Mod(TavernTokens.MODID)
@EventBusSubscriber(modid = TavernTokens.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ForgeTavernTokens extends TavernTokens {

    protected IEventBus eventBus;

    public ForgeTavernTokens() {
        this(FMLJavaModLoadingContext.get());
    }

    public ForgeTavernTokens(FMLJavaModLoadingContext context) {
        this.eventBus = context.getModEventBus();

        // ForgeModTags.register();
        // ForgeModItems.ITEMS.register(eventBus);
        // ForgeModItems.TAB.register(eventBus);
        // ForgeModItems.register();
        // ForgeModMenus.register();

        eventBus.addListener(this::commonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(this::clientSetup);
            eventBus.addListener(ForgeModKeybinds::registerKeybinds);
        });
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        init();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        initClient();
    }

    @Override
    public Path getConfigFolder() {
        return FMLLoader.getGamePath().resolve("config");
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        // ForgeExampleMod.LOGGER.info("HELLO FROM MOD REGISTRATION");

        register(event, () -> ModItems.bootStrap());
        register(event, () -> ModCreativeTabs.bootStrap());
        registerMenus(event, () -> ModMenus.bootStrap());
    }

    private static void registerMenus(RegisterEvent event, Supplier<ModRegistrationProvider<MenuType<?>>> bootStrap) {
        register(event, bootStrap);

        MenuScreens.<WalletContainerMenu, WalletScreen>register(ModMenus.WALLET_SCREEN_HANDLER.get(), (gui, inventory, title) -> new WalletScreen(gui, inventory, title));
    }

    private static <T> void register(RegisterEvent event, Supplier<ModRegistrationProvider<T>> object) {
        ModRegistrationProvider<T> provider = object.get();

        event.register(provider.getRegisteryKey(), helper -> {
            for (ModRegistryEntry<T> entry : provider.getEntries())
                helper.register(entry.location, entry.value);
        });

    }
}