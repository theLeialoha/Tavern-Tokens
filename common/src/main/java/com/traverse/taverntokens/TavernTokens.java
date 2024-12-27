package com.traverse.taverntokens;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TavernTokens {

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
    }

    public abstract Path getConfigFolder();
}