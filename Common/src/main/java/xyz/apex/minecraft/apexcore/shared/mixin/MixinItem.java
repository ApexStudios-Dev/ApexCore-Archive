package xyz.apex.minecraft.apexcore.shared.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import xyz.apex.minecraft.apexcore.shared.hooks.acessors.ItemHook;

@Mixin(Item.class)
public abstract class MixinItem implements ItemHook
{
    @Shadow @Final private Rarity rarity;

    @Override
    public Rarity ApexCore$getRarity()
    {
        return rarity;
    }
}
