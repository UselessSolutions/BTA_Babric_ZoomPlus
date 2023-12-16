package useless.zoomplus.mixin;

import net.minecraft.client.option.FloatOption;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.zoomplus.IZoomSettings;

@Mixin(value = GameSettings.class, remap = false)
public class GameSettingsMixin implements IZoomSettings {
    @Unique
    private final GameSettings thisAs = (GameSettings) (Object)this;
    @Unique
    public FloatOption maxFOV = new FloatOption(thisAs, "zoomplus.maxFOV", 1f);
    @Unique
    public FloatOption minFOV = new FloatOption(thisAs, "zoomplus.minFOV", 0f);
    @Inject(method = "getDisplayString(Lnet/minecraft/client/option/Option;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void customDisplayString(Option<Float> option, CallbackInfoReturnable<String> cir){
        if (option == maxFOV || option == minFOV){
            float value = option.value;
            cir.setReturnValue(String.format("%.1f", 0.5f + 179 * value));
        }
    }
    @Unique
    public FloatOption getMaxFov() {
        return maxFOV;
    }
    @Unique
    public FloatOption getMinFov() {
        return minFOV;
    }
}
