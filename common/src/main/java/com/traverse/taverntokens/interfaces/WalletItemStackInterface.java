package com.traverse.taverntokens.interfaces;

import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.nbt.CompoundTag;

public interface WalletItemStackInterface {

    default boolean isEmpty() {
        return true;
    }

    default WalletItemStack split(long amount) {
        return null;
    }

    static WalletItemStack of(CompoundTag nbt) {
        return null;
    }

    default CompoundTag save(CompoundTag nbt) {
        return null;
    }

    default long getMaxLongStackSize() {
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

    public static boolean matches(WalletItemStack left, WalletItemStack right) {
        return false;
    }

    public static boolean isSameItem(WalletItemStack left, WalletItemStack right) {
        return false;
    }

    public static boolean canCombine(WalletItemStack stack, WalletItemStack otherStack) {
        return false;
    }

    default long getLongCount() {
        return 0;
    }

    default void setCount(long count) {
    }

    default void grow(long amount) {
    }

    default void shrink(long amount) {
    }
}
