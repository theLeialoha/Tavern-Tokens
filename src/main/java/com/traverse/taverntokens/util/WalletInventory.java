package com.traverse.taverntokens.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

import java.util.HashMap;

public class WalletInventory implements Inventory {

    protected HashMap<Item, Long> inventory = new HashMap<>();
    protected Item[] slots = inventory.keySet().toArray(Item[]::new);

    public static WalletInventory createWalletInventory(PlayerEntity player) {
        // player.getNBTObject('coins')
        // TODO: Create the initializer
        return null;
    }

    // save()

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        ItemStack original = new ItemStack(slots[slot]);
        int amount = (int) Math.min(64L, inventory.get(slots[slot]));
        original.setCount(amount);
        return original;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void markDirty() {
        // Remove any empty coins
        inventory.entrySet().stream().filter(e -> e.getValue() == 0L)
            .forEach(e -> inventory.remove(e.getKey()));

        // Update slots
        slots = inventory.keySet().toArray(Item[]::new);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack original = new ItemStack(slots[slot]);
        Long amountInBag = inventory.get(slots[slot]);
        int amountToTake = (int) Math.min(64L, amountInBag);
        inventory.put(slots[slot], amountInBag - amountToTake);
        original.setCount(amountToTake);
        return original;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack original = new ItemStack(slots[slot]);
        Long amountInBag = inventory.get(slots[slot]);
        inventory.put(slots[slot], amountInBag - amount);
        original.setCount(amount);
        return original;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (!inventory.keySet().contains(stack.getItem()))
            inventory.put(stack.getItem(), (long) stack.getCount());
        else {
            Long amountInBag = inventory.get(stack.getItem());
            inventory.put(stack.getItem(), amountInBag + stack.getCount());
        }
    }

    @Override
    public int size() {
        return Math.max(inventory.size() + 1, 4*6);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

}
