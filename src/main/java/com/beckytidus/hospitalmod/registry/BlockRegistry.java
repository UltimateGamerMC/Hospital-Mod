package com.beckytidus.hospitalmod.registry;

import java.util.function.Function;

import com.beckytidus.hospitalmod.HospitalMod;
import com.beckytidus.hospitalmod.block.HeartMonitorBlock;
import com.beckytidus.hospitalmod.block.HospitalBedBlock;
import com.beckytidus.hospitalmod.block.IvStandBlock;
import com.beckytidus.hospitalmod.block.MedicalCabinetBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/** Every block in the hospital, plus the matching block item. */
public class BlockRegistry {
	public static Block HOSPITAL_FLOOR;
	public static Block HOSPITAL_WALL;
	public static Block MEDICAL_CABINET;
	public static Block HOSPITAL_BED;
	public static Block IV_STAND;
	public static Block HEART_MONITOR;

	public static void register() {
		HOSPITAL_FLOOR = block("hospital_floor", Block::new, props(MapColor.QUARTZ).strength(1.6F, 6.0F).sound(SoundType.STONE));
		HOSPITAL_WALL = block("hospital_wall", Block::new, props(MapColor.SNOW).strength(1.6F, 6.0F).sound(SoundType.STONE));

		MEDICAL_CABINET = block("medical_cabinet", MedicalCabinetBlock::new, props(MapColor.SNOW).strength(2.0F, 6.0F).sound(SoundType.METAL));
		HOSPITAL_BED = block("hospital_bed", HospitalBedBlock::new, props(MapColor.COLOR_LIGHT_BLUE).strength(1.2F, 4.0F).sound(SoundType.WOOL).noOcclusion());
		IV_STAND = block("iv_stand", IvStandBlock::new, props(MapColor.METAL).strength(1.0F, 4.0F).sound(SoundType.METAL).noOcclusion());
		HEART_MONITOR = block("heart_monitor", HeartMonitorBlock::new, props(MapColor.METAL).strength(1.4F, 4.0F).sound(SoundType.METAL).noOcclusion());
	}

	private static BlockBehaviour.Properties props(MapColor mapColor) {
		return BlockBehaviour.Properties.of().mapColor(mapColor);
	}

	private static Block block(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		Identifier id = Identifier.fromNamespaceAndPath(HospitalMod.MOD_ID, name);
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(properties.setId(blockKey)));

		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		Registry.register(BuiltInRegistries.ITEM, itemKey,
			new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		return block;
	}
}
