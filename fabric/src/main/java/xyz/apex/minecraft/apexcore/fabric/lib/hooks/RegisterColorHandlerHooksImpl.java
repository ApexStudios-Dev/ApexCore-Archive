package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterColorHandlerHooks;

import java.util.function.Supplier;

@ApiStatus.Internal
final class RegisterColorHandlerHooksImpl implements RegisterColorHandlerHooks
{
    @Override
    public void registerBlockColor(Supplier<? extends Block> block, Supplier<Supplier<BlockColor>> blockColor)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ColorProviderRegistry.BLOCK.register(blockColor.get().get(), block.get());
    }

    @Override
    public void registerItemColor(Supplier<ItemLike> item, Supplier<Supplier<ItemColor>> itemColor)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ColorProviderRegistry.ITEM.register(itemColor.get().get(), item.get());
    }
}