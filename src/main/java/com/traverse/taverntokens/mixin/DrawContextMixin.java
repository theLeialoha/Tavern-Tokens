package com.traverse.taverntokens.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow
    @Final
    private MatrixStack matrices;

    @Shadow
    public abstract int drawText(TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow);

    // @Inject(at = @At("HEAD"), method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    @ModifyVariable(at = @At("HEAD"), method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    private String drawItemInSlot$ModifyVariable(String countOverride, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        return (stack instanceof WalletItemStack) ? "" : countOverride;
    }

    @Inject(at = @At("HEAD"), method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    private void drawItemInSlot$Inject(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo info) {
        if (!stack.isEmpty()) {
            if (stack instanceof WalletItemStack walletItemStack) {
                if (walletItemStack.getItemCount() != 1) {
                    float scale = 0.75f;

                    this.matrices.push();
                    this.matrices.scale(scale, scale, 1.0F);
                    this.matrices.translate(0.0f, 0.0f, 200.0f);

                    String string = walletItemStack.getItemCountShort();

                    this.drawText(textRenderer, string, (int) ((x + 19 - 2) / scale - textRenderer.getWidth(string)),
                            (int) ((y + 3 + (6 / scale)) / scale), 0xFFFFFF, true);
                    this.matrices.pop();
                }
            }
        }
    }
}
