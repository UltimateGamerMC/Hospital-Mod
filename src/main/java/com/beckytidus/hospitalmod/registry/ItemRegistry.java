package com.beckytidus.hospitalmod.registry;

import java.util.List;

import com.beckytidus.hospitalmod.HospitalMod;
import com.beckytidus.hospitalmod.item.DefibrillatorItem;
import com.beckytidus.hospitalmod.item.MedicalItem;
import com.beckytidus.hospitalmod.item.StethoscopeItem;
import com.beckytidus.hospitalmod.item.SyringeItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

/** Every medical tool and consumable. */
public class ItemRegistry {
	public static Item BANDAGE;
	public static Item MEDKIT;
	public static Item PAINKILLERS;
	public static Item SALINE_BAG;
	public static Item SYRINGE;
	public static Item STETHOSCOPE;
	public static Item DEFIBRILLATOR;
	public static Item SCALPEL;

	public static void register() {
		BANDAGE = item("bandage", properties -> new MedicalItem(properties.stacksTo(16),
			4.0F, true, List.of(), "msg.hospital-mod.bandaged", 40));

		MEDKIT = item("medkit", properties -> new MedicalItem(properties.stacksTo(8),
			10.0F, true, List.of(
				new MobEffectInstance(MobEffects.REGENERATION, 200, 0),
				new MobEffectInstance(MobEffects.ABSORPTION, 400, 0)),
			"msg.hospital-mod.medkitted", 200));

		PAINKILLERS = item("painkillers", properties -> new MedicalItem(properties.stacksTo(16),
			0.0F, true, List.of(
				new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0),
				new MobEffectInstance(MobEffects.RESISTANCE, 600, 0)),
			"msg.hospital-mod.painkillers", 600));

		SALINE_BAG = item("saline_bag", properties -> new Item(properties.stacksTo(16)));

		SYRINGE = item("syringe", properties -> new SyringeItem(properties.stacksTo(16)));
		STETHOSCOPE = item("stethoscope", properties -> new StethoscopeItem(properties.stacksTo(1)));
		DEFIBRILLATOR = item("defibrillator", properties -> new DefibrillatorItem(properties.stacksTo(1)));

		SCALPEL = item("scalpel", properties -> new Item(properties.sword(ToolMaterial.IRON, 2.0F, -1.6F)));
	}

	private static Item item(String name, java.util.function.Function<Item.Properties, Item> factory) {
		Identifier id = Identifier.fromNamespaceAndPath(HospitalMod.MOD_ID, name);
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(new Item.Properties().setId(key)));
	}
}
