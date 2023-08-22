package xyz.apex.minecraft.apexcore.common.lib.registry.generic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class BlockSetTypeBuilder
{
    private boolean canOpenByHand = true;
    private SoundType soundType = SoundType.WOOD;
    private SoundEvent doorClose = SoundEvents.WOODEN_DOOR_CLOSE;
    private SoundEvent doorOpen = SoundEvents.WOODEN_DOOR_OPEN;
    private SoundEvent trapdoorClose = SoundEvents.WOODEN_TRAPDOOR_CLOSE;
    private SoundEvent trapdoorOpen = SoundEvents.WOODEN_TRAPDOOR_OPEN;
    private SoundEvent pressurePlateClickOff = SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF;
    private SoundEvent pressurePlateClickOn = SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON;
    private SoundEvent buttonClickOff = SoundEvents.WOODEN_BUTTON_CLICK_OFF;
    private SoundEvent buttonClickOn = SoundEvents.WOODEN_BUTTON_CLICK_ON;

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

    public BlockSetTypeBuilder doorClose(SoundEvent doorClose)
    {
        this.doorClose = doorClose;
        return this;
    }

    public BlockSetTypeBuilder doorOpen(SoundEvent doorOpen)
    {
        this.doorOpen = doorOpen;
        return this;
    }

    public BlockSetTypeBuilder trapdoorClose(SoundEvent trapdoorClose)
    {
        this.trapdoorClose = trapdoorClose;
        return this;
    }

    public BlockSetTypeBuilder trapdoorOpen(SoundEvent trapdoorOpen)
    {
        this.trapdoorOpen = trapdoorOpen;
        return this;
    }

    public BlockSetTypeBuilder pressurePlateClickOff(SoundEvent pressurePlateClickOff)
    {
        this.pressurePlateClickOff = pressurePlateClickOff;
        return this;
    }

    public BlockSetTypeBuilder pressurePlateClickOn(SoundEvent pressurePlateClickOn)
    {
        this.pressurePlateClickOn = pressurePlateClickOn;
        return this;
    }

    public BlockSetTypeBuilder buttonClickOff(SoundEvent buttonClickOff)
    {
        this.buttonClickOff = buttonClickOff;
        return this;
    }

    public BlockSetTypeBuilder buttonClickOn(SoundEvent buttonClickOn)
    {
        this.buttonClickOn = buttonClickOn;
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
                .doorClose(copyFrom.doorClose())
                .doorOpen(copyFrom.doorOpen())
                .trapdoorClose(copyFrom.trapdoorClose())
                .trapdoorOpen(copyFrom.trapdoorOpen())
                .pressurePlateClickOff(copyFrom.pressurePlateClickOff())
                .pressurePlateClickOn(copyFrom.pressurePlateClickOn())
                .buttonClickOff(copyFrom.buttonClickOff())
                .buttonClickOn(copyFrom.buttonClickOn());
    }

    public BlockSetType register(ResourceLocation registryName)
    {
        return BlockSetType.register(build(registryName));
    }

    public BlockSetType build(ResourceLocation registryName)
    {
        return new BlockSetType(
                registryName.toString(),
                canOpenByHand,
                soundType,
                doorClose,
                doorOpen,
                trapdoorClose,
                trapdoorOpen,
                pressurePlateClickOff,
                pressurePlateClickOn,
                buttonClickOff,
                buttonClickOn
        );
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
