package com.beckytidus.hospitalmod.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.beckytidus.hospitalmod.registry.ItemRegistry;

/** The supply cabinet's restock timer and loot table. */
public final class Supplies {
	private Supplies() {
	}

	/** Ticks before the same cabinet can be raided again (30 seconds). */
	public static final int RESTOCK_TICKS = 600;

	private static final Map<UUID, Long> LAST_USED = new ConcurrentHashMap<>();

	/** Returns the ticks still to wait, or 0 when ready. */
	public static long cooldown(Player player, long now) {
		Long last = LAST_USED.get(player.getUUID());
		if (last == null) {
			return 0L;
		}
		long left = RESTOCK_TICKS - (now - last);
		return Math.max(0L, left);
	}

	public static void markUsed(Player player, long now) {
		LAST_USED.put(player.getUUID(), now);
	}

	/** Two or three random supplies, weighted so bandages are common. */
	public static void dispense(Level level, Player player) {
		int rolls = 2 + level.getRandom().nextInt(2);
		for (int i = 0; i < rolls; i++) {
			int pick = level.getRandom().nextInt(10);
			ItemStack stack = switch (pick) {
				case 0, 1, 2, 3 -> new ItemStack(ItemRegistry.BANDAGE, 1 + level.getRandom().nextInt(2));
				case 4, 5 -> new ItemStack(ItemRegistry.PAINKILLERS);
				case 6, 7 -> new ItemStack(ItemRegistry.SALINE_BAG);
				case 8 -> new ItemStack(ItemRegistry.SYRINGE, 1 + level.getRandom().nextInt(2));
				default -> new ItemStack(ItemRegistry.MEDKIT);
			};
			Medic.give(player, stack);
		}
	}

	/** The static kit handed out by /hospital give. */
	public static void fullKit(Player player) {
		Medic.give(player, new ItemStack(ItemRegistry.BANDAGE, 4));
		Medic.give(player, new ItemStack(ItemRegistry.MEDKIT));
		Medic.give(player, new ItemStack(ItemRegistry.PAINKILLERS, 2));
		Medic.give(player, new ItemStack(ItemRegistry.SALINE_BAG, 2));
		Medic.give(player, new ItemStack(ItemRegistry.SYRINGE, 2));
		Medic.give(player, new ItemStack(ItemRegistry.STETHOSCOPE));
		Medic.give(player, new ItemStack(ItemRegistry.SCALPEL));
		Medic.give(player, new ItemStack(ItemRegistry.DEFIBRILLATOR));
	}

	public static void sparkle(ServerLevel level, double x, double y, double z) {
		Medic.particles(level, x, y, z, ParticleTypes.HAPPY_VILLAGER, 8);
		Medic.soundAt(level, x, y, z, SoundEvents.ITEM_PICKUP, 0.5F, 1.3F);
	}
}
