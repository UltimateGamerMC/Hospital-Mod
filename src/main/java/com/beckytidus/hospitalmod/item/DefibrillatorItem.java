package com.beckytidus.hospitalmod.item;

import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * A defibrillator. Shock a critical patient back to full health. Long cooldown between uses,
 * and it refuses to fire on somebody who is already stable.
 */
public class DefibrillatorItem extends Item {
	private static final int COOLDOWN_TICKS = 600;
	private static final double RANGE = 5.0;

	public DefibrillatorItem(Properties properties) {
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
		LivingEntity patient = findCritical(level, player);
		if (patient == null) {
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.no_patient"));
			return InteractionResult.FAIL;
		}
		shock(level, player, stack, patient);
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
		shock(level, player, stack, target);
		return InteractionResult.SUCCESS;
	}

	private void shock(Level level, Player player, ItemStack stack, LivingEntity patient) {
		patient.setHealth(patient.getMaxHealth());
		Medic.cureHarmful(patient);
		Medic.particles((ServerLevel) level, patient.getX(), patient.getY() + 1.0, patient.getZ(), ParticleTypes.ELECTRIC_SPARK, 24);
		Medic.soundAt(level, patient.getX(), patient.getY(), patient.getZ(), SoundEvents.BEACON_ACTIVATE, 0.8F, 1.4F);
		Medic.actionBar(player, Component.translatable("msg.hospital-mod.defibrillated", patient.getName()));
		player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
	}

	private LivingEntity findCritical(Level level, Player player) {
		AABB area = player.getBoundingBox().inflate(RANGE);
		LivingEntity best = null;
		double bestDistance = Double.MAX_VALUE;
		for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive)) {
			// Skip ourselves, the undead, and anyone who is already stable.
			if (entity == player || entity.isInvertedHealAndHarm() || entity.getHealth() >= entity.getMaxHealth() * 0.75F) {
				continue;
			}
			double distance = entity.distanceToSqr(player);
			if (distance < bestDistance) {
				bestDistance = distance;
				best = entity;
			}
		}
		return best;
	}
}
