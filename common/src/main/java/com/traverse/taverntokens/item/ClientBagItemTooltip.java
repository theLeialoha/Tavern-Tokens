package com.traverse.taverntokens.item;

import com.traverse.taverntokens.TavernTokens;
import com.traverse.taverntokens.wallet.WalletItemStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

public class ClientBagItemTooltip implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TavernTokens.MODID,
            "textures/gui/money_bag.png");
    private final NonNullList<WalletItemStack> items;
    private final int COLUMNS = 6;
    private final int ROWS = 4;

    private final int PADDING = 10;
    private final int SLOT_SIZE = 18;

    public ClientBagItemTooltip(BagItemTooltip bagTooltip) {
        this.items = bagTooltip.getItems();
    }

    public int getHeight() {
        int height = this.gridSizeY();
        return (height == 0) ? 0 : (height * SLOT_SIZE + PADDING + 2);
    }

    public int getWidth(Font font) {
        int width = this.gridSizeX();
        return (width == 0) ? 0 : (width * SLOT_SIZE + PADDING);
    }

    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        int row = this.gridSizeX();
        int column = this.gridSizeY();
        int pad = PADDING / 2;

        if (row == 0)
            return;

        this.drawMenu(x, y, row, column, guiGraphics);

        for (int index = 0; index < this.items.size(); index++) {
            int x2 = index % row, y2 = Math.floorDiv(index, row);
            int columnCount = Math.min(this.items.size() - (y2 * row), row);
            int offset = (getWidth(font) - PADDING - (columnCount * SLOT_SIZE)) / 2;

            if (y2 < column)
                this.renderSlot(x + (x2 * SLOT_SIZE) + offset + pad, y + (y2 * SLOT_SIZE) + pad, index, guiGraphics,
                        font);
        }
    }

    private void renderSlot(int x, int y, int k, GuiGraphics guiGraphics, Font font) {
        if (k < this.items.size()) {
            WalletItemStack walletStack = this.items.get(k);
            this.blit(guiGraphics, x, y, Texture.SLOT);
            guiGraphics.renderItem(walletStack, x + 1, y + 1, k);
            guiGraphics.renderItemDecorations(font, walletStack, x + 1, y + 1);
        }
    }

    private void drawMenu(int x, int y, int w, int h, GuiGraphics guiGraphics) {
        Texture bg = Texture.BACKGROUND;

        float maxWidth = (COLUMNS * SLOT_SIZE) + PADDING;
        float maxHeight = (ROWS * SLOT_SIZE) + PADDING;

        int guiWidth = (w * SLOT_SIZE) + PADDING;
        int guiHeight = (h * SLOT_SIZE) + PADDING;
        int halfGuiWidth = (int) Math.ceil(guiWidth / 2f);
        int halfGuiHeight = (int) Math.ceil(guiHeight / 2f);

        float horizontalScale = guiWidth / (maxWidth * 1F);
        float verticalScale = guiHeight / (maxHeight * 1F);

        int imageWidth = (int) Math.ceil(bg.w * horizontalScale);
        int imageHeight = (int) Math.ceil(bg.h * verticalScale);
        int halfImageWidth = (int) Math.ceil(imageWidth / 2f);
        int halfImageHeight = (int) Math.ceil(imageHeight / 2f);

        int offsetX = guiWidth - halfGuiWidth;
        int offsetY = guiHeight - halfGuiHeight;
        int offsetImageX = bg.w - halfImageWidth;
        int offsetImageY = bg.h - halfImageHeight;

        guiGraphics.blit(TEXTURE_LOCATION, x, y, 0, bg.x, bg.y,
                halfImageWidth, halfImageHeight, 128, 128);
        guiGraphics.blit(TEXTURE_LOCATION, x + offsetX, y, 0, bg.x + offsetImageX, bg.y,
                imageWidth - halfImageWidth, halfImageHeight, 128, 128);
        guiGraphics.blit(TEXTURE_LOCATION, x, y + offsetY, 0, bg.x, bg.y + offsetImageY,
                halfImageWidth, imageHeight - halfImageHeight, 128, 128);
        guiGraphics.blit(TEXTURE_LOCATION, x + offsetX, y + offsetY, 0, bg.x + offsetImageX, bg.y + offsetImageY,
                imageWidth - halfImageWidth, imageHeight - halfImageHeight, 128, 128);
    }

    private void blit(GuiGraphics guiGraphics, int i, int j, Texture texture) {
        guiGraphics.blit(TEXTURE_LOCATION, i, j, 0, (float) texture.x, (float) texture.y, texture.w, texture.h, 128,
                128);
    }

    private int gridSizeX() {
        return (this.items.size() > 0) ? COLUMNS : 0;
    }

    private int gridSizeY() {
        return Math.min((int) Math.ceil(this.items.size() / (float) COLUMNS), ROWS);
    }

    enum Texture {
        SLOT(0, 82, 18, 18),
        BACKGROUND(0, 0, 118, 82);

        final int x, y, w, h;

        Texture(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}
