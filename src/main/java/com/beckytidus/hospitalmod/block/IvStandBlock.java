package com.beckytidus.hospitalmod.block;

import com.beckytidus.hospitalmod.registry.ItemRegistry;
import com.beckytidus.hospitalmod.util.Medic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * An IV drip on a wheeled stand. Refill it with a saline bag and it slowly heals everyone
 * standing nearby, one dose at a time, until the bag runs dry.
 */
public class IvStandBlock extends FacingBlock {
	/** Doses left in the bag. 0 is empty, 4 is a fresh bag. */
	public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 4);
	public static final int MAX_CHARGE = 4;
	private static final int DRIP_DELAY = 40;
	private static final double HEAL_RANGE = 4.0;
	private static final VoxelShape SHAPE = Block.box(4.5, 0.0, 4.5, 11.5, 16.0, 11.5);

	public IvStandBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CHARGE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, CHARGE);
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
			level.scheduleTick(pos, this, DRIP_DELAY);
		}
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() != ItemRegistry.SALINE_BAG) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		if (state.getValue(CHARGE) >= MAX_CHARGE) {
			Medic.actionBar(player, Component.translatable("msg.hospital-mod.iv_full"));
			return InteractionResult.SUCCESS;
		}
		level.setBlock(pos, state.setValue(CHARGE, MAX_CHARGE), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
		Medic.actionBar(player, Component.translatable("msg.hospital-mod.iv_refilled"));
		Medic.sound(level, pos, SoundEvents.ITEM_PICKUP, 0.6F, 0.9F);
		Medic.particles((ServerLevel) level, pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, ParticleTypes.SPLASH, 6);
		if (!player.isCreative()) {
			stack.consume(1, player);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			int charge = state.getValue(CHARGE);
			Medic.actionBar(player, charge <= 0
				? Component.translatable("msg.hospital-mod.iv_empty")
				: Component.translatable("msg.hospital-mod.iv_level", charge, MAX_CHARGE));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int charge = state.getValue(CHARGE);
		if (charge > 0) {
			AABB area = new AABB(pos).inflate(HEAL_RANGE);
			boolean used = false;
			for (Player patient : level.getEntitiesOfClass(Player.class, area)) {
				if (Medic.isHurt(patient)) {
					patient.heal(2.0F);
					patient.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
					Medic.particles(level, patient.getX(), patient.getY() + 1.0, patient.getZ(), ParticleTypes.HEART, 2);
					used = true;
				}
			}
			// A steady drip from the bag while there is fluid left.
			Medic.particles(level, pos.getX() + 0.35, pos.getY() + 1.1, pos.getZ() + 0.5, ParticleTypes.DRIPPING_WATER, 1);
			if (used) {
				level.setBlock(pos, state.setValue(CHARGE, charge - 1), Block.UPDATE_ALL, Block.UPDATE_LIMIT);
			}
		}
		level.scheduleTick(pos, this, DRIP_DELAY);
	}
}
