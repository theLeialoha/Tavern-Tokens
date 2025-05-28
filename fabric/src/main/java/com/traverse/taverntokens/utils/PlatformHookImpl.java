package com.traverse.taverntokens.utils;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab.Builder;

public class PlatformHookImpl implements PlatformHook {

    @Override
    public String getLoader() {
        return "fabric";
    }

    @Override
    public Builder newCreativeTab() {
        return FabricItemGroup.builder();
    }
    

}
