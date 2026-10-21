package com.beckytidus.hospitalmod.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Base for every piece of hospital equipment that can face a direction.
 * Written against {@link Block} directly so it works on both 1.21.11 and 26.3.
 */
public abstract class FacingBlock extends Block {
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	protected FacingBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
}
