package net.zam.melodyapi.common.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.zam.melodyapi.MelodyAPI;

import java.util.function.Consumer;

public enum PausedComponent implements TooltipProvider {

    INSTANCE;

    public static final Codec<PausedComponent> CODEC = Codec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, PausedComponent> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    private static final Component TOOLTIP = Component.translatable("item." + MelodyAPI.MOD_ID + ".music_box.paused").withStyle(ChatFormatting.YELLOW);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        tooltipAdder.accept(TOOLTIP);
    }
}