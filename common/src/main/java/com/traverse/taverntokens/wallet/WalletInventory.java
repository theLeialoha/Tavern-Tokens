package com.traverse.taverntokens.wallet;

import java.util.ArrayList;
import java.util.List;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.registry.ModTags;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.NonNullList;

public class WalletInventory implements Container {

    public NonNullList<WalletItemStack> stacks = NonNullList.withSize(6 * 4, WalletItemStack.EMPTY);
    private double cooldown = 0;

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public WalletItemStack getCopy(int slot) {
        slot -= 9 * 4; // Default player inventory
        return stacks.get(slot).copy();
    }

    @Override
    public WalletItemStack getItem(int slot) {
        slot -= 9 * 4; // Default player inventory
        return stacks.get(slot).copy();
    }

    @Override
    public int countItem(Item item) {
        return (int) Math.min((long) Integer.MAX_VALUE,
                stacks.stream().filter(i -> i.is(item)).map(i -> i.getLongCount())
                        .reduce((a, b) -> a + b).orElse(0L));
    }

    @Override
    public boolean isEmpty() {
        return stacks.stream().filter(i -> !i.isEmpty()).count() == 0;
    }

    @Override
    public void setChanged() {
        joinSimilar();
        removeEmpty();
    }

    public void joinSimilar() {
        for (int i = stacks.size() - 1; i >= 0; i--) {
            WalletItemStack current = stacks.get(i);
            if (current.isEmpty())
                continue;

            for (int j = 0; j < i; j++) {
                if (i <= j)
                    continue;
                WalletItemStack prev = stacks.get(j);
                if (prev.isEmpty())
                    continue;
                if (prev.isFull())
                    continue;
                if (!prev.canCombine(current))
                    continue;
                if (prev.hasTag() != current.hasTag())
                    continue;
                else if (prev.hasTag() && !prev.getTag().equals(current.getTag()))
                    continue;

                long stackSize = prev.getLongCount();
                long maxStackSize = prev.getMaxLongStackSize();
                long amountToMax = Math.max(maxStackSize - stackSize, 0L);
                long amountToTake = Math.min(amountToMax, current.getLongCount());
                prev.grow(amountToTake);
                current.shrink(amountToTake);
            }
        }
    }

    public void removeEmpty() {
        List<WalletItemStack> copyStacks = List.copyOf(stacks);
        stacks = NonNullList.withSize(6 * 4, WalletItemStack.EMPTY);
        int i = 0;
        for (WalletItemStack walletItemStack : copyStacks) {
            if (!walletItemStack.isEmpty() && walletItemStack.getLongCount() > 0) {
                stacks.set(i, walletItemStack);
                i++;
            }
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return removeItem(slot, 64);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack removeItem(int slot, int amount) {
        slot -= 9 * 4; // Default player inventory
        WalletItemStack original = stacks.get(slot);
        int maxAmountToTake = Math.min(amount, original.getMaxStackSize());
        ItemStack split = original.split(maxAmountToTake);

        // if (original.isEmpty() || original.getLongCount() == 0)
        // removeEmpty();

        return split;
    }

    @SuppressWarnings("deprecation")
    public int getStackSize(int slot) {
        slot -= 9 * 4; // Default player inventory
        WalletItemStack original = stacks.get(slot);
        return original.getCount();
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        slot -= 9 * 4; // Default player inventory
        if (!hasRoomFor(stack))
            return; // Safety net

        WalletItemStack walletItemStack;
        if (stack instanceof WalletItemStack walletStack)
            walletItemStack = walletStack;
        else
            walletItemStack = WalletItemStack.fromVanillaItemStack(stack);

        // Optional<WalletItemStack> itemFilter = stacks.stream()
        // .filter(i -> i.isOf(stack.getItem())).findFirst();
        // boolean hasItem = itemFilter.isPresent();

        stacks.set(slot, walletItemStack);

        // if (hasItem) itemFilter.get().setCount(count);
        // else stacks.add(0, WalletItemStack.fromVanillaItemStack(stack));
        // stacks.set(slot, WalletItemStack.fromVanillaItemStack(stack));
    }

    public boolean isValidItem(ItemStack stack) {
        boolean bypassChecks = stack.getTags().anyMatch(t -> t.equals(ModTags.BYPASS_CHECKS));
        boolean validCurrency = stack.getTags().anyMatch(t -> t.equals(ModTags.VALID_CURRENCY));

        if (validCurrency) {
            if (!TavernTokens.CONFIG.allowItemsWithNBT.get()) {
                if (stack.hasTag()) {
                    return TavernTokens.CONFIG.allowBypassWithNBT.get() && bypassChecks;
                }
            }
            return true;
        } else
            return false;
    }

    public boolean hasRoomFor(ItemStack stack) {
        boolean hasRoom = stacks.stream().anyMatch(i -> i.canCombine(stack) || i.isEmpty());
        return isValidItem(stack) && hasRoom;
    }

    public int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    public boolean isHighlightable(int slot) {
        slot -= 9 * 4; // Default player inventory
        return getContainerSize() > slot;
    }

    @Override
    public int getContainerSize() {
        int size = (int) stacks.stream().filter(i -> !i.isEmpty() || i.getLongCount() > 0).count();
        return Math.min(size + 1, 4 * 6);
    }

    @Override
    public void clearContent() {
        stacks = NonNullList.withSize(6 * 4, WalletItemStack.EMPTY);
    }

    public void readNbtList(ListTag nbtList) {
        stacks = NonNullList.withSize(6 * 4, WalletItemStack.EMPTY);

        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag tag = nbtList.getCompound(i);
            stacks.set(i, WalletItemStack.of(tag));
        }
    }

    public ListTag toNbtList() {
        ListTag listTag = new ListTag();

        for (WalletItemStack item : stacks) {
            if (item.isEmpty())
                continue;
            CompoundTag tag = new CompoundTag();
            item.save(tag);
            listTag.add(tag);
        }

        return listTag;
    }

    public boolean isOnDropCooldown() {
        return cooldown > System.currentTimeMillis() / 1000;
    }

    public void setDropCooldown() {
        cooldown = System.currentTimeMillis() / 1000 + 0.25;
    }

    public void copy(WalletInventory walletInventory) {
        stacks = walletInventory.stacks;
    }

    @SuppressWarnings("deprecation")
    public void addItemStack(ItemStack stack) {
        WalletItemStack walletStack = WalletItemStack.fromVanillaItemStack(stack);
        addWalletStack(walletStack);
        stack.setCount(walletStack.getCount());
    }

    public void addWalletStack(WalletItemStack stack) {
        WalletItemStack newStack = stack.copy();

        boolean canBypass = TavernTokens.CONFIG.allowBypassWithNBT.get()
                && stack.getTags().anyMatch(t -> t.equals(ModTags.BYPASS_CHECKS));

        if (newStack.hasTag() && !canBypass) {
            if (TavernTokens.CONFIG.allowStripItemsWithNBT.get())
                newStack.setTag(new CompoundTag());
            else if (!TavernTokens.CONFIG.allowItemsWithNBT.get())
                return;
        }

        for (WalletItemStack walletStack : stacks) {
            if (newStack.isEmpty())
                return;
            if (walletStack.isEmpty())
                continue;
            if (!walletStack.canCombine(newStack))
                continue;
            if (walletStack.hasTag() != newStack.hasTag())
                continue;
            else if (walletStack.hasTag() && !walletStack.getTag().equals(newStack.getTag()))
                continue;
            long stackSize = walletStack.getLongCount();
            long maxStackSize = walletStack.getMaxLongStackSize();
            long amountToMax = Math.max(maxStackSize - stackSize, 0L);
            if (amountToMax <= 0)
                continue;
            long amountToTake = Math.min(amountToMax, stack.getLongCount());
            walletStack.grow(amountToTake);
            newStack.shrink(amountToTake);
            stack.shrink(amountToTake);
        }

        while (hasRoomFor(newStack) && !newStack.isEmpty()) {
            long amountToMax = newStack.getMaxLongStackSize();
            long amountToTake = Math.min(amountToMax, stack.getLongCount());

            WalletItemStack stackToInsert = newStack.copy();
            stackToInsert.setCount(amountToTake);
            stacks.set(getContainerSize() - 1, stackToInsert);

            newStack.shrink(amountToTake);
            stack.shrink(amountToTake);
        }
    }

    public void updateSlot(int slot, WalletItemStack walletItemStack) {
        slot -= 9 * 4; // Default player inventory
        stacks.set(slot, walletItemStack);
    }

    public WalletItemStack[] takePercent(float percentage) {
        List<WalletItemStack> output = new ArrayList<>();

        for (int i = 0; i < stacks.size(); i++) {
            WalletItemStack stack = stacks.get(i);
            if (stack.isEmpty())
                continue;
            if (stack.getTags().anyMatch(t -> t.equals(ModTags.WALLET_KEEP)))
                continue;

            WalletItemStack split = stack.split((long) Math.ceil(stack.getLongCount() * percentage));
            output.add(split);
        }

        return output.toArray(WalletItemStack[]::new);
    }

}
