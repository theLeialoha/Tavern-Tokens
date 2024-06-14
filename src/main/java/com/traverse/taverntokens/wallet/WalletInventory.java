package com.traverse.taverntokens.wallet;

import java.util.List;
import java.util.Optional;

import com.traverse.taverntokens.registry.ModItems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class WalletInventory implements Inventory {

    public DefaultedList<WalletItemStack> stacks = DefaultedList.ofSize(6*4, WalletItemStack.EMPTY);
    private double cooldown = 0;

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public WalletItemStack getCopy(int slot) {
        slot -= 9 * 4; // Default player inventory
        return stacks.get(slot).copy();
    }

    @Override
    public WalletItemStack getStack(int slot) {
        try {
            WalletItemStack original = getCopy(slot);
            NbtCompound compound = original.getOrCreateNbt();
            NbtCompound display = compound.getCompound("display");
            NbtList lore = compound.getList("Lore", NbtElement.STRING_TYPE);

            lore.add(NbtString.of("{\"text\":\"Total: " + original.getItemCount() + "\", \"color\":\"#f5d69d\",\"italic\":false}"));
            display.put("Lore", lore);
            compound.put("display", display);
            original.setNbt(compound);
            return original;
        } catch (Exception e) {
            return WalletItemStack.EMPTY;
        }
    }

    @Override
    public int count(Item item) {
        Optional<WalletItemStack> itemstack = stacks.stream().filter(i -> i.isOf(item)).findFirst();
        return itemstack.isEmpty() ? 0 : itemstack.get().getCount();
    }

    @Override
    public boolean isEmpty() {
        return stacks.stream().filter(i -> !i.isEmpty()).count() == 0;
    }

    @Override
    public void markDirty() { }

    @Override
    public ItemStack removeStack(int slot) {
        return removeStack(slot, 64);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        slot -= 9 * 4; // Default player inventory
        WalletItemStack original = stacks.get(slot);
        ItemStack split = original.split(amount);

        if (original.isEmpty() || original.getItemCount() == 0) {
            List<WalletItemStack> copyStacks = List.copyOf(stacks);
            stacks = DefaultedList.ofSize(6 * 4, WalletItemStack.EMPTY);
            int i = 0;
            for (WalletItemStack walletItemStack : copyStacks) {
                if (!walletItemStack.isEmpty() && walletItemStack.getItemCount() > 0) {
                    stacks.set(i, walletItemStack);
                    i++;
                }
            }
        }

        return split;
    }

    public int getStackSize(int slot) {
        slot -= 9 * 4; // Default player inventory
        WalletItemStack original = stacks.get(slot);
        return original.getCount();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        slot -= 9 * 4; // Default player inventory
        if (!hasRoomFor(stack)) return; // Safety net

        WalletItemStack walletItemStack;
        if (stack instanceof WalletItemStack walletStack) walletItemStack = walletStack;
        else walletItemStack = WalletItemStack.fromVanillaItemStack(stack);

        // Optional<WalletItemStack> itemFilter = stacks.stream()
        //         .filter(i -> i.isOf(stack.getItem())).findFirst();
        // boolean hasItem = itemFilter.isPresent();

        stacks.set(slot, walletItemStack);

        // if (hasItem) itemFilter.get().setCount(count);
        // else stacks.add(0, WalletItemStack.fromVanillaItemStack(stack));
        // stacks.set(slot, WalletItemStack.fromVanillaItemStack(stack));
    }

    public boolean isValidItem(ItemStack stack) {
        return (stack.isIn(ModItems.VALID_CURRENCY));
    }

    public boolean hasRoomFor(ItemStack stack) {
        Optional<WalletItemStack> itemFilter = stacks.stream()
                .filter(i -> i.isOf(stack.getItem())).findFirst();

        boolean hasRoom = itemFilter.isPresent()
                || size() - 1 != stacks.size();

        return isValidItem(stack) && hasRoom;
    }

    public int getMaxItemCount() {
        return Integer.MAX_VALUE;
    }

    public boolean canBeHighlighted(int slot) {
        slot -= 9 * 4; // Default player inventory
        return size() > slot;
    }

    @Override
    public int size() {
        int size = (int) stacks.stream().filter(i -> !i.isEmpty() || i.getCount() > 0).count(); 
        return Math.min(size + 1, 4*6);
    }

    @Override
    public void clear() {
        stacks.clear();
    }

    public void readNbtList(NbtList nbtList) {
        stacks = DefaultedList.ofSize(6*4, WalletItemStack.EMPTY);

        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Identifier identifier = Identifier.tryParse(nbtCompound.getString("id"));
            if (identifier == null) continue;

            stacks.set(i, WalletItemStack.fromNbt(nbtCompound));
        }
    }

    public NbtList toNbtList() {
        NbtList nbtList = new NbtList();

        for (WalletItemStack item : stacks) {
            if (item.isEmpty()) continue;
            Identifier identifier = Registries.ITEM.getId(item.getItem());
            if (identifier == null) continue;

            NbtCompound nbtCompound = new NbtCompound();
            item.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        return nbtList;
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

    public void addItemStack(ItemStack stack) {
        Optional<WalletItemStack> walletItem = stacks.stream().filter(i -> WalletItemStack.canCombine(i, WalletItemStack.fromVanillaItemStack(stack))).findFirst();
        if (walletItem.isPresent() && !walletItem.get().hasNbt()) {
            walletItem.get().increment(stack.getCount());
            stack.decrement(stack.getCount());
        } else if (hasRoomFor(stack) && stack.hasNbt()) {
            WalletItemStack newStack = WalletItemStack.fromVanillaItemStack(stack);
            newStack.setCount(1L);
            stacks.set(size() - 1, newStack);
            stack.decrement(1);
        } else if (hasRoomFor(stack)) {
            stacks.set(size() - 1, WalletItemStack.fromVanillaItemStack(stack));
            stack.decrement(stack.getCount());
        }
    }

    public void updateSlot(int slot, WalletItemStack walletItemStack) {
        slot -= 9 * 4; // Default player inventory
        stacks.set(slot, walletItemStack);
    }

}
