package com.traverse.taverntokens.wallet;

import java.sql.Ref;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class WalletScreenHandler extends ScreenHandler {

    public WalletInventory walletInventory;
    public PlayerInventory playerInventory;

    public WalletScreenHandler(int syncId, PlayerInventory inventory) {
        super(TavernTokens.WALLET_SCREEN_HANDLER, syncId);

        this.walletInventory = ((PlayerEntityWithBagInventory) inventory.player).getWalletInventory();
        this.playerInventory = inventory;

        // Player Inventory Slots
        for (int rows = 0; rows < 4; rows++) {
            for (int cols = 0; cols < 9; cols++) {
                int x = 8 + cols * 18;
                int y = 74 + rows * 18;
                if (rows == 0) y += 76;
                this.addSlot(new Slot(playerInventory, cols + rows * 9, x, y));
            }
        }

        // Wallet Inventory Slots
        int slotOffset = this.slots.size();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int x = 62 + col * 18;
                int y = 8 + row * 18;

                addSlot(new WalletSlot(walletInventory, slotOffset + col + row * 9, x, y));
            }
        }
    }

    @Override
    public void onSlotClick(int slot, int button, SlotActionType actionType, PlayerEntity player) {
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
        } catch (IndexOutOfBoundsException e) { }
    }
    
    public void onClone(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;
    }

    public void onPickup(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;
        if (!isPlayerInventory) {
            if (this.walletInventory.isValidItem(getCursorStack()) || getCursorStack().isEmpty()) {
                if (getCursorStack().isEmpty()) {
                    int stackSize = this.walletInventory.getStackSize(slot);
                    int scale = button + 1;
                    setCursorStack(this.walletInventory.removeStack(slot, stackSize / scale));
                } else {
                    ItemStack itemToInsert = getCursorStack();
                    int amount = itemToInsert.getCount();
                    itemToInsert.setCount(button == 0 ? amount : 1);

                    this.walletInventory.setStack(0, itemToInsert);
                    itemToInsert.setCount(amount - itemToInsert.getCount());
                    setCursorStack(itemToInsert);
                }
            }
        } else super.onSlotClick(slot, button, SlotActionType.PICKUP, player);
    }

    public void onPickupAll(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;

        // TODO: Force a limt
    }

    public void onQuickCraft(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;

        References.LOGGER.info("CLICK DRAG");
    }

    public void onQuickMove(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;
        if (!isPlayerInventory) {
            if (this.walletInventory.isValidItem(slots.getStack())) {
                for (int index = 0; index < 9 * 4; index++) {
                    Slot tempSlot = this.slots.get(index);
                    if (tempSlot.getStack().isEmpty()) {
                        tempSlot.setStack(this.walletInventory.removeStack(slot));
                        break;
                    }
                }
            }
        } else {
            if (this.walletInventory.isValidItem(slots.getStack())) {
                this.walletInventory.setStack(0, slots.getStack());
                slots.setStack(ItemStack.EMPTY);
            } else {
                boolean isHotbar = slot < 9;
                int startIndex = isHotbar ? 9 : 0;
                int endIndex = isHotbar ? 9 * 4 : 8;

                for (int index = startIndex; index < endIndex; index++) {
                    Slot tempSlot = this.slots.get(index);
                    if (tempSlot.getStack().isEmpty()) {
                        tempSlot.setStack(slots.getStack());
                        slots.setStack(ItemStack.EMPTY);
                        break;
                    }
                }
            }
        }
    }

    public void onSwap(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;
    }

    public void onThrow(int slot, int button, PlayerEntity player) {
        Slot slots = this.slots.get(slot);
        boolean isPlayerInventory = slots.inventory == playerInventory;
        if (!this.walletInventory.isOnDropCooldown()) {
            this.walletInventory.setDropCooldown();
        }
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return walletInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

}
