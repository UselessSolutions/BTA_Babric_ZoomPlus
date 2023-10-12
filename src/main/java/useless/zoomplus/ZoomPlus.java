package useless.zoomplus;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.GameSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZoomPlus implements ModInitializer {
    public static final String MOD_ID = "zoomplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static double getMinFOV(GameSettings gameSettings){
        return 0.5d + 179d * ((IZoomSettings)gameSettings).getMinFov().value;
    }
    public static double getMaxFOV(GameSettings gameSettings){
        return 0.5d + 179d * ((IZoomSettings)gameSettings).getMaxFov().value;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("ZoomPlus initialized.");
    }
}
