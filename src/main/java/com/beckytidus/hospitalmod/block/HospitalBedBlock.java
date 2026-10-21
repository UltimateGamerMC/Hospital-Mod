package com.beckytidus.hospitalmod.block;

import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A patient bed. Right-click to lie down: the bed heals you, clears debuffs and becomes your
 * respawn point. While occupied it slowly mends anyone standing beside it.
 */
public class HospitalBedBlock extends FacingBlock {
	public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 9.0, 15.0);
	private static final int MEND_DELAY = 60;

	public HospitalBedBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OCCUPIED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OCCUPIED);
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
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if (!level.isClientSide()) {
			level.scheduleTick(pos, this, MEND_DELAY);
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (!state.getValue(OCCUPIED)) {
			level.setBlock(pos, state.setValue(OCCUPIED, true), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
			if (player instanceof ServerPlayer serverPlayer) {
				Medic.restAt(serverPlayer, pos);
			}
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.rested"));
			Medic.sound(level, pos, SoundEvents.ANVIL_LAND, 0.25F, 1.6F);
			Medic.particles((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, ParticleTypes.HEART, 5);
		} else {
			level.setBlock(pos, state.setValue(OCCUPIED, false), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.got_up"));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(OCCUPIED)) {
			AABB area = new AABB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
			for (Player patient : level.getEntitiesOfClass(Player.class, area)) {
				if (Medic.isHurt(patient)) {
					patient.heal(1.0F);
					Medic.particles(level, patient.getX(), patient.getY() + 1.0, patient.getZ(), ParticleTypes.HEART, 1);
				}
			}
			Medic.particles(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, ParticleTypes.HEART, 1);
		}
		level.scheduleTick(pos, this, MEND_DELAY);
	}
}
