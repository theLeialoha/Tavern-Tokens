package com.traverse.taverntokens.mixin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.traverse.taverntokens.item.BagItemTooltip;
import com.traverse.taverntokens.item.ClientBagItemTooltip;
import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    public abstract int drawString(Font font, String text, int x, int y, int color, boolean shadow);

    @Shadow
    protected abstract void renderTooltipInternal(Font font, List<ClientTooltipComponent> list2, int x, int y,
            ClientTooltipPositioner instance);

    @ModifyVariable(at = @At("HEAD"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private String renderItemDecorations(String countOverride, Font font, ItemStack stack,
            int x, int y) {
        return (stack instanceof WalletItemStack) ? "" : countOverride;
    }

    @Inject(at = @At("HEAD"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private void renderItemDecorations(Font font, ItemStack stack, int x, int y, String countOverride,
            CallbackInfo info) {
        if (!stack.isEmpty()) {
            if (stack instanceof WalletItemStack walletItemStack) {
                if (walletItemStack.getLongCount() != 1) {
                    float scale = 0.75f;

                    this.pose.pushPose();
                    this.pose.scale(scale, scale, 1.0F);
                    this.pose.translate(0.0f, 0.0f, 200.0f);

                    String string = walletItemStack.getItemCountShort();

                    this.drawString(font, string, (int) ((x + 19 - 2) / scale - font.width(string)),
                            (int) ((y + 3 + (6 / scale)) / scale), 0xFFFFFF, true);
                    this.pose.popPose();
                }
            }
        }
    }

    @Inject(method = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(Font font, List<Component> list, Optional<TooltipComponent> optional, int x, int y,
            CallbackInfo ci) {

        optional.ifPresent((component) -> {
            if (optional.get() instanceof BagItemTooltip tooltip) {
                List<ClientTooltipComponent> list2 = list.stream().map(Component::getVisualOrderText)
                        .map(ClientTooltipComponent::create).collect(Collectors.toList());

                list2.add(1, new ClientBagItemTooltip(tooltip));

                this.renderTooltipInternal(font, list2, x, y, DefaultTooltipPositioner.INSTANCE);
                ci.cancel();
            }
        });
    }
}
