package useless.zoomplus;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageOptionBase;
import net.minecraft.client.option.GameSettings;

public class GuiOptionsPageZoomPlus extends GuiOptionsPageOptionBase {
    public GuiOptionsPageZoomPlus(GuiScreen parent, GameSettings settings) {
        super(parent, settings);
        IZoomSettings iZoomSettings = (IZoomSettings)settings;
        this.addOptionsCategory("options.zoomplus.category", iZoomSettings.getMinFov(), iZoomSettings.getMaxFov());
    }
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x,y,renderPartialTicks);
        if (!ZoomPlus.halpPresent){
            fontRenderer.drawString("Text might not translate properly without HalpLibe!", 10, 65, 0xFFFFFF);
        }

    }
}
