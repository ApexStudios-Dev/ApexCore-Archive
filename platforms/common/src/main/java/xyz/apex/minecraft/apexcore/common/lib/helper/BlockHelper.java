package xyz.apex.minecraft.apexcore.common.lib.helper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.mixin.AccessorBlock;
import xyz.apex.minecraft.apexcore.common.mixin.InvokerBlock;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@ApiStatus.NonExtendable
public interface BlockHelper
{
    /**
     * Fully rebuilds the StateDefinition for the given block.
     * <p>
     * This should rarely ever be done and other methods should be attempted before replacing the state definition.
     *
     * <ol>
     *     <li>Rebuild StateDefinition - Invoking {@link Block#createBlockStateDefinition(StateDefinition.Builder)}</li>
     *     <lI>Re-Register the Default BlockState - Invoking {@link Block#registerDefaultState(BlockState)}</lI>
     *     <li>Replace the backing StateDefinition with the newly built version - Replacing vanilla's {@link Block#stateDefinition}</li>
     * </ol>
     * <p>
     * We are using mixins to replace the stateDefintion field and invoke the protected methods rather than using AW/AT to change visibility,
     * due to Forge wanting us to add AT lines for every block that overrides createBlockStateDefinition, which cant be done due
     * modded blocks potentially overriding this method.
     * <p>
     * Block Component registration happens during the Block constructor, but BlockState initialization happens in the super's constructor,
     * meaning that any BlockStates our components register will not be initialized correctly.
     * <p>
     * We rebuild and replace vanillas state definition while also registering any block state properties for our components.
     *
     * @param block                     Block to rebuild StateDefinition for.
     * @param additionalProperties      Consumer used to register additional properties.
     * @param registerDefaultBlockState Function used to register default block state for newly built state definition.
     */
    static void rebuildStateDefinition(Block block, Consumer<StateDefinition.Builder<Block, BlockState>> additionalProperties, UnaryOperator<BlockState> registerDefaultBlockState)
    {
        var builder = new StateDefinition.Builder<Block, BlockState>(block);
        ((InvokerBlock) block).ApexCore$createBlockStateDefinition(builder);
        additionalProperties.accept(builder);
        var stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        ((AccessorBlock) block).ApexCore$setStateDefinition(stateDefinition);
        ((InvokerBlock) block).ApexCore$registerDefaultState(registerDefaultBlockState.apply(stateDefinition.any()));
    }
}
