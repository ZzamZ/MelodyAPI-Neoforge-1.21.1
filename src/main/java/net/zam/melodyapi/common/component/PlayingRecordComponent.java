package net.zam.melodyapi.common.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.zam.melodyapi.MelodyAPI;

import java.util.function.Consumer;

public record PlayingRecordComponent(ItemStack stack) implements TooltipProvider {

    public static final Codec<PlayingRecordComponent> CODEC = ItemStack.CODEC.xmap(PlayingRecordComponent::new, PlayingRecordComponent::stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayingRecordComponent> STREAM_CODEC = ItemStack.STREAM_CODEC.map(PlayingRecordComponent::new, PlayingRecordComponent::stack);

    private static final Component RECORDS = Component.translatable("item." + MelodyAPI.MOD_ID + ".music_box.records");

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (!this.stack.isEmpty()) {
            tooltipAdder.accept(Component.empty());
            tooltipAdder.accept(RECORDS);
            this.stack.addToTooltip(DataComponents.JUKEBOX_PLAYABLE, context, tooltipAdder, tooltipFlag);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        PlayingRecordComponent that = (PlayingRecordComponent) o;
        return ItemStack.matches(this.stack, that.stack);
    }

    @Override
    public int hashCode() {
        return ItemStack.hashItemAndComponents(this.stack);
    }
}