package dev.apexstudios.testmod.common.ref;

import dev.apexstudios.apexcore.common.network.Packet;
import dev.apexstudios.testmod.common.TestMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public interface TestPackets
{
    Packet.C2S<Boolean> TEST_PACKET_C2S = TestMod.NETWORK.registerC2S("test_c2s", (flag, buffer) -> buffer.writeBoolean(flag), FriendlyByteBuf::readBoolean, (sender, $) -> sender.displayClientMessage(Component.literal("Received reply from client"), true));

    // @formatter:off
    Packet.S2C<Integer> TEST_PACKET_S2C = TestMod.NETWORK.registerS2C("test_s2c", (counter, buffer) -> buffer.writeVarInt(counter), FriendlyByteBuf::readVarInt, counter -> {
        Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Client Counter: %s".formatted(counter)), false);
        TEST_PACKET_C2S.send(true);
    });
    // @formatter:on
}
