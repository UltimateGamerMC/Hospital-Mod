package com.beckytidus.hospitalmod.item;

import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** A stethoscope. Right-click a patient for a vitals report, or the air for your own. */
public class StethoscopeItem extends Item {
	private static final int COOLDOWN_TICKS = 10;

	public StethoscopeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		Medic.chat(player, Medic.chart(player));
		Medic.soundAt(level, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, 0.4F, 0.8F);
		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		Level level = player.level();
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		Medic.chat(player, Medic.chart(target));
		Medic.actionBar(player, Medic.vitals(target));
		Medic.soundAt(level, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, 0.4F, 0.8F);
		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		return InteractionResult.SUCCESS;
	}
}
