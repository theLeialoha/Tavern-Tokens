package com.traverse.taverntokens.interfaces;

import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.nbt.NbtCompound;

public interface WalletItemStackInterface {

    default boolean isEmpty() {
        return true;
    }

    default WalletItemStack split(long amount) {
        return null;
    }

    static WalletItemStack fromNbt(NbtCompound nbt) {
        return null;
    }

    default NbtCompound writeNbt(NbtCompound nbt) {
        return null;
    }

    default long getMaxItemCount() {
        return 0;
    }

    default boolean isStackable() {
        return false;
    }

    default WalletItemStack copy() {
        return null;
    }

    default WalletItemStack copyWithCount(long count) {
        return null;
    }

    public static boolean areEqual(WalletItemStack left, WalletItemStack right) {
        return false;
    }

    public static boolean areItemsEqual(WalletItemStack left, WalletItemStack right) {
        return false;
    }

    public static boolean canCombine(WalletItemStack stack, WalletItemStack otherStack) {
        return false;
    }

    default long getItemCount() {
        return 0;
    }

    default void setCount(long count) {}
    default void increment(long amount) {}
    default void decrement(long amount) {}
}
