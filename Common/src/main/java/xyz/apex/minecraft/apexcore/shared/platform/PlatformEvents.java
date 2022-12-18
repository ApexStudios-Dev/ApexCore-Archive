package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface PlatformEvents extends PlatformHolder
{
    void onClientSetup(Runnable runnable);
    void onDedicatedServerSetup(Runnable runnable);

    void registerRenderTypes(Supplier<? extends Block> block, Supplier<Supplier<RenderType[]>> renderTypes);

    void registerItemColor(Supplier<? extends Item> item, Supplier<Supplier<ItemColor>> colorHandler);

    void registerBlockColor(Supplier<? extends Block> block, Supplier<Supplier<BlockColor>> colorHandler);

    void register(String modId);

    @ApiStatus.Internal void registerDataGenerators(String modId);
}
