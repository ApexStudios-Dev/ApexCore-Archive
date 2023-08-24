package xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

import java.util.Optional;
import java.util.function.Supplier;

public final class LockCodeBlockEntityComponent extends BaseBlockEntityComponent
{
    public static final BlockEntityComponentType<LockCodeBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(ApexCore.ID, "lock_code", LockCodeBlockEntityComponent::new);

    private LockCode lockCode = LockCode.NO_LOCK;

    private LockCodeBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public void setLockCode(LockCode lockCode)
    {
        this.lockCode = lockCode;
    }

    public void clearLockCode()
    {
        setLockCode(LockCode.NO_LOCK);
    }

    public boolean isLocked()
    {
        return lockCode != LockCode.NO_LOCK;
    }

    public boolean canUnlock(Player player)
    {
        var displayName = NameableBlockEntityComponent.getDisplayName(componentHolder);
        return BaseContainerBlockEntity.canUnlock(player, lockCode, displayName);
    }

    public void runIfUnlockable(Player player, Runnable runnable)
    {
        if(canUnlock(player))
            runnable.run();
    }

    public <T> Optional<T> getIfUnlockable(Player player, Supplier<T> supplier)
    {
        return Optional.ofNullable(getIfUnlockableNullable(player, supplier));
    }

    @Nullable
    public <T> T getIfUnlockableNullable(Player player, Supplier<T> supplier)
    {
        if(canUnlock(player))
            return supplier.get();

        return null;
    }

    @Override
    public void serializeInto(CompoundTag tag, boolean forNetwork)
    {
        lockCode.addToTag(tag);
    }

    @Override
    public void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
        lockCode = LockCode.fromTag(tag);
    }
}
