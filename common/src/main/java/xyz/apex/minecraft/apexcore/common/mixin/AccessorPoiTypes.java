package xyz.apex.minecraft.apexcore.common.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(PoiTypes.class)
public interface AccessorPoiTypes
{
    @Accessor("TYPE_BY_STATE")
    static Map<BlockState, Holder<PoiType>> ApexCore$getTypeByStateMap()
    {
        throw new AssertionError();
    }
}
