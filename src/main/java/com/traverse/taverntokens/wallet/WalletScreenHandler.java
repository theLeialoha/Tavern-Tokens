package com.traverse.taverntokens.wallet;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

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
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                int x = 62 + col * 18;
                int y = 8 + row * 18;
                int slots = (this.slots.size() - 1);

                addSlot(new WalletSlot(walletInventory, slots + col + row * 9, x, y));
            }
        }
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return walletInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slots  = this.slots.get(slot);

        boolean isPlayerInventory = slots.inventory == playerInventory;
        if (isPlayerInventory) {
            stack = slots.getStack();

            if (walletInventory.hasRoomFor(stack)) {
                walletInventory.setStack(0, stack);
                stack = ItemStack.EMPTY;
            }
        } else {
            stack = walletInventory.removeStack(slot);
        }

        return stack;
    }

}
