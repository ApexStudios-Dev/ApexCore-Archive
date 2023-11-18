package dev.apexstudios.apexcore.common.registry.generic;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class WoodTypeBuilder
{
    private Supplier<BlockSetTypeBuilder> blockSetType = BlockSetTypeBuilder::builder;
    private SoundType soundType = SoundType.WOOD;
    private SoundType hangingSignSoundType = SoundType.HANGING_SIGN;
    private OptionalLike<SoundEvent> fenceGateClose = () -> SoundEvents.FENCE_GATE_CLOSE;
    private OptionalLike<SoundEvent> fenceGateOpen = () -> SoundEvents.FENCE_GATE_OPEN;

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

    public WoodTypeBuilder fenceGateClose(Supplier<@Nullable SoundEvent> fenceGateClose)
    {
        this.fenceGateClose = OptionalLike.of(fenceGateClose);
        return this;
    }

    public WoodTypeBuilder fenceGateOpen(Supplier<@Nullable SoundEvent> fenceGateOpen)
    {
        this.fenceGateOpen = OptionalLike.of(fenceGateOpen);
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
                .fenceGateClose(copyFrom::fenceGateClose)
                .fenceGateOpen(copyFrom::fenceGateOpen);
    }

    public WoodType register(ResourceLocation registryName)
    {
        return WoodType.register(new WoodType(
                registryName.toString(),
                blockSetType.get().register(registryName),
                soundType,
                hangingSignSoundType,
                fenceGateClose.orElseGet(() -> SoundEvents.FENCE_GATE_CLOSE),
                fenceGateOpen.orElseGet(() -> SoundEvents.FENCE_GATE_OPEN)
        ));
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
