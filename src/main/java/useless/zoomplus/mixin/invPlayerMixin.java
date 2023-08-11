package useless.zoomplus.mixin;

import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import useless.zoomplus.ZoomPlus;

@Mixin(value = InventoryPlayer.class, remap = false)
public class invPlayerMixin {

    @Shadow
    public int currentItem;
    @Shadow
    public int hotbarOffset;


    /**
     * @author Useless
     * @reason Prevent Scrolling while zooming
     */
    @Overwrite
    public void changeCurrentItem(int i) {
        if (!ZoomPlus.mc.gameSettings.keyZoom.isPressed()) {
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentItem -= i;
            while (this.currentItem < this.hotbarOffset) {
                this.currentItem += 9;
            }
            while (this.currentItem >= 9 + this.hotbarOffset) {
                this.currentItem -= 9;
            }
        }
    }

}
