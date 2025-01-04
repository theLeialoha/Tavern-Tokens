package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.registry.ModMenus;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;

public class WalletContainerMenu extends AbstractContainerMenu {

    public WalletInventory walletInventory;
    public Inventory playerInventory;

    @SuppressWarnings("unused")
    private final Player player;

    public WalletContainerMenu(int windowId, Inventory inventory) {
        this(windowId, inventory, inventory.player);
    }

    public WalletContainerMenu(int windowId, Inventory inventory, Player player) {
        super(ModMenus.WALLET_SCREEN_HANDLER, windowId);
        this.player = player;

        this.walletInventory = ((PlayerWithBagInventory) player).getWalletInventory();
        this.playerInventory = inventory;

        // Player Inventory Slots
        for (int rows = 0; rows < 4; rows++) {
            for (int cols = 0; cols < 9; cols++) {
                int x = 8 + cols * 18;
                int y = 74 + rows * 18;
                if (rows == 0)
                    y += 76;
                this.addSlot(new Slot(playerInventory, cols + rows * 9, x, y));
            }
        }

        // Wallet Inventory Slots
        int slotOffset = this.slots.size();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int x = 62 + col * 18;
                int y = 8 + row * 18;

                addSlot(new WalletSlot(walletInventory, slotOffset + col + row * 6, x, y));
            }
        }
    }

    @Override
    public void clicked(int slot, int button, ClickType actionType, Player player) {
        try {
            // Fuck MCreator ---- MUFFIN TIME
            switch (actionType) {
                case CLONE -> onClone(slot, button, player);
                case PICKUP -> onPickup(slot, button, player);
                case PICKUP_ALL -> onPickupAll(slot, button, player);
                case QUICK_CRAFT -> onQuickCraft(slot, button, player);
                case QUICK_MOVE -> onQuickMove(slot, button, player);
                case SWAP -> onSwap(slot, button, player);
                case THROW -> onThrow(slot, button, player);
            }

            // boolean isWallet = this.slots.get(slot).inventory == walletInventory;
            // if (isWallet) updateToClient();
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void onClone(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isInventory = slots.container == playerInventory;
        if (!isInventory) {
            ItemStack stack = walletInventory.getCopy(slot).toItemStack();
            setCarried(stack);
        } else
            super.clicked(slot, button, ClickType.CLONE, player);
    }

    public void onPickup(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isInventory = slots.container == playerInventory;
        if (!isInventory) {
            ItemStack stack = getCarried();
            if (walletInventory.isValidItem(stack) || stack.isEmpty()) {
                if (stack.isEmpty()) {
                    int divider = button + 1;
                    stack = walletInventory.removeItem(slot, 64 / divider);
                    setCarried(stack);
                } else if (slots.mayPlace(stack)) {
                    if (walletInventory.isValidItem(stack)) {
                        walletInventory.addItemStack(stack);
                    }
                }
            }
        } else
            super.clicked(slot, button, ClickType.PICKUP, player);
    }

    public void onPickupAll(int slot, int button, Player player) {
        // Automatically ignored
    }

    public void onQuickCraft(int slot, int button, Player player) {
        // Automatically ignored
    }

    public void onQuickMove(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        ItemStack itemStackSlot = slots.getItem();

        boolean isInventory = slots.container == playerInventory;
        if (!isInventory) {
            if (this.walletInventory.isValidItem(itemStackSlot)) {
                for (int index = 0; index < 9 * 4; index++) {
                    Slot tempSlot = this.slots.get(index);
                    ItemStack itemStackTempSlot = tempSlot.getItem().copy();

                    if (itemStackTempSlot.isEmpty()) {
                        tempSlot.setByPlayer(this.walletInventory.removeItemNoUpdate(slot));
                        break;
                    } else if (ItemStack.isSameItemSameTags(itemStackTempSlot, itemStackSlot)) {
                        int amountToMax = itemStackTempSlot.getMaxStackSize() - itemStackTempSlot.getCount();
                        int amountToTake = Math.max(0, amountToMax);
                        int amountToMove = Math.min(amountToTake, itemStackSlot.getCount());

                        if (amountToMove > 0) {
                            int newAmount = this.walletInventory.removeItem(slot, amountToMove).getCount();
                            itemStackTempSlot.grow(newAmount);
                            tempSlot.setByPlayer(itemStackTempSlot);
                            break;
                        }
                    }
                }
            }
        } else {
            if (this.walletInventory.isValidItem(itemStackSlot)) {
                this.walletInventory.addItemStack(itemStackSlot);
            } else {
                boolean isHotbar = slot < 9;
                int startIndex = isHotbar ? 9 : 0;
                int endIndex = isHotbar ? 9 * 4 : 8;

                for (int index = startIndex; index < endIndex; index++) {
                    Slot tempSlot = this.slots.get(index);
                    if (tempSlot.getItem().isEmpty()) {
                        tempSlot.setByPlayer(itemStackSlot);
                        slots.setByPlayer(ItemStack.EMPTY);
                        break;
                    }
                }
            }
        }
    }

    public void onSwap(int slot, int button, Player player) {
        Slot slots = this.slots.get(slot);
        boolean isInventory = slots.container == playerInventory;
        if (isInventory)
            super.clicked(slot, button, ClickType.SWAP, player);
        // TODO: Allow Hotbar Swapping
    }

    public void onThrow(int slot, int button, Player player) {
        // Automatically ignored
    }

    @Override
    public boolean stillValid(Player player) {
        return walletInventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

}
