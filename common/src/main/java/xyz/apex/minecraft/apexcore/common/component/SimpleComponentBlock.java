package xyz.apex.minecraft.apexcore.common.component;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.components.BlockEntityComponent;

import java.util.*;
import java.util.function.Consumer;

public class SimpleComponentBlock extends Block implements ComponentBlock, EntityBlock
{
    private final Map<ResourceLocation, Component> componentMap = Maps.newLinkedHashMap();
    private final Set<ResourceLocation> componentTypes = Collections.unmodifiableSet(componentMap.keySet());
    private final Collection<Component> components = Collections.unmodifiableCollection(componentMap.values());
    private boolean registered = false;

    public SimpleComponentBlock(Properties properties)
    {
        this(block -> {}, properties);
    }

    public SimpleComponentBlock(Consumer<SimpleComponentBlock> registerComponents, Properties properties)
    {
        super(properties);

        doConstructor(registerComponents);
    }

    // region: Component
    private void doConstructor(Consumer<SimpleComponentBlock> registerComponents)
    {
        registerComponents();
        registerComponents.accept(this);
        registered = true;
        validateComponents();
        registerDefaultBlockStateForComponents();
    }

    private void validateComponents()
    {
        /*for(var componentType : componentTypes)
        {
            for(var required : componentType.getRequiredTypes())
            {
                if(!hasComponent(required)) throw new IllegalStateException("Block: '%s' is missing required ComponentType: '%s' due to using ComponentType: '%s'".formatted(getClass().getName(), required.getRegistryName(), componentType.getRegistryName()));
            }
        }*/

        components.forEach(Component::validate);
    }

    private int compareComponentTypes(ComponentType<?> a, ComponentType<?> b)
    {
        // same component type, dont move
        if(a.getRegistryName().equals(b.getRegistryName())) return 0;

        // component type is required, move to front
        for(var required : a.getRequiredTypes())
        {
            if(required.getRegistryName().equals(b.getRegistryName())) return -1;
        }

        for(var required : b.getRequiredTypes())
        {
            if(required.getRegistryName().equals(a.getRegistryName())) return -1;
        }

        // not required by anything, move to end
        return 1;
    }

    private void registerDefaultBlockStateForComponents()
    {
        ApexCore.replaceBlockStateContainer(this);

        var defaultBlockState = defaultBlockState();

        for(var component : components)
        {
            defaultBlockState = component.registerDefaultBlockState(defaultBlockState);
        }

        registerDefaultState(defaultBlockState);
    }

    @Override
    public void registerComponents()
    {
    }

    @Override
    public final <T extends Component> T registerComponent(ComponentType<T> componentType, Object... constructorArgs)
    {
        if(registered) throw new IllegalStateException("Attempt to register component '%s' post registration".formatted(componentType.getRegistryName()));
        if(hasComponent(componentType)) throw new IllegalStateException("Attempt to register duplicate component: '%s'".formatted(componentType.getRegistryName()));

        var result = componentType.newInstance(this, constructorArgs);
        componentMap.put(componentType.getRegistryName(), result);
        return result;
    }

    @Override
    public final <T extends Component> Optional<T> getOptionalComponent(ComponentType<T> componentType)
    {
        return Optional.ofNullable(componentType.cast(componentMap.get(componentType.getRegistryName())));
    }

    @Override
    public final Set<ResourceLocation> getComponentTypes()
    {
        return componentTypes;
    }

    @Override
    public final <T extends Component> T getRequiredComponent(ComponentType<T> componentType)
    {
        return getOptionalComponent(componentType).orElseThrow(() -> new IllegalStateException("Block: '%s' is missing required ComponentType: '%s'".formatted(getClass().getName(), componentType.getRegistryName())));
    }

    @Override
    public final Collection<Component> getComponents()
    {
        return components;
    }

    @Override
    public final <T extends Component> boolean hasComponent(ComponentType<T> componentType)
    {
        // for some reason doing just `componentMap.containsKey(componentType)`
        // will not catch correct result every time
        // so we are going verbose and checking every possible way to
        // determine if a component has been registered

        if(componentMap.containsKey(componentType.getRegistryName())) return true;
        if(componentTypes.contains(componentType.getRegistryName())) return true;

        return componentTypes
                .stream()
                .anyMatch(registered -> registered.equals(componentType.getRegistryName()));
    }

    @Override
    public final Block toBlock()
    {
        return ComponentBlock.super.toBlock();
    }
    // endregion

    // region: Overrides
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        for(var component : components)
        {
            var result = component.use(blockState, level, pos, player, hand, hit);
            if(result == InteractionResult.FAIL || result.consumesAction()) return result;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        var blockState = defaultBlockState();

        for(var component : components)
        {
            blockState = component.getStateForPlacement(ctx, blockState);
            if(blockState == null) return null;
        }

        return blockState;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        for(var component : components)
        {
            if(!component.canSurvive(blockState, level, pos)) return false;
        }

        return true;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        components.forEach(component -> component.onPlace(blockState, level, pos, oldBlockState, isMoving));
        super.onPlace(blockState, level, pos, oldBlockState, isMoving);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        components.forEach(component -> component.onRemove(blockState, level, pos, newBlockState, isMoving));
        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        // this method gets run twice
        // once by super constructor, to build vanilla state definition
        // and once by us to build our replacement state definition
        // if called by super our fields have not yet initialized causing NPE
        // this if check delays until everything has been initialized
        if(registered) components.forEach(component -> component.createBlockStateDefinition(builder::add));
        super.createBlockStateDefinition(builder);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        for(var component : components)
        {
            var renderShape = component.getRenderShape(blockState);
            if(renderShape != null) return renderShape;
        }

        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        for(var component : components)
        {
            var blockEntity = component.newBlockEntity(pos, blockState);
            if(blockEntity != null) return blockEntity;
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        components.forEach(component -> component.setPlacedBy(level, pos, blockState, placer, stack));
        super.setPlacedBy(level, pos, blockState, placer, stack);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState)
    {
        for(var component : components)
        {
            if(component.hasAnalogOutputSignal(blockState)) return true;
        }

        return false;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        var result = 0;

        for(var component : components)
        {
            result = Math.max(result, component.getAnalogOutputSignal(blockState, level, pos));
        }

        return result;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        for(var component : components)
        {
            var menuProvider = component.getMenuProvider(blockState, level, pos);
            if(menuProvider != null) return menuProvider;
        }

        return null;
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos pos, int id, int param)
    {
        return BlockEntityComponent.lookupBlockEntity(level, pos).map(blockEntity -> blockEntity.triggerEvent(id, param)).orElse(false);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation)
    {
        var modified = blockState;

        for(var component : components)
        {
            if(!modified.is(this)) return modified;
            modified = component.rotate(modified, rotation);
        }

        return modified;
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror)
    {
        var modified = blockState;

        for(var component : components)
        {
            if(!modified.is(this)) return modified;
            modified = component.mirror(modified, mirror);
        }

        return modified;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        var modified = blockState;

        for(var component : components)
        {
            if(!modified.is(this)) return modified;
            modified = component.updateShape(modified, direction, neighborState, level, currentPos, neighborPos);
        }

        return modified;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        components.forEach(component -> component.neighborChanged(blockState, level, pos, block, fromPos, isMoving));
        super.neighborChanged(blockState, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        components.forEach(component -> component.playerWillDestroy(level, pos, blockState, player));
        super.playerWillDestroy(level, pos, blockState, player);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos pos, PathComputationType type)
    {
        for(var component : components)
        {
            if(!component.isPathFindable(blockState, level, pos, type)) return false;
        }

        return true;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState)
    {
        for(var component : components)
        {
            var reaction = component.getPistonPushReaction(blockState);
            if(reaction != null) return reaction;
        }

        return super.getPistonPushReaction(blockState);
    }

    @Override
    public long getSeed(BlockState blockState, BlockPos pos)
    {
        var seed = -1L;

        for(var component : components)
        {
            var componentSeed = component.getSeed(blockState, pos);
            if(componentSeed > 0L) seed = Math.max(seed, componentSeed);
        }

        return seed;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType)
    {
        for(var component : components)
        {
            var ticker = component.getTicker(level, blockState, blockEntityType);
            if(ticker != null) return ticker;
        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity)
    {
        for(var component : components)
        {
            var listener = component.getListener(level, blockEntity);
            if(listener != null) return listener;
        }

        return null;
    }
    // endregion
}
