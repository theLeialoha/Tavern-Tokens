package com.traverse.taverntokens.networking;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public abstract class PacketHandler {

    public static void registerClient() {
    }

    public static void registerServer() {
    }

    public final static void requestWallet() {
        Packet<?> packet = createPlayC2SPacket(PacketConstants.OPEN_WALLET_ID, createByteBuf());
        sendPacketToServer(packet);
    }

    public static void updateWalletSlot(ServerPlayer serverPlayer, int slot, CompoundTag itemCompound) {
        FriendlyByteBuf buf = createByteBuf();
        buf.writeInt(slot);
        buf.writeNbt(itemCompound);

        Packet<?> packet = createPlayS2CPacket(PacketConstants.UPDATE_WALLET_ID, buf);
        sendPacketToClient(serverPlayer, packet);
    }

    protected static final FriendlyByteBuf createByteBuf() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    public static final ServerboundCustomPayloadPacket createPlayC2SPacket(ResourceLocation channelName,
            FriendlyByteBuf buf) {
        return new ServerboundCustomPayloadPacket(channelName, buf);
    }

    public static final ClientboundCustomPayloadPacket createPlayS2CPacket(ResourceLocation channelName,
            FriendlyByteBuf buf) {
        return new ClientboundCustomPayloadPacket(channelName, buf);
    }

    public static final void sendPacketToServer(Packet<?> packet) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(packet);
        }
    }

    public static final void sendPacketToClient(ServerPlayer serverPlayer, Packet<?> packet) {
        serverPlayer.connection.send(packet);
    }

}
