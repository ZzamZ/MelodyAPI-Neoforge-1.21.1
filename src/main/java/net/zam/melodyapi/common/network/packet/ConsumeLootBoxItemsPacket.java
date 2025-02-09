package net.zam.melodyapi.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.zam.melodyapi.api.gui.BaseLootBoxMenu;

import java.util.function.Supplier;

public class ConsumeLootBoxItemsPacket {

    private final ItemStack keyItem;
    private final ItemStack caseItem;

    public ConsumeLootBoxItemsPacket(final ItemStack keyItem, final ItemStack caseItem) {
        this.keyItem = keyItem;
        this.caseItem = caseItem;
    }

    public ConsumeLootBoxItemsPacket(FriendlyByteBuf buf) {
        this.keyItem = buf.readItem();
        this.caseItem = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItemStack(keyItem, true);
        buf.writeItemStack(caseItem, true);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(player.containerMenu instanceof BaseLootBoxMenu) {
                ((BaseLootBoxMenu) player.containerMenu).consumeItems(player, keyItem, caseItem);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
