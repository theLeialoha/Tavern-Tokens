package com.traverse.taverntokens.utils;

import net.minecraft.world.item.CreativeModeTab;

public interface PlatformHook {
    
    String getLoader();

    static PlatformHook get() {
        return PlatformHandler.HOOK;
    }

    CreativeModeTab.Builder newCreativeTab();

    class PlatformHandler {
        protected static final PlatformHook HOOK = detectPlatform();

        private static PlatformHook detectPlatform() {
            try {
                String packageName = PlatformHandler.class.getPackageName();
                Class<?> clazz = Class.forName(packageName + "." + "PlatformHookImpl");
                if (!PlatformHook.class.isAssignableFrom(clazz))
                    throw new IllegalAccessError("Class \"" + clazz + "\" does not implment " + PlatformHook.class.getSimpleName());
                return (PlatformHook) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
