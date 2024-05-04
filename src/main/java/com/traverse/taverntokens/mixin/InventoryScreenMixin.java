package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.References;
import com.traverse.taverntokens.networking.PacketHandler;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Unique
    private final Identifier identifier_main = new Identifier(References.MODID, "textures/gui/wallet.png");

    @Inject(at = @At("TAIL"), method = "init", cancellable = true)
    private void init(CallbackInfo ci) {
        this.addDrawableChild(
            new TexturedButtonWidget(this.x + 27, this.height / 2 - 16, 10, 10, 0, 0, 10, identifier_main, 10, 20, (button) -> {
                PacketHandler.requestWallet();
            })
        );
    }
}
