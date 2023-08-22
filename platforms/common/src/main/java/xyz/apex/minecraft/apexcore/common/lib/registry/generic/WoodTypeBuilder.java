package xyz.apex.minecraft.apexcore.common.lib.registry.generic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class WoodTypeBuilder
{
    private Supplier<BlockSetTypeBuilder> blockSetType = BlockSetTypeBuilder::builder;
    private SoundType soundType = SoundType.WOOD;
    private SoundType hangingSignSoundType = SoundType.HANGING_SIGN;
    private SoundEvent fenceGateClose = SoundEvents.FENCE_GATE_CLOSE;
    private SoundEvent fenceGateOpen = SoundEvents.FENCE_GATE_OPEN;

    public WoodTypeBuilder blockSetType(BlockSetTypeBuilder copyFrom, UnaryOperator<BlockSetTypeBuilder> builder)
    {
        this.blockSetType = () -> builder.apply(BlockSetTypeBuilder.builder().soundType(soundType).copyFrom(copyFrom));
        return this;
    }

    public WoodTypeBuilder blockSetType(BlockSetTypeBuilder copyFrom)
    {
        return blockSetType(copyFrom, UnaryOperator.identity());
    }

    public WoodTypeBuilder blockSetType(BlockSetType copyFrom, UnaryOperator<BlockSetTypeBuilder> builder)
    {
        this.blockSetType = () -> builder.apply(BlockSetTypeBuilder.builder().soundType(soundType).copyFrom(copyFrom));
        return this;
    }

    public WoodTypeBuilder blockSetType(BlockSetType copyFrom)
    {
        return blockSetType(copyFrom, UnaryOperator.identity());
    }

    public WoodTypeBuilder blockSetType(UnaryOperator<BlockSetTypeBuilder> builder)
    {
        blockSetType = () -> builder.apply(BlockSetTypeBuilder.builder().soundType(soundType));
        return this;
    }

    public WoodTypeBuilder soundType(SoundType soundType)
    {
        this.soundType = soundType;
        return this;
    }

    public WoodTypeBuilder hangingSignSoundType(SoundType hangingSignSoundType)
    {
        this.hangingSignSoundType = hangingSignSoundType;
        return this;
    }

    public WoodTypeBuilder fenceGateClose(SoundEvent fenceGateClose)
    {
        this.fenceGateClose = fenceGateClose;
        return this;
    }

    public WoodTypeBuilder fenceGateOpen(SoundEvent fenceGateOpen)
    {
        this.fenceGateOpen = fenceGateOpen;
        return this;
    }

    public WoodTypeBuilder copyFrom(WoodTypeBuilder copyFrom)
    {
        return blockSetType(blockSetTypeBuilder -> blockSetTypeBuilder.copyFrom(copyFrom.blockSetType.get()))
                .soundType(copyFrom.soundType)
                .hangingSignSoundType(copyFrom.hangingSignSoundType)
                .fenceGateClose(copyFrom.fenceGateClose)
                .fenceGateOpen(copyFrom.fenceGateOpen);
    }

    public WoodTypeBuilder copyFrom(WoodType copyFrom)
    {
        return blockSetType(blockSetTypeBuilder -> blockSetTypeBuilder.copyFrom(copyFrom.setType()))
                .soundType(copyFrom.soundType())
                .hangingSignSoundType(copyFrom.hangingSignSoundType())
                .fenceGateClose(copyFrom.fenceGateClose())
                .fenceGateOpen(copyFrom.fenceGateOpen());
    }

    public WoodType register(ResourceLocation registryName)
    {
        return WoodType.register(build(registryName));
    }

    public WoodType build(ResourceLocation registryName)
    {
        Validate.notNull(blockSetType);

        return new WoodType(
                registryName.toString(),
                blockSetType.get().register(registryName),
                soundType,
                hangingSignSoundType,
                fenceGateClose,
                fenceGateOpen
        );
    }

    public static WoodTypeBuilder builder()
    {
        return new WoodTypeBuilder();
    }

    public static WoodTypeBuilder copyOf(WoodTypeBuilder copyFrom)
    {
        return builder().copyFrom(copyFrom);
    }

    public static WoodTypeBuilder copyOf(WoodType copyFrom)
    {
        return builder().copyFrom(copyFrom);
    }
}
