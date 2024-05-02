package com.traverse.taverntokens.wallet;

import java.util.Arrays;
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

    protected HashMap<Item, Long> inventory = new HashMap<>();
    protected Item[] slots = inventory.keySet().toArray(Item[]::new);

    // save()

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slots.length <= slot) return ItemStack.EMPTY;

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
        boolean hasRoom = Arrays.asList(stack.getItem()).contains(stack.getItem())
                || inventory.size() != size();

        return isValidItem(stack) && hasRoom;
    }

    public Item[] getInventory() {
        return this.inventory.keySet().toArray(Item[]::new);
    }

    public int getMaxItemCount() {
        return Integer.MAX_VALUE;
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

}
