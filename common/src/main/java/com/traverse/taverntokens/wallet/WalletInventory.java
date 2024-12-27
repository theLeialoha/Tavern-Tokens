package com.traverse.taverntokens.wallet;

import java.util.List;
import java.util.Optional;

import com.traverse.taverntokens.registry.ModItems;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
        try {
            WalletItemStack original = getCopy(slot);
            CompoundTag compound = original.getOrCreateTag();
            CompoundTag display = compound.getCompound("display");
            ListTag lore = compound.getList("Lore", Tag.TAG_STRING);

            lore.add(StringTag.valueOf(
                    "{\"text\":\"Total: " + original.getLongCount() + "\", \"color\":\"#f5d69d\",\"italic\":false}"));
            display.put("Lore", lore);
            compound.put("display", display);
            original.setTag(compound);
            return original;
        } catch (Exception e) {
            return WalletItemStack.EMPTY;
        }
    }

    @Override
    public int countItem(Item item) {
        Optional<WalletItemStack> itemstack = stacks.stream().filter(i -> i.is(item)).findFirst();
        return itemstack.isEmpty() ? 0 : itemstack.get().getCount();
    }

    @Override
    public boolean isEmpty() {
        return stacks.stream().filter(i -> !i.isEmpty()).count() == 0;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return removeItem(slot, 64);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        slot -= 9 * 4; // Default player inventory
        WalletItemStack original = stacks.get(slot);
        ItemStack split = original.split(amount);

        if (original.isEmpty() || original.getLongCount() == 0) {
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

        return split;
    }

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
        return (stack.is(ModItems.VALID_CURRENCY));
    }

    public boolean hasRoomFor(ItemStack stack) {
        Optional<WalletItemStack> itemFilter = stacks.stream()
                .filter(i -> i.is(stack.getItem())).findFirst();

        boolean hasRoom = itemFilter.isPresent()
                || getContainerSize() - 1 != stacks.size();

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
        stacks.clear();
    }

    public void readNbtList(ListTag nbtList) {
        stacks = NonNullList.withSize(6 * 4, WalletItemStack.EMPTY);

        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag tag = nbtList.getCompound(i);
            ResourceLocation resourceLocation = ResourceLocation.tryParse(tag.getString("id"));
            if (resourceLocation == null)
                continue;

            stacks.set(i, WalletItemStack.fromTag(tag));
        }
    }

    public ListTag toNbtList() {
        ListTag listTag = new ListTag();

        for (WalletItemStack item : stacks) {
            if (item.isEmpty())
                continue;
            ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item.getItem());
            if (resourceLocation == null)
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

    public void addItemStack(ItemStack stack) {
        Optional<WalletItemStack> walletItem = stacks.stream()
                .filter(i -> WalletItemStack.canCombine(i, WalletItemStack.fromVanillaItemStack(stack))).findFirst();
        if (walletItem.isPresent() && !walletItem.get().hasTag()) {
            walletItem.get().grow(stack.getCount());
            stack.shrink(stack.getCount());
        } else if (hasRoomFor(stack) && stack.hasTag()) {
            WalletItemStack newStack = WalletItemStack.fromVanillaItemStack(stack);
            newStack.setCount(1L);
            stacks.set(getContainerSize() - 1, newStack);
            stack.shrink(1);
        } else if (hasRoomFor(stack)) {
            stacks.set(getContainerSize() - 1, WalletItemStack.fromVanillaItemStack(stack));
            stack.shrink(stack.getCount());
        }
    }

    public void updateSlot(int slot, WalletItemStack walletItemStack) {
        slot -= 9 * 4; // Default player inventory
        stacks.set(slot, walletItemStack);
    }

}
