package useless.zoomplus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.camera.EntityCamera;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.zoomplus.ZoomPlus;

@Mixin(value = EntityCamera.class, remap = false)
public class EntityCameraMixin {
    @Unique
    private int zoomOffset = 0;
    @Unique
    private boolean isPressedThisFrame = false;
    @Unique
    private Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
    @Inject(method = "getFov()D", at = @At("HEAD"), cancellable = true)
    private void modFov(CallbackInfoReturnable<Double> cir){
        double defaultFOV = (this.mc.gameSettings.FoV.value).floatValue() * 100 + 30;
        int zoomRadius = 2000;

        if (!isPressedThisFrame && this.mc.currentScreen == null){
            Mouse.getDWheel();
        }
        isPressedThisFrame = this.mc.gameSettings.keyZoom.isPressed();

        if (this.mc.gameSettings.keyZoom.isPressed() && this.mc.currentScreen == null) {
            defaultFOV *= .45d; // Default Zoom Mod
            zoomOffset -= Mouse.getDWheel();
        }
        else {
            zoomOffset = 0;
        }

        if (zoomOffset > zoomRadius){
            zoomOffset = zoomRadius;
        } else if (zoomOffset < -zoomRadius) {
            zoomOffset = -zoomRadius;
        }
        double fovy = defaultFOV;
        if (this.mc.gameSettings.keyZoom.isPressed() && this.mc.currentScreen == null) {
            if (zoomOffset >= 0){
                fovy = interpolate(defaultFOV, ZoomPlus.getMaxFOV(mc.gameSettings), ((float) zoomOffset) /zoomRadius);
            } else {
                fovy = interpolate(ZoomPlus.getMinFOV(mc.gameSettings), defaultFOV, 1 - ((float) zoomOffset) /(zoomRadius));
            }
        }
        cir.setReturnValue((fovy-30)/100d);

    }
    @Unique
    private double interpolate(double y1, double y2, double x){
        double x2 = ((1-Math.cos(x * Math.PI))/2);
        return (y1 * (1-x2) + y2 *x2);
    }
}
