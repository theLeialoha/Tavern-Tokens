package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.networking.PacketHandler;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

    public InventoryScreenMixin(Player player) {
        super(player.inventoryMenu, player.getInventory(), Component.translatable("container.crafting"));
    }

    @Unique
    private final ResourceLocation WALLET_GUI_LOCATION = new ResourceLocation(TavernTokens.MODID,
            "textures/gui/wallet.png");

    @Inject(at = @At("TAIL"), method = "init", cancellable = true)
    private void init(CallbackInfo ci) {

        int offsetChance = Math.floorDiv((int) Math.ceil(Math.random() * 100), 100) * 10;

        this.addRenderableWidget(
                new ImageButton(this.leftPos + 27, this.height / 2 - 16, 10, 10, offsetChance, 0, 10,
                        WALLET_GUI_LOCATION, 30, 20, (button) -> {
                            PacketHandler.requestWallet();
                        }));
    }
}
