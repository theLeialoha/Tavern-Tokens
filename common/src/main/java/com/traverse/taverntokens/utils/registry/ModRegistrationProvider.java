package com.traverse.taverntokens.utils.registry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistrationProvider<T> {

    private static final Set<ModRegistrationProvider<?>> providers = new HashSet<>();
    
    private final Set<ModRegistryObject<T>> entries = new HashSet<>();
    private final Registry<T> registery;
    private final String modid;

    private ModRegistrationProvider(Registry<T> registery, String modid) {
        this.registery = registery;
        this.modid = modid;
    }

    @SuppressWarnings("unchecked")
    public static <T> ModRegistrationProvider<T> of(Registry<T> registry, String modid) {
        Optional<ModRegistrationProvider<?>> filteredProviders = providers.stream()
            .filter(p -> p.getModID().equalsIgnoreCase(modid))
            .filter(p -> p.registery.equals(registry)).findFirst();

        if (filteredProviders.isPresent()) return (ModRegistrationProvider<T>) filteredProviders.get();

        ModRegistrationProvider<T> provider = new ModRegistrationProvider<>(registry, modid);
        providers.add(provider);
        return provider;
    }

    @SuppressWarnings("unchecked")
    public <V extends T> ModRegistryObject<V> register(String name, V value) {
        String finalName = name.toLowerCase();

        if (entries.stream().map(e -> e.id().toLowerCase()).toList().contains(finalName))
            throw new IllegalArgumentException("An object named \"" + finalName + "\" already exists");

        ModRegistryObject<T> object = new ModRegistryObject<>(value, finalName);
        entries.add(object);

        return (ModRegistryObject<V>) object;
    }

    public boolean contains(Object object) {
        if (entries.contains(object)) return true;
        return entries.stream().anyMatch(ro -> ro.get().equals(object));
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int size() {
        return entries.size();
    }

    public Registry<T> getRegistery() {
        return registery;
    }

    @SuppressWarnings("unchecked")
    public ResourceKey<Registry<T>> getRegisteryKey() {
        return (ResourceKey<Registry<T>>) registery.asLookup().key();
    }

    public Collection<ModRegistryEntry<T>> getEntries() {
        return ModRegistryEntry.fromProvider(this);
    }

    public String getModID() {
        return modid;
    }

    protected Set<ModRegistryObject<T>> getRawEntries() {
        return Set.copyOf(entries);
    }

}
