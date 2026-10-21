package com.beckytidus.hospitalmod.block;

import com.beckytidus.hospitalmod.util.Medic;
import com.beckytidus.hospitalmod.util.Supplies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** A wall cabinet that restocks itself with medical supplies. */
public class MedicalCabinetBlock extends FacingBlock {
	private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

	public MedicalCabinetBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		long now = level.getGameTime();
		long cooldown = Supplies.cooldown(player, now);
		if (cooldown > 0L) {
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.cabinet_restocking", (cooldown + 19L) / 20L));
			return InteractionResult.SUCCESS;
		}
		Supplies.markUsed(player, now);
		Supplies.dispense(level, player);
		Supplies.sparkle((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
		Medic.actionBar(player, Component.translatable("msg.hospital-mod.cabinet_used"));
		return InteractionResult.SUCCESS;
	}
}
