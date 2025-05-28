package com.traverse.taverntokens;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.traverse.taverntokens.config.TavernTokensConfig;
import com.traverse.taverntokens.registry.ModMenus;
import com.traverse.taverntokens.registry.ModTags;
import com.traverse.taverntokens.screens.WalletScreen;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import de.maxhenkel.configbuilder.ConfigBuilder;
import net.minecraft.client.gui.screens.MenuScreens;

public abstract class TavernTokens {

    public static TavernTokensConfig CONFIG;
    public static final String MODID = "taverntokens";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public void init() {
        initConfig();
        ModTags.register();

        TavernTokens.LOGGER.info("Insert Coins to Continue");
    }

    public void initClient() {
        initConfig();

        MenuScreens.<WalletContainerMenu, WalletScreen>register(ModMenus.WALLET_SCREEN_HANDLER.get(), (gui, inventory, title) -> new WalletScreen(gui, inventory, title));
    }

    private void initConfig() {
        if (CONFIG == null) {
            CONFIG = ConfigBuilder.builder(TavernTokensConfig::new)
                    .path(getConfigFolder().resolve(MODID).resolve("taverntokens.properties")).build();
        }
    }

    public static String getTranslationKey(String category, String... path) {
        if (path.length == 0)
            return category + "." + MODID;
        else
            return category + "." + MODID + "." + String.join(".", path);
    }

    public abstract Path getConfigFolder();
}