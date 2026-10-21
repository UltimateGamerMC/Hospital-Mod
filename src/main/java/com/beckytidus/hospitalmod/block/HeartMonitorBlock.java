package com.beckytidus.hospitalmod.block;

import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
 * A bedside heart monitor. The screen lights up on its own whenever a patient is close by,
 * and right-clicking it prints a vitals readout of the nearest patient.
 */
public class HeartMonitorBlock extends FacingBlock {
	/** True while the monitor is picking up a patient. */
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	private static final int SCAN_DELAY = 20;
	private static final double DETECT_RANGE = 6.0;
	private static final double READ_RANGE = 10.0;
	private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

	public HeartMonitorBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
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
			level.scheduleTick(pos, this, SCAN_DELAY);
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		LivingEntity patient = nearestPatient(level, pos, READ_RANGE);
		if (patient == null) {
			Medic.chat(player, Component.translatable("msg.hospital-mod.monitor_none"));
		} else {
			Medic.chat(player, Medic.chart(patient));
		}
		Medic.sound(level, pos, SoundEvents.STONE_BUTTON_CLICK_ON, 0.5F, 1.5F);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		boolean detecting = nearestPatient(level, pos, DETECT_RANGE) != null;
		if (detecting != state.getValue(POWERED)) {
			level.setBlock(pos, state.setValue(POWERED, detecting), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
			if (detecting) {
				Medic.sound(level, pos, SoundEvents.NOTE_BLOCK_BELL.value(), 0.35F, 1.7F);
			}
		}
		level.scheduleTick(pos, this, SCAN_DELAY);
	}

	private static LivingEntity nearestPatient(Level level, BlockPos pos, double range) {
		AABB area = new AABB(pos).inflate(range);
		LivingEntity best = null;
		double bestDistance = Double.MAX_VALUE;
		for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive)) {
			double distance = entity.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			if (distance < bestDistance) {
				bestDistance = distance;
				best = entity;
			}
		}
		return best;
	}
}
