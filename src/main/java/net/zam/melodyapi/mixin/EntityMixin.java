package net.zam.melodyapi.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.zam.melodyapi.common.util.EntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityDataSaver {
    @Unique private CompoundTag melody$persistentData;

    @Override
    public CompoundTag melody$getPersistentData() {
        if (this.melody$persistentData == null) {
            this.melody$persistentData = new CompoundTag();
        }

        return this.melody$persistentData;
    }

    @Inject(
        method = "save",
        at = @At("HEAD")
    )
    protected void melody$addAdditionalSaveData(CompoundTag tag, CallbackInfoReturnable<Boolean> cir) {
        if (this.melody$persistentData != null) {
            tag.put("melodyapi.persistentData", this.melody$persistentData);
        }
    }

    @Inject(
        method = "load",
        at = @At("HEAD")
    )
    protected void melody$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("melodyapi.persistentData", 10)) {
            this.melody$persistentData = tag.getCompound("melodyapi.persistentData");
        }
    }
}