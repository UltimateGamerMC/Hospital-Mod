package com.beckytidus.hospitalmod.command;

import com.beckytidus.hospitalmod.util.Medic;
import com.beckytidus.hospitalmod.util.Supplies;
import com.beckytidus.hospitalmod.util.WardBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/** /hospital give | heal | room | vitals */
public class HospitalCommand {
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommand(dispatcher));
	}

	private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("hospital")
			.then(Commands.literal("give")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					Supplies.fullKit(player);
					context.getSource().sendSystemMessage(Component.translatable("msg.hospital-mod.command_give"));
					return 1;
				}))
			.then(Commands.literal("heal")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					player.setHealth(player.getMaxHealth());
					int cured = Medic.cureHarmful(player);
					context.getSource().sendSystemMessage(Component.translatable("msg.hospital-mod.command_heal", cured));
					return 1;
				}))
			.then(Commands.literal("vitals")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					context.getSource().sendSystemMessage(Medic.chart(player));
					return 1;
				}))
			.then(Commands.literal("room")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					ServerLevel level = context.getSource().getLevel();
					BlockPos origin = player.blockPosition();
					Direction facing = player.getDirection();
					WardBuilder.build(level, origin, facing);
					context.getSource().sendSystemMessage(Component.translatable("msg.hospital-mod.command_room"));
					return 1;
				})));
	}
}
