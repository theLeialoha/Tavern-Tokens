package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
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
            Slot slots = this.slots.get(slot);
            boolean isPlayerInventory = slots.inventory == playerInventory;
            if (!isPlayerInventory && actionType == SlotActionType.SWAP) {
                // TODO: Allow numpad swapping
            } else if (!isPlayerInventory && actionType == SlotActionType.THROW) {
                if (!this.walletInventory.isOnDropCooldown()) {
                    this.walletInventory.setDropCooldown();
                    super.onSlotClick(slot, button, actionType, player);
                }
            } else super.onSlotClick(slot, button, actionType, player);

            References.LOGGER.info(actionType.toString());
        } catch (IndexOutOfBoundsException e) {
            // Clicking on a gui but not in a slot results in an index of -999
            super.onSlotClick(slot, button, actionType, player);
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
