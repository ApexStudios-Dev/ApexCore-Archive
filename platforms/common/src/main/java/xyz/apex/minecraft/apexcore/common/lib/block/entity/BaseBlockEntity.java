package xyz.apex.minecraft.apexcore.common.lib.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseBlockEntity extends BlockEntity
{
    private static final String NBT_NETWORK_MARKER = "__APEX_INTERNAL_NETWORK_MARKER__";

    public BaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);
    }

    protected void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
    }

    protected void serializeInto(CompoundTag tag, boolean forNetwork)
    {
    }

    @Override
    public final void load(CompoundTag tag)
    {
        // loaded from network?
        var fromNetwork = tag.contains(NBT_NETWORK_MARKER, Tag.TAG_BYTE) && tag.getBoolean(NBT_NETWORK_MARKER);

        // should not be visible to inheritors
        if(tag.contains(NBT_NETWORK_MARKER))
            tag.remove(NBT_NETWORK_MARKER);

        // does nothing in vanilla or fabric
        // but forge saves custom data in super
        super.load(tag);

        // deserialize after super has handled everything
        // to stop inheritors from potentially modifying data
        deserializeFrom(tag, fromNetwork);
    }

    @Override
    protected final void saveAdditional(CompoundTag tag)
    {
        // serialize before super writes any data
        // to stop inheritors from potentially modifying data
        serializeInto(tag, false);

        // does nothing in vanilla or fabric
        // but forge saves custom data in super
        super.saveAdditional(tag);
    }

    @Override
    public final Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public final CompoundTag getUpdateTag()
    {
        var tag = new CompoundTag();
        serializeInto(tag, true);
        // add network marker
        tag.putBoolean(NBT_NETWORK_MARKER, true);
        return tag;
    }

    @Override // finalize, we dont want to accidentally override this
    public final BlockEntityType<?> getType()
    {
        return super.getType();
    }
}
