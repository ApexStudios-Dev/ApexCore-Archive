package xyz.apex.minecraft.apexcore.common.component.entity.types;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.entity.BaseBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.component.entity.BaseBlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentType;
import xyz.apex.minecraft.apexcore.common.util.NameableMutable;

public final class NameableBlockEntityComponent extends BaseBlockEntityComponent implements NameableMutable
{
    public static final BlockEntityComponentType<NameableBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(
            new ResourceLocation(ApexCore.ID, "nameable"),
            NameableBlockEntityComponent::new
    );

    @Nullable private Component customName = null;

    private NameableBlockEntityComponent(BlockEntityComponentHolder holder)
    {
        super(holder);
    }

    @Override
    public Component getName()
    {
        return BaseBlockEntityComponentHolder.getDefaultName(toBlockEntity());
    }

    @Override
    public Component getDisplayName()
    {
        return customName == null ? BaseBlockEntityComponentHolder.getDefaultName(toBlockEntity()) : customName;
    }

    @Override
    public boolean hasCustomName()
    {
        return customName != null;
    }

    @Nullable
    @Override
    public Component getCustomName()
    {
        return customName;
    }

    @Override
    public void setCustomName(@Nullable Component customName)
    {
        if(this.customName != customName) markDirty();
        this.customName = customName;
    }

    @Nullable
    @Override
    public Tag writeNbt()
    {
        return customName == null ? null : StringTag.valueOf(Component.Serializer.toJson(customName));
    }

    @Override
    public void readNbt(Tag nbt)
    {
        if(!(nbt instanceof StringTag stringTag)) return;
        customName = Component.Serializer.fromJson(stringTag.getAsString());
    }
}
