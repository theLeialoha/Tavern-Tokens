package com.traverse.taverntokens.utils;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class PlatformHookImpl implements PlatformHook {

    @Override
    public String getLoader() {
        return "forge";
    }

    @Override
    public Builder newCreativeTab() {
        return CreativeModeTab.builder();
    }

}
