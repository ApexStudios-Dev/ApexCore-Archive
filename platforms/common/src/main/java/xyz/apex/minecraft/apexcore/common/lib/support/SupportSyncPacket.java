package xyz.apex.minecraft.apexcore.common.lib.support;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

final class SupportSyncPacket
{
    final Map<UUID, EnumSet<SupportLevel>> levels;

    SupportSyncPacket(Map<UUID, EnumSet<SupportLevel>> levels)
    {
        this.levels = Collections.unmodifiableMap(levels);
    }

    SupportSyncPacket(FriendlyByteBuf buffer)
    {
        levels = buffer.readMap(FriendlyByteBuf::readUUID, valueBuffer -> valueBuffer.readEnumSet(SupportLevel.class));
    }

    static void encode(SupportSyncPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeMap(packet.levels, FriendlyByteBuf::writeUUID, (valueBuffer, supportLevels) -> valueBuffer.writeEnumSet(supportLevels, SupportLevel.class));
    }

    static void consume(Minecraft client, NetworkManager networkManager, SupportSyncPacket packet)
    {
        SupportManager.INSTANCE.fromNetwork(packet);
    }
}
