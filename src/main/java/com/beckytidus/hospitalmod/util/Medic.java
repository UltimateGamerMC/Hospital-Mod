package com.beckytidus.hospitalmod.util;

import java.util.List;
import java.util.Locale;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;

/** Shared medical helpers: vitals text, cures, resting and effects. */
public final class Medic {
	private Medic() {
	}

	public static final ChatFormatting ACCENT = ChatFormatting.AQUA;
	public static final ChatFormatting WARN = ChatFormatting.GOLD;
	public static final ChatFormatting BAD = ChatFormatting.RED;
	public static final ChatFormatting GOOD = ChatFormatting.GREEN;

	/** Action-bar text. Sent as a packet so the same code runs on every Minecraft version. */
	public static void actionBar(Player player, Component message) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(message));
		}
	}

	/** Chat text. ServerPlayer is the only place sendSystemMessage exists on every version. */
	public static void chat(Player player, Component message) {
		if (player instanceof ServerPlayer serverPlayer) {
			serverPlayer.sendSystemMessage(message);
		}
	}

	/** Removes every harmful effect, returns how many were cleared. */
	public static int cureHarmful(LivingEntity entity) {
		int removed = 0;
		for (MobEffectInstance instance : List.copyOf(entity.getActiveEffects())) {
			Holder<MobEffect> effect = instance.getEffect();
			if (effect.value().getCategory() == MobEffectCategory.HARMFUL) {
				entity.removeEffect(effect);
				removed++;
			}
		}
		return removed;
	}

	public static boolean isHurt(LivingEntity entity) {
		return entity.getHealth() < entity.getMaxHealth();
	}

	public static String hearts(float health) {
		return String.format(Locale.ROOT, "%.1f", health / 2.0F);
	}

	public static Component healthLine(LivingEntity entity) {
		float health = entity.getHealth();
		float max = entity.getMaxHealth();
		float ratio = max <= 0.0F ? 0.0F : health / max;
		ChatFormatting colour = ratio > 0.66F ? GOOD : ratio > 0.33F ? WARN : BAD;
		return Component.literal(hearts(health) + "/" + hearts(max) + " \u2665").withStyle(colour);
	}

	/** A short vitals report for the action bar. */
	public static Component vitals(LivingEntity entity) {
		return Component.literal("")
			.append(Component.literal(entity.getName().getString()).withStyle(ACCENT))
			.append(Component.literal("  "))
			.append(healthLine(entity));
	}

	/** A longer report for chat, including every active effect. */
	public static Component chart(LivingEntity entity) {
		Component line = Component.literal("")
			.append(Component.literal("\u2695 ").withStyle(ACCENT))
			.append(Component.literal(entity.getName().getString()).withStyle(ChatFormatting.WHITE))
			.append(Component.literal("  ").withStyle(ChatFormatting.DARK_GRAY))
			.append(healthLine(entity));

		var effects = entity.getActiveEffects();
		if (effects.isEmpty()) {
			return line.copy().append(Component.literal("   no active effects").withStyle(ChatFormatting.GRAY));
		}
		var out = line.copy();
		for (MobEffectInstance instance : effects) {
			boolean good = instance.getEffect().value().getCategory() != MobEffectCategory.HARMFUL;
			String name = instance.getEffect().value().getDisplayName().getString();
			int seconds = instance.getDuration() / 20;
			out.append(Component.literal("\n   \u2022 " + name + " " + roman(instance.getAmplifier() + 1) + " (" + seconds + "s)")
				.withStyle(good ? GOOD : BAD));
		}
		return out;
	}

	private static String roman(int value) {
		return switch (value) {
			case 1 -> "I";
			case 2 -> "II";
			case 3 -> "III";
			case 4 -> "IV";
			case 5 -> "V";
			default -> Integer.toString(value);
		};
	}

	/** Puts a patient into a bed: heal, clear debuffs, regenerate and set spawn. */
	public static void restAt(ServerPlayer player, BlockPos bedPos) {
		player.heal(6.0F);
		cureHarmful(player);
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
		player.setRespawnPosition(new ServerPlayer.RespawnConfig(
			LevelData.RespawnData.of(player.level().dimension(), bedPos.above(), player.getYRot(), player.getXRot()), false), true);
	}

	public static void sound(Level level, BlockPos pos, SoundEvent sound, float volume, float pitch) {
		level.playSound(null, pos, sound, SoundSource.BLOCKS, volume, pitch);
	}

	public static void soundAt(Level level, double x, double y, double z, SoundEvent sound, float volume, float pitch) {
		level.playSound(null, x, y, z, sound, SoundSource.PLAYERS, volume, pitch);
	}

	public static void particles(ServerLevel level, double x, double y, double z, ParticleOptions particle, int count) {
		level.sendParticles(particle, x, y, z, count, 0.35, 0.35, 0.35, 0.02);
	}

	/** Gives an item, or drops it at the player's feet when the inventory is full. */
	public static void give(Player player, ItemStack stack) {
		if (player.getInventory().add(stack)) {
			return;
		}
		Level level = player.level();
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.addFreshEntity(new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), stack));
		}
	}
}
