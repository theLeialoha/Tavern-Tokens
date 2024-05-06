package com.traverse.taverntokens.networking;

import com.traverse.taverntokens.interfaces.PlayerEntityWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletScreenHandlerFactory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class PacketHandler {

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PacketConstants.UPDATE_WALLET_ID, (client, handler, buf, responseSender) -> {
            WalletInventory walletInventory = ((PlayerEntityWithBagInventory) client.player).getWalletInventory();
            
            boolean isEmpty = buf.readBoolean();
            if (!isEmpty) {
                NbtCompound compound = buf.readNbt();
                NbtList wallet = compound.getList("wallet", 10);
                walletInventory.readNbtList(wallet);
            } else walletInventory.clear();
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

    public static void updateWallet(ServerPlayerEntity serverPlayer, WalletInventory walletInventory) {
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound compound = new NbtCompound();
        NbtList walletItems = walletInventory.toNbtList();
        buf.writeBoolean(walletItems.isEmpty());

        if (!walletItems.isEmpty()) {
            compound.put("wallet", walletItems);
            buf.writeNbt(compound);
        }

        ServerPlayNetworking.send(serverPlayer, PacketConstants.UPDATE_WALLET_ID, buf);
    }

}
