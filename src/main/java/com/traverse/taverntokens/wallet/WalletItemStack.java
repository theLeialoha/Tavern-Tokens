package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.interfaces.WalletItemStackInterface;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class WalletItemStack extends ItemStack implements WalletItemStackInterface {

    private long count;
    public static final WalletItemStack EMPTY = new WalletItemStack(ItemStack.EMPTY);

    public WalletItemStack(ItemStack item) {
        this(item.getItem(), item.getCount());
    }

    public WalletItemStack(ItemConvertible item) {
        this(item, 1);
    }

    public WalletItemStack(RegistryEntry<Item> entry) {
        this((ItemConvertible)entry.value(), 1);
    }

    public WalletItemStack(RegistryEntry<Item> itemEntry, long count) {
        this((ItemConvertible)itemEntry.value(), count);
    }

    public WalletItemStack(ItemConvertible item, long count) {
        super(item, 1);
        this.count = count;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY || this.item == Items.AIR || this.count <= 0;
    }

    @Deprecated
    @Override
    public ItemStack split(int amount) {
        long i = Math.min(Math.min(amount, this.getCount()), 64);
        WalletItemStack itemStack = this.copyWithCount(i);
        this.decrement(i);
        return itemStack.toItemStack();
    }

    public WalletItemStack split(long amount) {
        long i = Math.min(amount, this.getItemCount());
        WalletItemStack itemStack = this.copyWithCount(i);
        this.decrement(i);
        return itemStack;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Identifier identifier = Registries.ITEM.getId(this.getItem());
        nbt.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
        nbt.putLong("Count", this.count);
        return nbt;
    }

    public long getMaxItemCount() {
        return Long.MAX_VALUE;
    }

    public boolean isStackable() {
        return getMaxItemCount() > 1 && (!this.isDamageable() || !this.isDamaged());
    }

    @Override
    public WalletItemStack copy() {
        if (this.isEmpty()) return EMPTY;
        WalletItemStack itemStack = new WalletItemStack((ItemConvertible) this.getItem(), this.count);
        itemStack.setBobbingAnimationTime(this.getBobbingAnimationTime());
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
        if (left == right) return true;
        if (left.getItemCount() != right.getItemCount()) return false;
        return WalletItemStack.canCombine(left, right);
    }

    @Deprecated
    public static boolean areItemsEqual(ItemStack left, ItemStack right) {
        return left.isOf(right.getItem());
    }

    public static boolean areItemsEqual(WalletItemStack left, WalletItemStack right) {
        return left.isOf(right.getItem());
    }

    @Deprecated
    public static boolean canCombine(ItemStack stack, ItemStack otherStack) {
        if (!stack.isOf(otherStack.getItem())) return false;
        if (stack.isEmpty() && otherStack.isEmpty()) return true;
        return WalletItemStack.canCombine((WalletItemStack) stack, (WalletItemStack) otherStack);
    }

    public static boolean canCombine(WalletItemStack stack, WalletItemStack otherStack) {
        if (!stack.isOf(otherStack.getItem())) return false;
        if (stack.isEmpty() && otherStack.isEmpty()) return true;
        return true;
    }

    @Deprecated
    @Override
    public int getCount() {
        return (int) Math.min(getItemCount(), 64);
    }

    public long getItemCount() {
        return this.isEmpty() ? 0 : this.count;
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
    public void increment(int amount) {
        this.setCount(this.getItemCount() + amount);
    }
    
    public void increment(long amount) {
        this.setCount(this.getItemCount() + amount);
    }

    @Deprecated
    @Override
    public void decrement(int amount) {
        this.increment(-amount);
    }
    
    public void decrement(long amount) {
        this.increment(-amount);
    }

    public ItemStack toItemStack() {
        int count = (int) Math.min(this.getItemCount(), 64);
        return new ItemStack(this.getItem(), count);
    }
}
