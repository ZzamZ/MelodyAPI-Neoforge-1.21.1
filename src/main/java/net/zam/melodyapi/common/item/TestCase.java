package net.zam.melodyapi.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zam.melodyapi.common.gui.casetest.TestCaseMenu;

public class TestCase extends Item {
    public TestCase(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, ignoredPlayer) -> new TestCaseMenu(id, inv),
                    Component.translatable("container.test_case")
            ));
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}