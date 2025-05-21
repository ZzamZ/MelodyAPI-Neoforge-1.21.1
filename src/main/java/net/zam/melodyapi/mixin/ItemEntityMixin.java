package net.zam.melodyapi.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.common.item.MusicBoxItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getItem();

    @Inject(
        method = "tick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void melody$onTick(CallbackInfo ci) {
        if (this.getItem().getItem() instanceof MusicBoxItem item) {
            if (item.onEntityItemUpdate(this.getItem(), (ItemEntity)(Object) this)) {
                ci.cancel();
            }
        }
    }
}