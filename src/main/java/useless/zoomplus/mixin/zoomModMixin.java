package useless.zoomplus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPhotoMode;
import net.minecraft.client.option.enums.RenderDistance;
import net.minecraft.client.render.WorldRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldRenderer.class)
public class zoomModMixin{
    @Unique
    public boolean zoomPressed = false;
    @Unique
    public float zoomModifierOffset = 0;
    @Unique
    public float originalFOV = 90f;
    @Shadow
    private float farPlaneDistance;
    @Shadow
    private Minecraft mc;
    @Shadow
    private double cameraZoom;
    @Shadow
    private double cameraYaw;
    @Shadow
    private double cameraPitch;
    @Shadow
    private int rendererUpdateCount;


    /**
     * @author Useless
     * @reason Adding zoom modification
     */
    @Overwrite
    private void setupCameraTransform(float renderPartialTicks) {
        this.farPlaneDistance = (float)(((RenderDistance)this.mc.gameSettings.renderDistance.value).chunks * 16);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        float f1 = 0.07F;
        float fov = originalFOV = this.getFOVModifier(renderPartialTicks, true);
        if (this.mc.gameSettings.keyZoom.isPressed() && this.mc.currentScreen == null) {
            if (!zoomPressed){
                Mouse.getDWheel();
            }
            zoomPressed = true;
            float zoomModifierPreMath = zoomModifierOffset;
            zoomModifierOffset += -(float)Mouse.getDWheel() * 0.0005;
            final float firstFOVMod = 0.45f;
            fov *= firstFOVMod + (Math.sqrt(zoomModifierOffset + 1) - 1);
            if (fov < 0.5){
                fov = 0.5f;
                zoomModifierOffset = (float)(Math.pow((((fov- originalFOV * firstFOVMod)/originalFOV) + 1),2)-1);
            } else if (fov > 160) {
                fov = 160f;
                zoomModifierOffset = (float)(Math.pow((((fov- originalFOV * firstFOVMod)/originalFOV) + 1),2)-1);
            }

        }
        else {
            zoomModifierOffset = 0;
            zoomPressed = false;
        }

        if (this.mc.currentScreen instanceof GuiPhotoMode) {
            double div = Math.pow(2.0, (double)((GuiPhotoMode)this.mc.currentScreen).getZoom(renderPartialTicks));
            GL11.glTranslatef(1.0F, 1.0F, 0.0F);
            GL11.glOrtho(
                    0.0,
                    (double)this.mc.resolution.width / div,
                    0.0,
                    (double)this.mc.resolution.height / div,
                    (double)(this.farPlaneDistance * -2.0F),
                    (double)(this.farPlaneDistance * 2.0F)
            );
            GL11.glTranslatef(
                    ((GuiPhotoMode)this.mc.currentScreen).getPanX(renderPartialTicks), -((GuiPhotoMode)this.mc.currentScreen).getPanY(renderPartialTicks), 0.0F
            );
        } else if (this.cameraZoom != 1.0) {
            GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0);
            GLU.gluPerspective(fov, (float)this.mc.resolution.width / (float)this.mc.resolution.height, 0.05F, this.farPlaneDistance * 2.0F);
        } else {
            GLU.gluPerspective(fov, (float)this.mc.resolution.width / (float)this.mc.resolution.height, 0.05F, this.farPlaneDistance * 2.0F);
        }

        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.hurtCameraEffect(renderPartialTicks);
        if (this.mc.gameSettings.viewBobbing.value && !(this.mc.currentScreen instanceof GuiPhotoMode)) {
            this.setupViewBobbing(renderPartialTicks);
        }

        float f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * renderPartialTicks;
        if (f2 > 0.0F) {
            float f3 = 5.0F / (f2 * f2 + 5.0F) - f2 * 0.04F;
            f3 *= f3;
            GL11.glRotatef(((float)this.rendererUpdateCount + renderPartialTicks) * 20.0F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
            GL11.glRotatef(-((float)this.rendererUpdateCount + renderPartialTicks) * 20.0F, 0.0F, 1.0F, 1.0F);
        }

        this.orientCamera(renderPartialTicks);
    }

    @Shadow
    private void orientCamera(float renderPartialTicks) {
    }

    @Shadow
    private void setupViewBobbing(float renderPartialTicks) {
    }

    @Shadow
    private void hurtCameraEffect(float renderPartialTicks) {
    }

    @Shadow
    private float getFOVModifier(float renderPartialTicks, boolean b) {
        return  renderPartialTicks;
    }

}
