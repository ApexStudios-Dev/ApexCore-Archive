package xyz.apex.minecraft.apexcore.shared.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface AccessorBlockEntityType
{
    @Accessor Set<Block> getValidBlocks();
}
