package com.beckytidus.hospitalmod.item;

import java.util.List;

import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A consumable medical item. Bandages, medkits and painkillers are all the same code with
 * different numbers: heal, clear debuffs, grant buffs, then burn a cooldown.
 */
public class MedicalItem extends Item {
	private final float heal;
	private final boolean cureHarmful;
	private final List<MobEffectInstance> grants;
	private final String messageKey;
	private final int cooldownTicks;

	public MedicalItem(Properties properties, float heal, boolean cureHarmful, List<MobEffectInstance> grants,
			String messageKey, int cooldownTicks) {
		super(properties);
		this.heal = heal;
		this.cureHarmful = cureHarmful;
		this.grants = grants;
		this.messageKey = messageKey;
		this.cooldownTicks = cooldownTicks;
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
		if (heal <= 0.0F && !cureHarmful && grants.isEmpty()) {
			return InteractionResult.FAIL;
		}
		if (heal <= 0.0F && !Medic.isHurt(player) && Medic.cureHarmful(player) == 0 && grants.isEmpty()) {
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.already_healthy"));
			return InteractionResult.FAIL;
		}
		if (heal > 0.0F) {
			player.heal(heal);
		}
		if (cureHarmful) {
			Medic.cureHarmful(player);
		}
		for (MobEffectInstance grant : grants) {
			player.addEffect(new MobEffectInstance(grant));
		}
		Medic.actionBar(player, Component.translatable(messageKey));
		Medic.soundAt(level, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, 0.6F, 1.5F);
		if (cooldownTicks > 0) {
			player.getCooldowns().addCooldown(stack, cooldownTicks);
		}
		if (!player.isCreative()) {
			stack.consume(1, player);
		}
		return InteractionResult.SUCCESS;
	}
}
