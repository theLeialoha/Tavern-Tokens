package com.traverse.taverntokens.screens;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.wallet.WalletContainerMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WalletScreen extends AbstractContainerScreen<WalletContainerMenu> {

    private final ResourceLocation WALLET_GUI = new ResourceLocation(TavernTokens.MODID,
            "textures/gui/wallet_inventory.png");

    public WalletScreen(WalletContainerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int slots = this.menu.walletInventory.getContainerSize();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, WALLET_GUI);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(WALLET_GUI, x, y, 0, 0, 175, 173);

        // Adds Slots Dynamically
        for (int slot = 0; slot < slots; slot++) {
            int _x = 61 + 18 * (slot % 6);
            int _y = 7 + 18 * (Math.floorDiv(slot, 6));
            graphics.blit(WALLET_GUI, x + _x, y + _y, 176, 0, 18, 18);
        }

        // Renders Player Model
        drawEntity(graphics, x + 31, y + 70, 30, (float) (x + 31) - mouseX, (float) (y + 70 - 50) - mouseY,
                this.minecraft.player);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }

    public static void drawEntity(GuiGraphics guiGraphics, int x, int y, int size, float mouseX,
            float mouseY, LivingEntity livingEntity) {
        float h = (float) Math.atan((double) (mouseX / 40.0F));
        float l = (float) Math.atan((double) (mouseY / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(l * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float m = livingEntity.yBodyRot;
        float n = livingEntity.getYRot();
        float o = livingEntity.getXRot();
        float p = livingEntity.yHeadRotO;
        float q = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + h * 20.0F;
        livingEntity.setYRot(180.0F + h * 40.0F);
        livingEntity.setXRot(-l * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        drawEntity(guiGraphics, x, y, size, quaternionf, quaternionf2, livingEntity);
        livingEntity.yBodyRot = m;
        livingEntity.setYRot(n);
        livingEntity.setXRot(o);
        livingEntity.yHeadRotO = p;
        livingEntity.yHeadRot = q;
    }

    @SuppressWarnings("deprecation")
    public static void drawEntity(GuiGraphics guiGraphics, int x, int y, int size, Quaternionf quaternionf,
            @Nullable Quaternionf quaternionf2, LivingEntity livingEntity) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((double) x, (double) y, 50.0);
        guiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling((float) size, (float) size, (float) (-size)));
        guiGraphics.pose().mulPose(quaternionf);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.overrideCameraOrientation(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(),
                    guiGraphics.bufferSource(), 15728880);
        });
        guiGraphics.flush();
        entityRenderDispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

}