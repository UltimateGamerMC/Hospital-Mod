package com.beckytidus.hospitalmod.util;

import com.beckytidus.hospitalmod.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Builds a small hospital ward in front of the player: floor, walls, ceiling, lights,
 * two patient beds with IV drips, a heart monitor and a supply cabinet.
 */
public final class WardBuilder {
	private WardBuilder() {
	}

	/** Half-width of the ward along the player's left/right axis. */
	private static final int HALF_WIDTH = 3;
	private static final int LENGTH = 7;
	private static final int WALL_HEIGHT = 3;

	public static void build(ServerLevel level, BlockPos origin, Direction facing) {
		BlockPos floor = origin.above();

		// Floor and ceiling.
		for (int x = -HALF_WIDTH; x <= HALF_WIDTH; x++) {
			for (int z = 0; z <= LENGTH; z++) {
				set(level, world(floor, facing, x, -1, z), BlockRegistry.HOSPITAL_FLOOR.defaultBlockState());
				set(level, world(floor, facing, x, WALL_HEIGHT, z), BlockRegistry.HOSPITAL_FLOOR.defaultBlockState());
			}
		}

		// Interior air, plus the walls around it.
		for (int x = -HALF_WIDTH; x <= HALF_WIDTH; x++) {
			for (int z = 0; z <= LENGTH; z++) {
				boolean edge = x == -HALF_WIDTH || x == HALF_WIDTH || z == LENGTH || z == 0;
				boolean doorway = z == 0 && (x == 0 || x == -1);
				for (int y = 0; y < WALL_HEIGHT; y++) {
					BlockPos pos = world(floor, facing, x, y, z);
					if (edge && !doorway) {
						set(level, pos, BlockRegistry.HOSPITAL_WALL.defaultBlockState());
					} else {
						set(level, pos, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}

		// Ceiling lights.
		for (int x : new int[]{-2, 2}) {
			for (int z : new int[]{2, 5}) {
				set(level, world(floor, facing, x, WALL_HEIGHT, z), Blocks.SEA_LANTERN.defaultBlockState());
			}
		}

		// Two beds against the left wall, each with a drip beside it.
		Direction bedFacing = rotate(Direction.EAST, facing);
		for (int z : new int[]{2, 5}) {
			set(level, world(floor, facing, -HALF_WIDTH + 1, 0, z), BlockRegistry.HOSPITAL_BED.defaultBlockState().setValue(
				com.beckytidus.hospitalmod.block.FacingBlock.FACING, bedFacing));
			set(level, world(floor, facing, -HALF_WIDTH + 2, 0, z), BlockRegistry.IV_STAND.defaultBlockState().setValue(
				com.beckytidus.hospitalmod.block.IvStandBlock.FACING, bedFacing).setValue(
				com.beckytidus.hospitalmod.block.IvStandBlock.CHARGE, com.beckytidus.hospitalmod.block.IvStandBlock.MAX_CHARGE));
		}

		// Heart monitor by the entrance, supply cabinet set into the right wall.
		set(level, world(floor, facing, HALF_WIDTH - 1, 0, 1), BlockRegistry.HEART_MONITOR.defaultBlockState().setValue(
			com.beckytidus.hospitalmod.block.HeartMonitorBlock.FACING, rotate(Direction.WEST, facing)));
		set(level, world(floor, facing, HALF_WIDTH, 0, 4), BlockRegistry.MEDICAL_CABINET.defaultBlockState().setValue(
			com.beckytidus.hospitalmod.block.MedicalCabinetBlock.FACING, rotate(Direction.WEST, facing)));
		set(level, world(floor, facing, HALF_WIDTH - 1, 0, 6), BlockRegistry.SHARPS_CONTAINER.defaultBlockState().setValue(
			com.beckytidus.hospitalmod.block.FacingBlock.FACING, rotate(Direction.WEST, facing)));
		// Biohazard signs, mounted high on the far wall above the beds. Placed one block
		// short of the wall so the wall itself stays intact behind them.
		for (int x : new int[]{-1, 1}) {
			set(level, world(floor, facing, x, 2, LENGTH - 1), BlockRegistry.BIOHAZARD_SIGN.defaultBlockState().setValue(
				com.beckytidus.hospitalmod.block.FacingBlock.FACING, facing.getOpposite()));
		}
	}

	private static void set(ServerLevel level, BlockPos pos, BlockState state) {
		level.setBlock(pos, state, Block.UPDATE_ALL, Block.UPDATE_LIMIT);
	}

	/** Local coordinates: x is right, z is forward, y is up. */
	private static BlockPos world(BlockPos origin, Direction facing, int x, int y, int z) {
		Vec3i forward = facing.getUnitVec3i();
		Vec3i right = facing.getClockWise().getUnitVec3i();
		return origin.offset(
			right.getX() * x + forward.getX() * z,
			y,
			right.getZ() * x + forward.getZ() * z);
	}

	/** Rotates a local direction into world space. */
	private static Direction rotate(Direction local, Direction facing) {
		Direction result = local;
		for (int i = 0; i < turns(facing); i++) {
			result = result.getClockWise();
		}
		return result;
	}

	private static int turns(Direction facing) {
		return switch (facing) {
			case NORTH -> 0;
			case EAST -> 1;
			case SOUTH -> 2;
			case WEST -> 3;
			default -> 0;
		};
	}
}
