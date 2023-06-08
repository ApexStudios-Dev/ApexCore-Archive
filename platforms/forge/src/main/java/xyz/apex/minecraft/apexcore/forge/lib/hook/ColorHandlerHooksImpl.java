package xyz.apex.minecraft.apexcore.forge.lib.hook;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.forge.core.ModEvents;

import java.util.function.Supplier;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
final class ColorHandlerHooksImpl implements ColorHandlerHooks
{
    @Override
    public void registerBlockHandler(Supplier<? extends Block> block, Supplier<Supplier<BlockColor>> blockColor)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ModEvents.active().addListener(RegisterColorHandlersEvent.Block.class, event -> event.register(blockColor.get().get(), block.get()));
    }

    @Override
    public void registerItemHandler(Supplier<ItemLike> item, Supplier<Supplier<ItemColor>> itemColor)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ModEvents.active().addListener(RegisterColorHandlersEvent.Item.class, event -> event.register(itemColor.get().get(), item.get()));
    }
}
