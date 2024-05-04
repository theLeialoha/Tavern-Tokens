package com.traverse.taverntokens.wallet;

import java.util.HashMap;

import com.traverse.taverntokens.registry.ModItems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class WalletInventory implements Inventory {

    private HashMap<Item, Long> inventory = new HashMap<>();
    private double cooldown = 0;

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        slot -= 9 * 4; // Default player inventory
        if (inventory.size() <= slot) return ItemStack.EMPTY;
        
        Item item = inventory.keySet().toArray(Item[]::new)[slot];
        ItemStack original = new ItemStack(item);
        original.setCount(1);
        return original;
    }

    @Override
    public int count(Item item) {
        return (int) Math.min(64, inventory.get(item));
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void markDirty() {
        // Remove any empty coins
        @SuppressWarnings("unchecked")
        HashMap<Item, Long> tempInventory = (HashMap<Item, Long>) inventory.clone();

        tempInventory.entrySet().stream().filter(e -> e.getValue() == 0L)
            .forEach(e -> inventory.remove(e.getKey()));
    }

    @Override
    public ItemStack removeStack(int slot) {
        slot -= 9 * 4; // Default player inventory
        Item item = inventory.keySet().toArray(Item[]::new)[slot];
        ItemStack original = new ItemStack(item);
        Long amountInBag = inventory.get(item);
        int amountToTake = (int) Math.min(amountInBag, 64);
        if (amountInBag - amountToTake != 0) inventory.put(item, amountInBag - amountToTake);
        else inventory.remove(item);
        original.setCount(amountToTake);
        return original;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        slot -= 9 * 4; // Default player inventory
        Item item = inventory.keySet().toArray(Item[]::new)[slot];
        ItemStack original = new ItemStack(item);
        Long amountInBag = inventory.get(item);
        if (amountInBag - amount != 0) inventory.put(item, amountInBag - amount);
        else inventory.remove(item);
        original.setCount(amount);
        return original;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        slot -= 9 * 4; // Default player inventory
        if (!hasRoomFor(stack)) return; // Safety net

        if (!inventory.keySet().contains(stack.getItem()))
            inventory.put(stack.getItem(), (long) stack.getCount());
        else {
            Long amountInBag = inventory.get(stack.getItem());
            inventory.put(stack.getItem(), amountInBag + stack.getCount());
        }
    }

    public boolean isValidItem(ItemStack stack) {
        return (stack.isIn(ModItems.VALID_CURRENCY));
    }

    public boolean hasRoomFor(ItemStack stack) {
        boolean hasRoom = inventory.keySet().contains(stack.getItem())
                || inventory.size() != size();

        return isValidItem(stack) && hasRoom;
    }

    public Item[] getInventory() {
        return this.inventory.keySet().toArray(Item[]::new);
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
        return Math.min(inventory.size() + 1, 4*6);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    public void readNbtList(NbtList nbtList) {
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Identifier identifier = Identifier.tryParse(nbtCompound.getString("id"));
            if (identifier == null) continue;

            Item item = Registries.ITEM.get(identifier);
            Long count = nbtCompound.getLong("Count");
            inventory.put(item, count);
        }
    }

    public NbtList toNbtList() {
        NbtList nbtList = new NbtList();

        for (Item item : inventory.keySet()) {
            Identifier identifier = Registries.ITEM.getId(item);
            if (identifier == null) continue;

            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putString("id", identifier.toString());
            nbtCompound.putLong("Count", inventory.get(item));
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
        inventory.putAll(walletInventory.inventory);
    }

}
