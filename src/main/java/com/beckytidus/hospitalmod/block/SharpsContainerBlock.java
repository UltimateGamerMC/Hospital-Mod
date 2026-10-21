package com.beckytidus.hospitalmod.block;

import com.beckytidus.hospitalmod.registry.ItemRegistry;
import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A sharps disposal bin. Drop used syringes in the slot and the window fills up. Once it is
 * full the hazard tape comes out, a warning lamp lights on top, the bin glows, and it starts
 * venting soul fumes until you seal it with a biohazard bag.
 */
public class SharpsContainerBlock extends FacingBlock {
	/** 0 empty, 3 full. */
	public static final IntegerProperty FILL = IntegerProperty.create("fill", 0, 3);
	public static final int MAX_FILL = 3;
	private static final int FUME_DELAY = 25;
	private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

	/** The fumes that pour out of an overflowing bin. */
	private static final ParticleOptions[] FUMES = {
		ParticleTypes.SCULK_SOUL,
		ParticleTypes.SOUL_FIRE_FLAME,
		ParticleTypes.WITCH,
		ParticleTypes.SMOKE,
		ParticleTypes.ASH,
	};

	public SharpsContainerBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FILL, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, FILL);
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
			level.scheduleTick(pos, this, FUME_DELAY);
		}
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hitResult) {
		int fill = state.getValue(FILL);

		if (stack.getItem() == ItemRegistry.SYRINGE) {
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			if (fill >= MAX_FILL) {
				Medic.actionBar(player, Component.translatable("msg.hospital-mod.sharps_full"));
				return InteractionResult.SUCCESS;
			}
			int next = fill + 1;
			level.setBlock(pos, state.setValue(FILL, next), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.sharps_dropped", next, MAX_FILL));
			Medic.sound(level, pos, SoundEvents.SCULK_CATALYST_BLOOM, 0.6F, 0.8F);
			Medic.particles((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, ParticleTypes.CRIT, 6);
			if (next >= MAX_FILL) {
				// Overflowing: hazard tape, warning lamp and a proper shriek.
				Medic.sound(level, pos, SoundEvents.SCULK_SHRIEKER_SHRIEK, 1.0F, 1.0F);
				burst(level, pos);
				Medic.chat(player, Component.translatable("msg.hospital-mod.sharps_warning"));
			}
			if (!player.isCreative()) {
				stack.consume(1, player);
			}
			return InteractionResult.SUCCESS;
		}

		if (stack.getItem() == ItemRegistry.BIOHAZARD_BAG) {
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			if (fill <= 0) {
				Medic.actionBar(player, Component.translatable("msg.hospital-mod.sharps_empty"));
				return InteractionResult.SUCCESS;
			}
			level.setBlock(pos, state.setValue(FILL, 0), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.sharps_sealed"));
			Medic.sound(level, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), 0.7F, 1.2F);
			burst(level, pos);
			if (!player.isCreative()) {
				stack.consume(1, player);
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		int fill = state.getValue(FILL);
		if (fill <= 0) {
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.sharps_empty"));
		} else if (fill >= MAX_FILL) {
			Medic.chat(player, Component.translatable("msg.hospital-mod.sharps_warning"));
			Medic.sound(level, pos, SoundEvents.SCULK_CATALYST_BLOOM, 0.5F, 0.6F);
		} else {
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.sharps_level", fill, MAX_FILL));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(FILL) >= MAX_FILL) {
			double x = pos.getX() + 0.5;
			double y = pos.getY() + 1.1;
			double z = pos.getZ() + 0.5;

			// Soul fumes leaking out of an overflowing bin.
			ParticleOptions fume = FUMES[random.nextInt(FUMES.length)];
			Medic.particles(level, x, y, z, fume, 3 + random.nextInt(3));
			if (random.nextInt(3) == 0) {
				Medic.particles(level, x, y, z, ParticleTypes.ELDER_GUARDIAN, 1);
			}
			if (random.nextInt(4) == 0) {
				Medic.sound(level, pos, SoundEvents.SOUL_ESCAPE.value(), 0.5F, 0.6F);
			} else if (random.nextInt(6) == 0) {
				Medic.sound(level, pos, SoundEvents.SCULK_SHRIEKER_SHRIEK, 0.35F, 1.4F);
			}
		}
		level.scheduleTick(pos, this, FUME_DELAY);
	}

	private static void burst(Level level, BlockPos pos) {
		if (level instanceof ServerLevel server) {
			double x = pos.getX() + 0.5;
			double y = pos.getY() + 1.2;
			double z = pos.getZ() + 0.5;
			for (ParticleOptions fume : FUMES) {
				Medic.particles(server, x, y, z, fume, 8);
			}
			Medic.particles(server, x, y, z, ParticleTypes.SONIC_BOOM, 1);
		}
	}
}
