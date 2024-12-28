package com.traverse.taverntokens;

import java.nio.file.Path;

import com.traverse.taverntokens.registry.ForgeModItems;
import com.traverse.taverntokens.registry.ForgeModKeybinds;
import com.traverse.taverntokens.registry.ForgeModMenus;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(ForgeTavernTokens.MODID)
public class ForgeTavernTokens extends TavernTokens {

    protected IEventBus eventBus;

    public ForgeTavernTokens() {
        this(FMLJavaModLoadingContext.get());
    }

    public ForgeTavernTokens(FMLJavaModLoadingContext context) {
        this.eventBus = context.getModEventBus();

        ForgeModItems.ITEMS.register(eventBus);
        ForgeModItems.TAB.register(eventBus);
        ForgeModItems.register();
        ForgeModMenus.register();

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
}