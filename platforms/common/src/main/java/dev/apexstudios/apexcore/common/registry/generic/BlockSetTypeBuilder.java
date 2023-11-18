package dev.apexstudios.apexcore.common.registry.generic;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class BlockSetTypeBuilder
{
    private boolean canOpenByHand = true;
    private SoundType soundType = SoundType.WOOD;
    private OptionalLike<SoundEvent> doorClose = () -> SoundEvents.WOODEN_DOOR_CLOSE;
    private OptionalLike<SoundEvent> doorOpen = () -> SoundEvents.WOODEN_DOOR_OPEN;
    private OptionalLike<SoundEvent> trapdoorClose = () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE;
    private OptionalLike<SoundEvent> trapdoorOpen = () -> SoundEvents.WOODEN_TRAPDOOR_OPEN;
    private OptionalLike<SoundEvent> pressurePlateClickOff = () -> SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF;
    private OptionalLike<SoundEvent> pressurePlateClickOn = () -> SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON;
    private OptionalLike<SoundEvent> buttonClickOff = () -> SoundEvents.WOODEN_BUTTON_CLICK_OFF;
    private OptionalLike<SoundEvent> buttonClickOn = () -> SoundEvents.WOODEN_BUTTON_CLICK_ON;

    public BlockSetTypeBuilder canOpenByHand(boolean canOpenByHand)
    {
        this.canOpenByHand = canOpenByHand;
        return this;
    }

    public BlockSetTypeBuilder soundType(SoundType soundType)
    {
        this.soundType = soundType;
        return this;
    }

    public BlockSetTypeBuilder doorClose(Supplier<@Nullable SoundEvent> doorClose)
    {
        this.doorClose = OptionalLike.of(doorClose);
        return this;
    }

    public BlockSetTypeBuilder doorOpen(Supplier<@Nullable SoundEvent> doorOpen)
    {
        this.doorOpen = OptionalLike.of(doorOpen);
        return this;
    }

    public BlockSetTypeBuilder trapdoorClose(Supplier<@Nullable SoundEvent> trapdoorClose)
    {
        this.trapdoorClose = OptionalLike.of(trapdoorClose);
        return this;
    }

    public BlockSetTypeBuilder trapdoorOpen(Supplier<@Nullable SoundEvent> trapdoorOpen)
    {
        this.trapdoorOpen = OptionalLike.of(trapdoorOpen);
        return this;
    }

    public BlockSetTypeBuilder pressurePlateClickOff(Supplier<@Nullable SoundEvent> pressurePlateClickOff)
    {
        this.pressurePlateClickOff = OptionalLike.of(pressurePlateClickOff);
        return this;
    }

    public BlockSetTypeBuilder pressurePlateClickOn(Supplier<@Nullable SoundEvent> pressurePlateClickOn)
    {
        this.pressurePlateClickOn = OptionalLike.of(pressurePlateClickOn);
        return this;
    }

    public BlockSetTypeBuilder buttonClickOff(Supplier<@Nullable SoundEvent> buttonClickOff)
    {
        this.buttonClickOff = OptionalLike.of(buttonClickOff);
        return this;
    }

    public BlockSetTypeBuilder buttonClickOn(Supplier<@Nullable SoundEvent> buttonClickOn)
    {
        this.buttonClickOn = OptionalLike.of(buttonClickOn);
        return this;
    }

    public BlockSetTypeBuilder copyFrom(BlockSetTypeBuilder copyFrom)
    {
        return canOpenByHand(copyFrom.canOpenByHand)
                .soundType(copyFrom.soundType)
                .doorClose(copyFrom.doorClose)
                .doorOpen(copyFrom.doorOpen)
                .trapdoorClose(copyFrom.trapdoorClose)
                .trapdoorOpen(copyFrom.trapdoorOpen)
                .pressurePlateClickOff(copyFrom.pressurePlateClickOff)
                .pressurePlateClickOn(copyFrom.pressurePlateClickOn)
                .buttonClickOff(copyFrom.buttonClickOff)
                .buttonClickOn(copyFrom.buttonClickOn);
    }

    public BlockSetTypeBuilder copyFrom(BlockSetType copyFrom)
    {
        return canOpenByHand(copyFrom.canOpenByHand())
                .soundType(copyFrom.soundType())
                .doorClose(copyFrom::doorClose)
                .doorOpen(copyFrom::doorOpen)
                .trapdoorClose(copyFrom::trapdoorClose)
                .trapdoorOpen(copyFrom::trapdoorOpen)
                .pressurePlateClickOff(copyFrom::pressurePlateClickOff)
                .pressurePlateClickOn(copyFrom::pressurePlateClickOn)
                .buttonClickOff(copyFrom::buttonClickOff)
                .buttonClickOn(copyFrom::buttonClickOn);
    }

    public BlockSetType register(ResourceLocation registryName)
    {
        // default to other similar sounds if unset
        var trapdoorClose = this.trapdoorClose.or(() -> doorClose);
        var trapdoorOpen = this.trapdoorOpen.or(() -> doorOpen);
        var pressurePlateClickOff = this.pressurePlateClickOff.or(() -> buttonClickOff);
        var pressurePlateClickOn = this.pressurePlateClickOn.or(() -> buttonClickOn);

        return BlockSetType.register(new BlockSetType(
                registryName.toString(),
                canOpenByHand,
                soundType,
                // default to wooden sounds if unset
                doorClose.orElseGet(() -> SoundEvents.WOODEN_DOOR_CLOSE),
                doorOpen.orElseGet(() -> SoundEvents.WOODEN_DOOR_OPEN),
                trapdoorClose.orElseGet(() -> SoundEvents.WOODEN_TRAPDOOR_CLOSE),
                trapdoorOpen.orElseGet(() -> SoundEvents.WOODEN_TRAPDOOR_OPEN),
                pressurePlateClickOff.orElseGet(() -> SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF),
                pressurePlateClickOn.orElseGet(() -> SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON),
                buttonClickOff.orElseGet(() -> SoundEvents.WOODEN_BUTTON_CLICK_OFF),
                buttonClickOn.orElseGet(() -> SoundEvents.WOODEN_BUTTON_CLICK_ON)
        ));
    }

    public static BlockSetTypeBuilder builder()
    {
        return new BlockSetTypeBuilder();
    }

    public static BlockSetTypeBuilder copyOf(BlockSetTypeBuilder copyFrom)
    {
        return builder().copyFrom(copyFrom);
    }

    public static BlockSetTypeBuilder copyOf(BlockSetType copyFrom)
    {
        return builder().copyFrom(copyFrom);
    }
}
