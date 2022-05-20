package xyz.apex.forge.apexcore.core.net;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import xyz.apex.forge.apexcore.lib.net.AbstractPacket;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.java.utility.Apex;

import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

public final class ClientSyncSupportersPacket extends AbstractPacket
{
	public final Set<SupporterManager.SupporterInfo> networkInfos;

	public ClientSyncSupportersPacket(Set<SupporterManager.SupporterInfo> networkInfos)
	{
		this.networkInfos = ImmutableSet.copyOf(networkInfos);
	}

	public ClientSyncSupportersPacket(PacketBuffer buffer)
	{
		super(buffer);

		int count = buffer.readVarInt();

		networkInfos = Apex.makeImmutableSet(builder -> {
			for(int i = 0; i < count; i++)
			{
				SupporterManager.SupporterLevel level = SupporterManager.SupporterLevel.DEFAULT;
				Set<UUID> aliases = Sets.newHashSet();
				UUID playerId = buffer.readUUID();
				String username = buffer.readUtf();

				if(buffer.readBoolean())
					level = buffer.readEnum(SupporterManager.SupporterLevel.class);

				if(buffer.readBoolean())
				{
					int aliasCount = buffer.readVarInt();
					IntStream.range(0, aliasCount).mapToObj(j -> buffer.readUUID()).forEach(aliases::add);
				}

				builder.add(new SupporterManager.SupporterInfo(playerId, level, aliases, username));
			}

			return builder;
		});
	}

	@Override
	protected void encode(PacketBuffer buffer)
	{
		buffer.writeVarInt(networkInfos.size());

		for(SupporterManager.SupporterInfo info : networkInfos)
		{
			SupporterManager.SupporterLevel level = info.getLevel();
			Set<UUID> aliases = info.getAliases();

			buffer.writeUUID(info.getPlayerId());
			buffer.writeUtf(info.getUsername());

			if(level.isDefault())
				buffer.writeBoolean(false);
			else
			{
				buffer.writeBoolean(true);
				buffer.writeEnum(level);
			}

			if(aliases.isEmpty())
				buffer.writeBoolean(false);
			else
			{
				buffer.writeBoolean(true);
				buffer.writeVarInt(aliases.size());
				aliases.forEach(buffer::writeUUID);
			}
		}
	}

	@Override
	protected void process(NetworkManager network, NetworkEvent.Context ctx)
	{
		SupporterManager.loadSupportersFromNetwork(this);
	}
}
