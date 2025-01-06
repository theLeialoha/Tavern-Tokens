package com.traverse.taverntokens.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class TavernTokensConfig {

    public final ConfigEntry<Boolean> allowRollover;
    public final ConfigEntry<Float> lossPercentage;
    public final ConfigEntry<Boolean> allowDrops;

    public final ConfigEntry<Long> maxHeldAmount;
    public final ConfigEntry<Long> maxHeldAmountNBT;
    public final ConfigEntry<Boolean> allowItemsWithNBT;
    public final ConfigEntry<Boolean> allowStripItemsWithNBT;
    public final ConfigEntry<Boolean> allowBypassWithNBT;

    public TavernTokensConfig(ConfigBuilder builder) {

        allowRollover = builder.booleanEntry("allow_rollover", true)
                .comment("Enables/Disables currency rollover on respawn",
                        "When enabled, all the contents (after loss) is kept upon respawn",
                        "When disabled, all the contents (after loss) will disappear completely");

        lossPercentage = builder.floatEntry("loss_percentage", 15f, 0f, 100f)
                .comment("Percentage of loss upon death",
                        "The percent of coins to lose upon death");

        allowDrops = builder.booleanEntry("allow_drops", true)
                .comment("Enables/Disables currency drops upon death",
                        "When enabled, a bag is dropped instead of losing currency",
                        "When disabled, currency loss will disappear completely");

        maxHeldAmount = builder.longEntry("max_held_amount", 16777216L, 0L, Long.MAX_VALUE)
                .comment("The amount of items that can be held in the wallet",
                        "Max: 9223372036854775807 (~144,115,188,075,855,870 stacks)",
                        "Default: 16777216 (262,144 stacks)");

        maxHeldAmountNBT = builder.longEntry("max_held_amount_nbt", 5L, 0L, Long.MAX_VALUE)
                .comment("The amount of items (with NBT) that can be held in the wallet",
                        "Default: 5");

        allowItemsWithNBT = builder.booleanEntry("allow_items_with_nbt", true)
                .comment("Enables/Disables weather items with NBT can be stored");

        allowStripItemsWithNBT = builder.booleanEntry("allow_strip_with_nbt", true)
                .comment("Enables/Disables weather items with NBT will be stripped",
                        "When enabled, items with NBT will have their NBT stripped before storing",
                        "When disabled, items stored with NBT will keep their NBT");

        allowBypassWithNBT = builder.booleanEntry("allow_bypass_with_nbt", true)
                .comment("Enables/Disables weather items can bypass NBT checks",
                        "When enabled, items with the '#bypass_checks' tag will skip allow_nbt checks",
                        "When disabled, all items will be checked using allow_nbt validations",
                        "This option only takes effect if allow_nbt is disabled");
    }
}
