package com.traverse.taverntokens.networking;

import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletScreenHandlerFactory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class PacketHandler extends PacketHandlerInterface {

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PacketConstants.UPDATE_WALLET_ID, (client, handler, buf, responseSender) -> {
            WalletInventory walletInventory = ((PlayerEntityWithBagInventory) client.player).getWalletInventory();
            
            int slot = buf.readInt();
            WalletItemStack itemStack = WalletItemStack.fromNbt(buf.readNbt());
            walletInventory.updateSlot(slot, itemStack);
        });
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(PacketConstants.OPEN_WALLET_ID, (server, player, handler, buf, responseSender) -> {
            player.openHandledScreen(new WalletScreenHandlerFactory());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void requestWallet() {
        ClientPlayNetworking.send(PacketConstants.OPEN_WALLET_ID, PacketByteBufs.empty());
    }

    public static void updateWalletSlot(ServerPlayerEntity serverPlayer, int slot, NbtCompound itemCompound) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(slot);
        buf.writeNbt(itemCompound);

        ServerPlayNetworking.send(serverPlayer, PacketConstants.UPDATE_WALLET_ID, buf);
    }

}
