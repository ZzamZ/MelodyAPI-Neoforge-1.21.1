package net.zam.melodyapi.common.gui.casetest;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxMenu;
import net.zam.melodyapi.registry.MelodyCaseRewards;
import net.zam.melodyapi.registry.MelodyMenuTypes;

public class SingletonCaseMenu extends BaseLootBoxMenu<SingletonCaseMenu> {
    public static final MenuType<SingletonCaseMenu> TEST_CASE_MENU = MelodyMenuTypes.SINGLETON_CASE;

    public SingletonCaseMenu(int id, Inventory inv, Player player) {
        super(TEST_CASE_MENU, id, inv, MelodyCaseRewards.SINGLETON_CASE, player);
    }

    public SingletonCaseMenu(int id, Inventory inventory) {
        this(id, inventory, null);
    }
}