package net.zam.melodyapi.common.gui.casetest;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.zam.melodyapi.common.gui.cases.BaseLootBoxMenu;
import net.zam.melodyapi.registry.MelodyCaseRewards;
import net.zam.melodyapi.registry.MelodyMenuTypes;

public class TestCaseMenu extends BaseLootBoxMenu<TestCaseMenu> {
    public static final MenuType<TestCaseMenu> TEST_CASE_MENU = MelodyMenuTypes.TEST_CASE;

    public TestCaseMenu(int id, Inventory inv, Player player) {
        super(TEST_CASE_MENU, id, inv, MelodyCaseRewards.TEST_CASE, player);
    }

    public TestCaseMenu(int id, Inventory inventory) {
        this(id, inventory, null);
    }
}