package net.zam.melodyapi.common.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.zam.melodyapi.MelodyAPI;
import net.zam.melodyapi.common.data.MelodySavedData;
import net.zam.melodyapi.common.item.rarity.Rarity;
import net.zam.melodyapi.common.item.rarity.RarityItem;
import net.zam.melodyapi.registry.MelodyCriteriaTriggers;

public class ClaimRewardPacket implements CustomPacketPayload {
    public static final Type<ClaimRewardPacket> TYPE = new Type<>(MelodyAPI.id("claim_reward"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Component> COMPONENT_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(ComponentSerialization.CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClaimRewardPacket> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, packet -> packet.reward().getItemStack(),
        ByteBufCodecs.STRING_UTF8, packet -> packet.reward.getRarity().name(),
        ByteBufCodecs.STRING_UTF8, packet -> packet.playerName,
        COMPONENT_CODEC, packet -> packet.caseTitle,
        ResourceLocation.STREAM_CODEC, packet -> packet.id,
        ByteBufCodecs.BOOL, packet -> packet.isComplete,
        ClaimRewardPacket::new
    );

    private final RarityItem reward;
    private final String playerName;
    private final Component caseTitle;
    private final ResourceLocation id;
    private final boolean isComplete;

    public ClaimRewardPacket(RarityItem reward, String playerName, Component caseTitle, ResourceLocation id, boolean isComplete) {
        this.reward = reward;
        this.playerName = playerName;
        this.caseTitle = caseTitle;
        this.id = id;
        this.isComplete = isComplete;
    }

    public ClaimRewardPacket(ItemStack itemStack, String rarity, String playerName, Component caseTitle, ResourceLocation id, boolean isComplete) {
        this.reward = new RarityItem(itemStack, Rarity.valueOf(rarity));
        this.playerName = playerName;
        this.caseTitle = caseTitle;
        this.id = id;
        this.isComplete = isComplete;
    }

    @Override
    public Type<ClaimRewardPacket> type() {
        return TYPE;
    }

    public static void handle(ClaimRewardPacket packet, ServerPlayNetworking.Context ctx) {
        ServerPlayer player = ctx.player();
        player.closeContainer();
        ItemStack stack = packet.reward().getItemStack().copy();
        boolean added = player.getInventory().add(stack);
        if (added && stack.isEmpty()) {
            player.inventoryMenu.broadcastChanges();
        } else {
            ItemEntity itemEntity = player.drop(stack, false);
            if (itemEntity != null) {
                itemEntity.setNoPickUpDelay();
                itemEntity.setTarget(player.getUUID());
            }
        }
        MelodySavedData.setCollected(player.getServer(), player, packet.reward().getItemStack().getItem());

        Component announcement = createAnnouncement(packet.playerName, packet.reward, packet.caseTitle);
        player.server.getPlayerList().broadcastSystemMessage(announcement, false);

        MelodyCriteriaTriggers.CASE_REWARD.trigger(player, packet.reward.getItemStack(), packet.id);

        if (packet.isComplete) {
            MelodyCriteriaTriggers.CASE_COMPLETITION.trigger(player, packet.id);
        }
    }

    private static Component createAnnouncement(String playerName, RarityItem reward, Component caseTitle) {
        ItemStack itemStack = reward.getItemStack();
        Component itemNameOrDescription = itemStack.getItem().components().has(DataComponents.JUKEBOX_PLAYABLE)
            ? Component.translatable(itemStack.getDescriptionId() + ".desc")
            : itemStack.getHoverName();

        Component playerNameComponent = Component.literal(playerName).withStyle(style -> style.withColor(0x55FF55));
        Component itemNameOrDescriptionComponent = itemNameOrDescription.copy().withStyle(style -> style.withColor(reward.getRarity().getColor()));
        Component caseNameComponent = caseTitle.copy().withStyle(style -> style.withColor(0x90EE90));
        Component openBracket = Component.literal("[").withStyle(style -> style.withColor(0xADD8E6));
        Component closeBracket = Component.literal("]").withStyle(style -> style.withColor(0xADD8E6));

        return Component.literal("")
            .append(playerNameComponent)
            .append(Component.literal(" opened a ").withStyle(style -> style.withColor(0xADD8E6)))
            .append(openBracket)
            .append(caseNameComponent)
            .append(closeBracket)
            .append(Component.literal(" and received ").withStyle(style -> style.withColor(0xADD8E6)))
            .append(Component.literal("[").withStyle(style -> style.withColor(reward.getRarity().getColor())))
            .append(itemNameOrDescriptionComponent)
            .append(Component.literal("]").withStyle(style -> style.withColor(reward.getRarity().getColor())));
    }

    public RarityItem reward() {
        return reward;
    }
}