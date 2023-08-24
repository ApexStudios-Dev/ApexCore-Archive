package xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

public final class NameableBlockEntityComponent extends BaseBlockEntityComponent implements Nameable
{
    public static final BlockEntityComponentType<NameableBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(ApexCore.ID, "nameable", NameableBlockEntityComponent::new);

    private static final String NBT_CUSTOM_NAME = "CustomName";

    @Nullable private Component customName;

    private NameableBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public void setCustomName(@Nullable Component customName)
    {
        this.customName = customName;
    }

    @Override
    public Component getName()
    {
        return customName == null ? getGameObject().getBlockState().getBlock().getName() : customName;
    }

    @Override
    public boolean hasCustomName()
    {
        return customName != null;
    }

    @Override
    public Component getDisplayName()
    {
        return getName();
    }

    @Nullable
    @Override
    public Component getCustomName()
    {
        return customName;
    }

    @Override
    public void serializeInto(CompoundTag tag, boolean forNetwork)
    {
        if(customName != null)
            tag.putString(NBT_CUSTOM_NAME, Component.Serializer.toJson(customName));
    }

    @Override
    public void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
        customName = null;

        if(tag.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING))
            customName = Component.Serializer.fromJson(tag.getString(NBT_CUSTOM_NAME));
    }

    @Override
    public void setPlacedBy(Level level, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(stack.hasCustomHoverName())
            setCustomName(stack.getHoverName());
    }

    public static Component getDisplayName(BlockEntityComponentHolder componentHolder)
    {
        return componentHolder.findComponent(COMPONENT_TYPE).map(NameableBlockEntityComponent::getDisplayName).orElseGet(componentHolder::getDefaultName);
    }

    public static Component getDisplayName(BlockEntity blockEntity)
    {
        return blockEntity instanceof BlockEntityComponentHolder componentHolder ? getDisplayName(componentHolder) : blockEntity.getBlockState().getBlock().getName();
    }
}
