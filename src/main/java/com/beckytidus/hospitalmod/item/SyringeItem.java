package com.beckytidus.hospitalmod.item;

import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A single-use syringe. Inject yourself for an adrenaline hit, or inject a patient.
 * On the undead it works as a sedative instead.
 */
public class SyringeItem extends Item {
	private static final int COOLDOWN_TICKS = 60;

	public SyringeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (player.getCooldowns().isOnCooldown(stack)) {
			return InteractionResult.FAIL;
		}
		player.addEffect(new MobEffectInstance(MobEffects.SPEED, 400, 1));
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 0));
		Medic.actionBar(player, Component.translatable("msg.hospital-mod.injected_self"));
		finish(level, player, stack);
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		Level level = player.level();
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (player.getCooldowns().isOnCooldown(stack)) {
			return InteractionResult.FAIL;
		}
		if (target.isInvertedHealAndHarm()) {
			// Sedative: calms undead instead of healing them.
			target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0));
			target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 1));
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.sedated", target.getName()));
		} else {
			target.heal(4.0F);
			target.addEffect(new MobEffectInstance(MobEffects.SPEED, 300, 0));
			target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 1));
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.injected_target", target.getName()));
		}
		Medic.particles((net.minecraft.server.level.ServerLevel) level, target.getX(), target.getY() + 1.0, target.getZ(),
			net.minecraft.core.particles.ParticleTypes.CRIT, 8);
		finish(level, player, stack);
		return InteractionResult.SUCCESS;
	}

	private void finish(Level level, Player player, ItemStack stack) {
		Medic.soundAt(level, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, 0.6F, 1.6F);
		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
		if (!player.isCreative()) {
			stack.consume(1, player);
		}
	}
}
