package useless.zoomplus;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.function.Function;

public class ModMenuModule implements ModMenuApi {
    @Override
    public String getModId() {
        return ZoomPlus.MOD_ID;
    }

    @Override
    public Function<GuiScreen, ? extends GuiScreen> getConfigScreenFactory() {
        return (screenBase -> new GuiOptionsPageZoomPlus(screenBase, ((Minecraft) FabricLoader.getInstance().getGameInstance()).gameSettings));
    }
}
