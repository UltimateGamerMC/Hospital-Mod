package com.beckytidus.hospitalmod;

import com.beckytidus.hospitalmod.command.HospitalCommand;
import com.beckytidus.hospitalmod.registry.BlockRegistry;
import com.beckytidus.hospitalmod.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HospitalMod implements ModInitializer {
	public static final String MOD_ID = "hospital-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Hospital Mod...");

		BlockRegistry.register();
		ItemRegistry.register();
		HospitalCommand.register();

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
			output.accept(BlockRegistry.HOSPITAL_BED.asItem());
			output.accept(BlockRegistry.IV_STAND.asItem());
			output.accept(BlockRegistry.HEART_MONITOR.asItem());
			output.accept(BlockRegistry.MEDICAL_CABINET.asItem());
			output.accept(BlockRegistry.HOSPITAL_FLOOR.asItem());
			output.accept(BlockRegistry.HOSPITAL_WALL.asItem());
			output.accept(BlockRegistry.SHARPS_CONTAINER.asItem());
			output.accept(BlockRegistry.BIOHAZARD_SIGN.asItem());
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
			output.accept(ItemRegistry.STETHOSCOPE);
			output.accept(ItemRegistry.SCALPEL);
			output.accept(ItemRegistry.SYRINGE);
			output.accept(ItemRegistry.DEFIBRILLATOR);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
			output.accept(ItemRegistry.BANDAGE);
			output.accept(ItemRegistry.MEDKIT);
			output.accept(ItemRegistry.PAINKILLERS);
			output.accept(ItemRegistry.SALINE_BAG);
			output.accept(ItemRegistry.BIOHAZARD_BAG);
		});

		LOGGER.info("Hospital Mod loaded successfully!");
	}
}
