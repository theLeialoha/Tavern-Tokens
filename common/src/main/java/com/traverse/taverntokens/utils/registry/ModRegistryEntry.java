package com.traverse.taverntokens.utils.registry;

import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

public class ModRegistryEntry<T> {
    
    public final ResourceLocation location;
    public final T value;

    ModRegistryEntry(ResourceLocation location, T value) {
        this.location = location;
        this.value = value;
    }

    public static <V> Collection<ModRegistryEntry<V>> fromProvider(ModRegistrationProvider<V> provider) {
        return provider.getRawEntries().stream()
            .map(e -> new ModRegistryEntry<>(new ResourceLocation(provider.getModID(), e.id()), e.get()))
            .toList();
    }


}
