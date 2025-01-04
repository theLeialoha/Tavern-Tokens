package com.traverse.taverntokens;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.traverse.taverntokens.config.TavernTokensConfig;

import de.maxhenkel.configbuilder.ConfigBuilder;

public abstract class TavernTokens {

    public static TavernTokensConfig CONFIG;
    public static final String MODID = "taverntokens";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public void init() {
        initConfig();

        TavernTokens.LOGGER.info("Insert Coins to Continue");
    }

    public void initClient() {
        initConfig();
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