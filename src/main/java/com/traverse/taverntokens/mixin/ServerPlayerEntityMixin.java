package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;
import com.traverse.taverntokens.networking.PacketHandler;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletScreenHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin {

    @SuppressWarnings("static-access")
    @Inject(at = @At("RETURN"), method = "copyFrom")
    public void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        this.walletInventory.copy(((PlayerEntityWithBagInventory) oldPlayer).getWalletInventory());
    }

    @Inject( method = "onScreenHandlerOpened", at = @At("INVOKE"), cancellable = true )
    public void onScreenHandlerOpened(ScreenHandler screenHandler, CallbackInfo ci) {
        if (screenHandler instanceof WalletScreenHandler) {
            ServerPlayerEntity that = (ServerPlayerEntity) (Object) this;

            screenHandler.updateSyncHandler(new ScreenHandlerSyncHandler() {
                @Override
                public void updateState(ScreenHandler handler, DefaultedList<ItemStack> stacks, ItemStack cursorStack, int[] properties) {
                    that.networkHandler.sendPacket(new InventoryS2CPacket(handler.syncId, handler.nextRevision(), stacks, cursorStack));
                    for (int i = 0; i < properties.length; ++i) {
                        this.sendPropertyUpdate(handler, i, properties[i]);
                    }
                }

                @Override
                public void updateSlot(ScreenHandler handler, int slot, ItemStack stack) {
                    if (stack instanceof WalletItemStack walletItem) {
                        NbtCompound walletItemAsNBT = walletItem.writeNbt(new NbtCompound());
                        PacketHandler.updateWalletSlot(that, slot, walletItemAsNBT);
                    } else {
                        that.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), slot, stack));
                    }
                }

                @Override
                public void updateCursorStack(ScreenHandler handler, ItemStack stack) {
                    that.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-1, handler.nextRevision(), -1, stack));
                }

                @Override
                public void updateProperty(ScreenHandler handler, int property, int value) {
                    this.sendPropertyUpdate(handler, property, value);
                }

                private void sendPropertyUpdate(ScreenHandler handler, int property, int value) {
                    that.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(handler.syncId, property, value));
                }
            });

            ci.cancel();
        }
    }

}
