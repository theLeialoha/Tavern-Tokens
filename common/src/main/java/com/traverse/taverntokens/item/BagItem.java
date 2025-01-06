package com.traverse.taverntokens.item;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.registry.ModTags;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BagItem extends Item {

    // Amount of inventory in the wallet
    private static final int MAX_INSERT_AMOUNT = 6 * 4;

    public BagItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (openContents(itemStack, player)) {
            // this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemStack);
        }
    }

    private static boolean openContents(ItemStack itemStack, Player player) {
        WalletInventory inv = ((PlayerWithBagInventory) player).getWalletInventory();
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains("Items")) {
            return false;
        } else {
            if (player instanceof ServerPlayer) {
                ListTag nbtList = compoundTag.getList("Items", 10);
                ListTag insert = new ListTag();

                for (int i = 0; i < nbtList.size(); ++i) {
                    CompoundTag tag = nbtList.getCompound(i);
                    WalletItemStack walletStack = WalletItemStack.of(tag);
                    inv.addWalletStack(walletStack);

                    if (!walletStack.isEmpty()) {
                        walletStack.save(tag);
                        insert.add(tag);
                    }
                }

                inv.setChanged();

                if (!insert.isEmpty())
                    compoundTag.put("Items", insert);
                else
                    itemStack.removeTagKey("Items");
            }

            if (compoundTag.contains("shadowed", 1) && !compoundTag.contains("Items")) {
                itemStack.setCount(0);
            }

            return true;
        }
    }

    public static void makeShadowed(ItemStack itemStack, boolean shadow) {
        if (itemStack.isEmpty())
            return;
        if (!(itemStack.getItem() instanceof BagItem))
            return;

        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putByte("shadowed", (byte) (shadow ? 1 : 0));
    }

    public static byte isShadowed(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        return compoundTag.getByte("shadowed");
    }

    public static boolean hasContents(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains("Items"))
            return false;
        return compoundTag.getList("Items", 10).size() > 0;
    }

    public static boolean addWalletItemStack(ItemStack itemStack, WalletItemStack itemToInsert) {
        if (itemStack.isEmpty())
            return false;
        if (!(itemStack.getItem() instanceof BagItem))
            return false;
        if (itemToInsert.isEmpty())
            return false;
        if (itemToInsert.getTags().noneMatch(t -> t.equals(ModTags.VALID_CURRENCY)))
            return false;

        String itemId = BuiltInRegistries.ITEM.getKey(itemToInsert.getItem()).toString();
        long prevStackSize = itemToInsert.getLongCount();

        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains("Items"))
            compoundTag.put("Items", new ListTag());
        ListTag nbtList = compoundTag.getList("Items", 10);

        for (int i = 0; i < nbtList.size(); ++i) {
            if (itemToInsert.isEmpty())
                return true;
            CompoundTag tag = nbtList.getCompound(i);
            if (!tag.getString("id").equalsIgnoreCase(itemId))
                continue;
            long count = tag.getLong("Count");
            long amountToMax = Math.max(0, Long.MAX_VALUE - count);
            long amountToMove = Math.min(amountToMax, itemToInsert.getLongCount());
            tag.putLong("Count", count + amountToMove);
            itemToInsert.shrink(amountToMove);
        }

        if (nbtList.size() < MAX_INSERT_AMOUNT) {
            CompoundTag tag = new CompoundTag();
            itemToInsert.save(tag);
            nbtList.add(tag);
        }

        return itemToInsert.isEmpty() || prevStackSize < itemToInsert.getLongCount();
    }

    private static Stream<WalletItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null) {
            return Stream.empty();
        } else {
            ListTag listTag = compoundTag.getList("Items", 10);
            Stream<Tag> tagStream = listTag.stream();
            Objects.requireNonNull(CompoundTag.class);
            return tagStream.map(CompoundTag.class::cast).map(WalletItemStack::of);
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<WalletItemStack> nonNullList = NonNullList.create();
        Stream<WalletItemStack> walletStackStream = getContents(itemStack);
        Objects.requireNonNull(nonNullList);
        walletStackStream.forEach(nonNullList::add);
        return Optional.of(new BagItemTooltip(nonNullList));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (BagItem.hasContents(itemStack))
            list.add(Component.translatable(TavernTokens.getTranslationKey("item", "money_bag", "consume"))
                    .withStyle(ChatFormatting.GRAY));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
