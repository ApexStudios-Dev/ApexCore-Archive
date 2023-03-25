package xyz.apex.minecraft.apexcore.common.component.entity.types;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.entity.*;

public final class LockableContainerBlockEntityComponent extends BaseBlockEntityComponent
{
    public static final BlockEntityComponentType<LockableContainerBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(
            new ResourceLocation(ApexCore.ID, "locking"),
            LockableContainerBlockEntityComponent::new,
            BlockEntityComponentTypes.CONTAINER
    );

    @Nullable private String lockCode = null;

    private LockableContainerBlockEntityComponent(BlockEntityComponentHolder holder)
    {
        super(holder);
    }

    public LockableContainerBlockEntityComponent withLockCode(@Nullable String lockCode)
    {
        this.lockCode = lockCode;
        return this;
    }

    @Nullable
    public String getLockCode()
    {
        return lockCode;
    }

    public boolean isLocked()
    {
        return lockCode != null && !lockCode.isBlank();
    }

    public boolean holdingMatchingItem(Player player, InteractionHand hand)
    {
        if(lockCode == null || lockCode.isBlank()) return true;
        if(player.isSpectator()) return true;
        return isMatchingItem(player.getItemInHand(hand));
    }

    public boolean isMatchingItem(ItemStack stack)
    {
        if(lockCode == null || lockCode.isBlank()) return true;
        if(stack.isEmpty()) return false;
        if(!stack.hasCustomHoverName()) return false;
        return lockCode.equals(stack.getHoverName().toString());
    }

    public void tryUnlock(Player player, InteractionHand hand)
    {
        if(isLocked())
        {
            var stack = player.getItemInHand(hand);

            if(isMatchingItem(stack))
            {
                lockCode = null;
                return;
            }

            var displayName = getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getDisplayName).orElseGet(() -> BaseBlockEntityComponentHolder.getDefaultName(toBlockEntity()));
            player.displayClientMessage(Component.translatable("container.isLocked", displayName), true);
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1F, 1F);
        }
    }

    @Override
    public Tag writeNbt()
    {
        if(lockCode == null) return null;
        return StringTag.valueOf(lockCode);
    }

    @Override
    public void readNbt(Tag nbt)
    {
        if(nbt instanceof StringTag tag) lockCode = tag.getAsString();
    }
}
