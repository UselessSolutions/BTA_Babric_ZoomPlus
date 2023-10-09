package useless.zoomplus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WorldRenderer.class, remap = false)
public class WorldRendererMixin {
    @Shadow
    private Minecraft mc;
    @Unique
    private int zoomOffset = 0;

    @Unique
    private boolean isPressedThisFrame = false;

    @Redirect(method = "setupCameraTransform(F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/GLU;gluPerspective(FFFF)V"))
    private void modZoom(float fovy, float aspect, float zNear, float zFar){
        float originalFov = this.getFOVModifier(0, true);
        float fovMax = 160f;
        float fovMin = 0.5f;
        float fovDefaultZoom = originalFov * 0.45f;

        int zoomRadius = 2000;

        if (!isPressedThisFrame){
            Mouse.getDWheel();
        }
        isPressedThisFrame = this.mc.gameSettings.keyZoom.isPressed();

        if (this.mc.gameSettings.keyZoom.isPressed() && this.mc.currentScreen == null) {
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

        if (this.mc.gameSettings.keyZoom.isPressed() && this.mc.currentScreen == null) {
            if (zoomOffset >= 0){
                fovy = interpolate(fovDefaultZoom, fovMax, ((float) zoomOffset) /zoomRadius);
            } else {
                fovy = interpolate(fovMin, fovDefaultZoom, 1 - ((float) zoomOffset) /(zoomRadius));
            }

            if (fovy > fovMax){
                fovy = fovMax;
            } else if (fovy < fovMin) {
                fovy = fovMin;
            }
        }


        GLU.gluPerspective(fovy, aspect, zNear, zFar);
    }

    @Unique
    private float interpolate(float y1, float y2, float x){
        float x2 = (float) ((1-Math.cos(x * Math.PI))/2);
        return (y1 * (1-x2) + y2 *x2);
    }
    @Shadow
    private float getFOVModifier(float renderPartialTicks, boolean b) {
        return  renderPartialTicks;
    }

}
