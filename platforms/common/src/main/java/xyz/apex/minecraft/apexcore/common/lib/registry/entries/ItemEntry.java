package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.ItemBuilder;

/**
 * Main RegistryEntry class for all Item entries.
 * <p>
 * While the constructor is publicly visible, you should never invoke or create instance of this class yourself.
 * Instances of this class are provided when registered using the {@link ItemBuilder} class.
 *
 * @param <T> Type of item.
 */
public final class ItemEntry<T extends Item> extends DelegatedRegistryEntry<T> implements ItemLikeEntry<T>, FeatureElementEntry<T>
{
    /**
     * DO NOT MANUALLY CALL PUBLIC FOR INTERNAL USAGES ONLY
     */
    @ApiStatus.Internal
    public ItemEntry(RegistryEntry<T> delegate)
    {
        super(delegate);
    }
}
