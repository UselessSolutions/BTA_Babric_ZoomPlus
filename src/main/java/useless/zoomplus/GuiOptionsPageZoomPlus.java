package useless.zoomplus;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptions;
import net.minecraft.client.gui.options.components.FloatOptionComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.option.GameSettings;

public class GuiOptionsPageZoomPlus extends GuiScreen {
    public static GameSettings gameSettings = Minecraft.getMinecraft(Minecraft.class).gameSettings;
    public static IZoomSettings modSettings = (IZoomSettings) gameSettings;
    public static final OptionsPage zoomOptions = new OptionsPage("options.zoomplus.category")
            .withComponent(new OptionsCategory("options.zoomplus.category")
                    .withComponent(new FloatOptionComponent(modSettings.getMaxFov()))
                    .withComponent(new FloatOptionComponent(modSettings.getMinFov())));
    static {
//        OptionsPages.register(zoomOptions);
    }
    public static GuiOptions createGui(GuiScreen parent){
        return new GuiOptions(parent, ((Minecraft) FabricLoader.getInstance().getGameInstance()).gameSettings, zoomOptions);
    }
}
