package com.traverse.taverntokens.networking;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.interfaces.PlayerWithBagInventory;
import com.traverse.taverntokens.wallet.WalletInventory;
import com.traverse.taverntokens.wallet.WalletItemStack;
import com.traverse.taverntokens.wallet.WalletScreenMenuProvider;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public abstract class PacketHandler {

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

    public static final void injectServer(ServerPlayer serverPlayer, Connection connection) {
        ChannelPipeline pipeline = connection.channel.pipeline();

        pipeline.addBefore("packet_handler", TavernTokens.MODID, new SimpleChannelInboundHandler<Packet<?>>() {
            protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) throws Exception {
                ServerPlayer player = serverPlayer.getServer().getPlayerList().getPlayer(serverPlayer.getUUID());

                if (packet instanceof ServerboundCustomPayloadPacket customPayloadPacket) {
                    if (customPayloadPacket.getIdentifier().equals(PacketConstants.OPEN_WALLET_ID)) {
                        player.openMenu(new WalletScreenMenuProvider());
                        return;
                    }
                }
                ctx.fireChannelRead(packet);
            };
        });
    }

    public static final void uninjectServer(Connection connection) {
        ChannelPipeline pipeline = connection.channel.pipeline();
        if (pipeline.get(TavernTokens.MODID) != null)
            pipeline.remove(TavernTokens.MODID);
    }

    public static final void injectClient(LocalPlayer player, Connection connection) {
        connection.channel.pipeline().addBefore("packet_handler", TavernTokens.MODID,
                new SimpleChannelInboundHandler<Packet<?>>() {
                    protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) throws Exception {
                        if (packet instanceof ClientboundCustomPayloadPacket customPayloadPacket) {
                            FriendlyByteBuf buf = customPayloadPacket.getData();

                            if (customPayloadPacket.getIdentifier().equals(PacketConstants.UPDATE_WALLET_ID)) {
                                WalletInventory walletInventory = ((PlayerWithBagInventory) player)
                                        .getWalletInventory();

                                int slot = buf.readInt();
                                WalletItemStack itemStack = WalletItemStack.fromTag(buf.readNbt());
                                walletInventory.updateSlot(slot, itemStack);

                                return;
                            }
                        }
                        ctx.fireChannelRead(packet);
                    };
                });
    }

    public static final void uninjectClient(Connection connection) {
        if (connection.channel.pipeline().get(TavernTokens.MODID) != null)
            connection.channel.pipeline().remove(TavernTokens.MODID);
    }

}
