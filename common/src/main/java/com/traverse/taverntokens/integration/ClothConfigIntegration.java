package com.traverse.taverntokens.integration;

import com.traverse.taverntokens.TavernTokens;

import de.maxhenkel.configbuilder.entry.BooleanConfigEntry;
import de.maxhenkel.configbuilder.entry.ConfigEntry;
import de.maxhenkel.configbuilder.entry.FloatConfigEntry;
import de.maxhenkel.configbuilder.entry.LongConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigIntegration {

    private static String getTranslationKey(String... path) {
        return TavernTokens.getTranslationKey("cloth_config", path);
    }

    private static Component translatable(String... path) {
        return Component.translatable(getTranslationKey(path));
    }

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(translatable("settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(translatable("category", "general"));

        general.addEntry(fromConfigEntry(entryBuilder,
                translatable("allow_rollover"),
                translatable("allow_rollover", "description"),
                TavernTokens.CONFIG.allowRollover));

        general.addEntry(fromConfigEntry(entryBuilder,
                translatable("loss_percentage"),
                translatable("loss_percentage", "description"),
                TavernTokens.CONFIG.lossPercentage));

        general.addEntry(fromConfigEntry(entryBuilder,
                translatable("allow_drops"),
                translatable("allow_drops", "description"),
                TavernTokens.CONFIG.allowDrops));

        ConfigCategory restrictions = builder.getOrCreateCategory(translatable("category", "restrictions"));

        restrictions.addEntry(fromConfigEntry(entryBuilder,
                translatable("max_held_amount"),
                translatable("max_held_amount", "description"),
                TavernTokens.CONFIG.maxHeldAmount));

        restrictions.addEntry(fromConfigEntry(entryBuilder,
                translatable("max_held_amount_nbt"),
                translatable("max_held_amount_nbt", "description"),
                TavernTokens.CONFIG.maxHeldAmountNBT));

        restrictions.addEntry(fromConfigEntry(entryBuilder,
                translatable("allow_items_with_nbt"),
                translatable("allow_items_with_nbt", "description"),
                TavernTokens.CONFIG.allowItemsWithNBT));

        restrictions.addEntry(fromConfigEntry(entryBuilder,
                translatable("allow_strip_with_nbt"),
                translatable("allow_strip_with_nbt", "description"),
                TavernTokens.CONFIG.allowStripItemsWithNBT));

        restrictions.addEntry(fromConfigEntry(entryBuilder,
                translatable("allow_bypass_with_nbt"),
                translatable("allow_bypass_with_nbt", "description"),
                TavernTokens.CONFIG.allowBypassWithNBT));

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private static <T> AbstractConfigListEntry<T> fromConfigEntry(ConfigEntryBuilder entryBuilder, Component name,
            Component description, ConfigEntry<T> entry) {
        if (entry instanceof BooleanConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startBooleanToggle(name, e.get())
                    .setTooltip(description)
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(b -> e.set(b).save())
                    .build();
        } else if (entry instanceof LongConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startLongField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(i -> e.set(i).save())
                    .build();
        } else if (entry instanceof FloatConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startFloatField(name, e.get())
                    .setTooltip(description)
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> e.set(d).save())
                    .build();
        }

        return null;
    }
}
