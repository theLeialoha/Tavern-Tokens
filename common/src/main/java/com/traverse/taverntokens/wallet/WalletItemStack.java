package com.traverse.taverntokens.wallet;

import java.util.List;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.WalletItemStackInterface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.RegistryAccess.RegistryEntry;
import net.minecraft.resources.ResourceLocation;

public class WalletItemStack extends ItemStack implements WalletItemStackInterface {

    private long count;
    public static final WalletItemStack EMPTY = new WalletItemStack(ItemStack.EMPTY);

    private WalletItemStack(ItemStack item) {
        this(item.getItem(), item.getCount());
        this.setTag(item.getTag());
    }

    public WalletItemStack(ItemLike item) {
        this(item, 1);
    }

    public WalletItemStack(RegistryEntry<Item> entry) {
        this((ItemLike) entry.value(), 1);
    }

    public WalletItemStack(RegistryEntry<Item> itemEntry, long count) {
        this((ItemLike) itemEntry.value(), count);
    }

    public WalletItemStack(ItemLike item, long count) {
        super(item, 1);
        this.count = count;
    }

    private WalletItemStack(CompoundTag nbt) {
        this(
                BuiltInRegistries.ITEM.get(new ResourceLocation(nbt.getString("id"))),
                nbt.getLong("Count"));
        if (nbt.contains("tag", (int) Tag.TAG_COMPOUND)) {
            super.setTag(nbt.getCompound("tag"));
            this.getItem().verifyTagAfterLoad(this.getTag());
        }
        if (this.isDamageableItem()) {
            this.setDamageValue(this.getDamageValue());
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isEmpty() {
        return this == EMPTY || this.item == Items.AIR || this.count <= 0;
    }

    @Deprecated
    @Override
    public ItemStack split(int amount) {
        long i = Math.min(Math.min(amount, this.getLongCount()), 64);
        WalletItemStack itemStack = this.copyWithCount(i);
        this.shrink(i);
        return itemStack.toItemStack();
    }

    public WalletItemStack split(long amount) {
        long i = Math.min(amount, this.getLongCount());
        WalletItemStack itemStack = this.copyWithCount(i);
        this.shrink(i);
        return itemStack;
    }

    public static WalletItemStack fromVanillaItemStack(ItemStack itemStack) {
        return new WalletItemStack(itemStack);
    }

    public static WalletItemStack fromTag(CompoundTag nbt) {
        try {
            return new WalletItemStack(nbt);
        } catch (RuntimeException runtimeException) {
            TavernTokens.LOGGER.debug("Tried to load invalid item: {}", (Object) nbt, (Object) runtimeException);
            return EMPTY;
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(this.getItem());
        nbt.putString("id", resourceLocation == null ? "minecraft:air" : resourceLocation.toString());
        nbt.putLong("Count", this.count);
        if (this.getTag() != null) {
            nbt.put("tag", (Tag) this.getTag().copy());
        }
        return nbt;
    }

    public long getMaxLongStackSize() {
        if (hasTag())
            return TavernTokens.CONFIG.maxHeldAmountNBT.get();
        return TavernTokens.CONFIG.maxHeldAmount.get();
    }

    public boolean isStackable() {
        return getMaxLongStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
    }

    @Override
    public WalletItemStack copy() {
        if (this.isEmpty())
            return EMPTY;
        WalletItemStack itemStack = new WalletItemStack((ItemLike) this.getItem(), this.count);
        itemStack.setPopTime(this.getPopTime());
        if (this.hasTag())
            itemStack.setTag(this.getTag().copy());
        return itemStack;
    }

    @Deprecated
    @Override
    public ItemStack copyWithCount(int count) {
        return copyWithCount((long) count);
    }

    public WalletItemStack copyWithCount(long count) {
        if (this.isEmpty()) {
            return EMPTY;
        }
        WalletItemStack itemStack = this.copy();
        itemStack.setCount(count);
        return itemStack;
    }

    @Deprecated
    public static boolean areEqual(ItemStack left, ItemStack right) {
        return areEqual((WalletItemStack) left, (WalletItemStack) right);
    }

    public static boolean areEqual(WalletItemStack left, WalletItemStack right) {
        if (left == right)
            return true;
        if (left.getLongCount() != right.getLongCount())
            return false;
        return WalletItemStack.canCombine(left, right);
    }

    @Deprecated
    public static boolean areItemsEqual(ItemStack left, ItemStack right) {
        return left.is(right.getItem());
    }

    public static boolean areItemsEqual(WalletItemStack left, WalletItemStack right) {
        return left.is(right.getItem());
    }

    public boolean isFull() {
        return getMaxLongStackSize() <= getLongCount();
    }

    @Deprecated
    public static boolean canCombine(ItemStack stack, ItemStack otherStack) {
        return WalletItemStack.canCombine((WalletItemStack) stack, (WalletItemStack) otherStack);
    }

    public static boolean canCombine(WalletItemStack stack, WalletItemStack otherStack) {
        if (otherStack.isEmpty())
            return false;
        if (stack.isEmpty())
            return true;

        long stackSize = stack.getLongCount();
        long maxStackSize = stack.getMaxLongStackSize();
        long amountToMax = maxStackSize - stackSize;

        if (amountToMax <= 0)
            return false;

        if (!TavernTokens.CONFIG.allowStripItemsWithNBT.get())
            if (stack.hasTag() || otherStack.hasTag())
                if (!stack.getTag().equals(otherStack.getTag()))
                    return false;

        if (!stack.is(otherStack.getItem()))
            return false;
        return true;
    }

    public boolean canCombine(ItemStack stack) {
        return this.canCombine((WalletItemStack) stack);
    }

    public boolean canCombine(WalletItemStack stack) {
        return WalletItemStack.canCombine(this, stack);
    }

    public boolean hasTag() {
        return super.hasTag();
    }

    public CompoundTag getTag() {
        return super.getTag();
    }

    public void setTag(CompoundTag nbt) {
        super.setTag(nbt);
    }

    @Deprecated
    @Override
    public int getCount() {
        return (int) Math.min(getLongCount(), 64);
    }

    public long getLongCount() {
        return this.isEmpty() ? 0 : this.count;
    }

    public String getItemCountShort() {
        List<String> numAbbrv = List.of("", "K", "M", "B", "T", "Qa", "Qi");
        for (int i = 0; i < numAbbrv.size(); i++) {
            double c = Math.floor(this.count / Math.pow(1000L, i));
            if (c < 1000)
                return ((int) c) + numAbbrv.get(i);
        }
        return null;
    }

    @Deprecated
    @Override
    public void setCount(int count) {
        this.count = count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Deprecated
    @Override
    public void grow(int amount) {
        this.setCount(this.getLongCount() + amount);
    }

    public void grow(long amount) {
        this.setCount(this.getLongCount() + amount);
    }

    @Deprecated
    @Override
    public void shrink(int amount) {
        this.grow(-amount);
    }

    public void shrink(long amount) {
        this.grow(-amount);
    }

    public ItemStack toItemStack() {
        int count = (int) Math.min(this.getLongCount(), 64);
        ItemStack stack = new ItemStack(this.getItem(), count);
        stack.setTag(this.getTag());
        return stack;
    }

    @Override
    public List<Component> getTooltipLines(Player player, TooltipFlag tooltipFlag) {
        List<Component> tooltip = super.getTooltipLines(player, tooltipFlag);

        tooltip.add(Component.translatable("tooltip." + TavernTokens.MODID + ".count", getLongCount())
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xf5d69d)).withItalic(true)));

        return tooltip;
    }
}
