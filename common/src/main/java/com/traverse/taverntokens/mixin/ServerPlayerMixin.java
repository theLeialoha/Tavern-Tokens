package com.traverse.taverntokens.mixin;

import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.networking.PacketHandler;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletSlot;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.NonNullList;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin {

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    public void onRespawn(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        this.walletInventory.copy(((PlayerWithBagInventory) oldPlayer).getWalletInventory());
    }

    @Inject(method = "initMenu", at = @At("INVOKE"), cancellable = true)
    public void initMenu(AbstractContainerMenu menu, CallbackInfo ci) {
        if (menu instanceof WalletContainerMenu) {
            ServerPlayer that = (ServerPlayer) (Object) this;

            menu.setSynchronizer(new ContainerSynchronizer() {
                @Override
                public void sendInitialData(AbstractContainerMenu menu, NonNullList<ItemStack> stacks,
                        ItemStack cursorStack,
                        int[] properties) {
                    that.connection.send(
                            new ClientboundContainerSetContentPacket(menu.containerId, menu.incrementStateId(), stacks,
                                    cursorStack));
                    for (int i = 0; i < properties.length; ++i) {
                        this.sendPropertyUpdate(menu, i, properties[i]);
                    }
                }

                @Override
                public void sendSlotChange(AbstractContainerMenu menu, int slot, ItemStack stack) {
                    if (menu.getSlot(slot) instanceof WalletSlot walletSlot) {
                        WalletItemStack walletItem = (WalletItemStack) walletSlot.getItem();
                        CompoundTag walletItemAsNBT = walletItem.save(new CompoundTag());
                        PacketHandler.updateWalletSlot(that, slot, walletItemAsNBT);
                    } else if (stack instanceof WalletItemStack walletItem) {
                        CompoundTag walletItemAsNBT = walletItem.save(new CompoundTag());
                        PacketHandler.updateWalletSlot(that, slot, walletItemAsNBT);
                    } else {
                        that.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId,
                                menu.incrementStateId(), slot, stack));
                    }
                }

                @Override
                public void sendCarriedChange(AbstractContainerMenu menu, ItemStack stack) {
                    that.connection.send(new ClientboundContainerSetSlotPacket(-1, menu.incrementStateId(), -1, stack));
                }

                @Override
                public void sendDataChange(AbstractContainerMenu menu, int property, int value) {
                    this.sendPropertyUpdate(menu, property, value);
                }

                private void sendPropertyUpdate(AbstractContainerMenu menu, int property, int value) {
                    that.connection.send(new ClientboundContainerSetDataPacket(menu.containerId, property, value));
                }
            });

            ci.cancel();
        }
    }

}
