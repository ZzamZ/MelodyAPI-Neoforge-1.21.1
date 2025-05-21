package net.zam.melodyapi.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import net.zam.melodyapi.MelodyAPI;

import java.util.*;

public class MelodySavedData extends SavedData {
    private final Map<UUID, List<Item>> items = new HashMap<>();

    public static MelodySavedData create() {
        return new MelodySavedData();
    }

    public static MelodySavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        MelodySavedData data = create();
        ListTag listTag = (ListTag) tag.get("data");

        for (Tag entry : listTag) {
            CompoundTag compoundTag = (CompoundTag) entry;

            UUID uuid = compoundTag.getUUID("uuid");
            List<Item> items = new ArrayList<>();

            ListTag itemsTag = (ListTag) compoundTag.get("items");
            for (Tag itemTag : itemsTag) {
                items.add(BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemTag.getAsString())));
            }

            data.items.put(uuid, items);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();

        for (UUID uuid : items.keySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("uuid", uuid);

            ListTag itemsTag = new ListTag();
            for (Item item : items.get(uuid)) {
                itemsTag.add(StringTag.valueOf(BuiltInRegistries.ITEM.getKey(item).toString()));
            }

            playerTag.put("items", itemsTag);
            listTag.add(playerTag);
        }

        tag.put("data", listTag);
        return tag;
    }

    public static void setCollected(MinecraftServer server, Player player, Item item) {
        // TODO: may cause crash due null data fix type
        MelodySavedData data = server.overworld().getDataStorage().computeIfAbsent(new Factory<>(MelodySavedData::create, MelodySavedData::load, null), MelodyAPI.MOD_ID);
        data.items.computeIfAbsent(player.getUUID(), uuid -> new ArrayList<>()).add(item);
        data.setDirty();
    }

    public static boolean isCollected(MinecraftServer server, Player player, Item item) {
        MelodySavedData data = server.overworld().getDataStorage().computeIfAbsent(new Factory<>(MelodySavedData::create, MelodySavedData::load, null), MelodyAPI.MOD_ID);
        return data.items.containsKey(player.getUUID()) && data.items.get(player.getUUID()).contains(item);
    }
}