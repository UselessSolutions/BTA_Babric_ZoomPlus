package useless.zoomplus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InventoryPlayer.class, remap = false)
public class invPlayerMixin {
    @Unique
    private final Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
    @Inject(method = "changeCurrentItem(I)V", at = @At("HEAD"), cancellable = true)
    public void stopHotbarSwap(int i, CallbackInfo ci) {
        if (mc.gameSettings.keyZoom.isPressed()) {
            ci.cancel();}
    }

}
