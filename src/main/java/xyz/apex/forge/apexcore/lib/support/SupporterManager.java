package xyz.apex.forge.apexcore.lib.support;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.core.net.ClientSyncSupportersPacket;
import xyz.apex.forge.apexcore.lib.util.ProfileHelper;
import xyz.apex.forge.apexcore.lib.util.SkinHelper;
import xyz.apex.java.utility.nullness.NonnullConsumer;
import xyz.apex.java.utility.tuple.Pair;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public final class SupporterManager
{
	public static final Gson GSON = new GsonBuilder().setLenient().create();
	// public static final String SUPPORTER_URL = "file:///D:/Development/Java/Minecraft/Forge-1.16.5/supporter.json";
	public static final String SUPPORTER_URL = "https://raw.githubusercontent.com/ApexStudios-Dev/Version/master/supporters.json";

	private static final Set<SupporterInfo> supporterInfos = Sets.newHashSet();
	private static final Map<UUID, SupporterInfo> supporterInfoMap = Maps.newHashMap();

	public static void precacheSupporterSkins()
	{
		ApexCore.LOGGER.info("Precaching Supporter Profile & Skins...");

		for(var info : supporterInfos)
		{
			ApexCore.LOGGER.info("Precaching Supporter '{}' Profile & Skins...", info.username);
			ProfileHelper.getGameProfile(info.playerId, info.username);

			for(var alias : info.aliases)
			{
				ProfileHelper.getGameProfile(alias, null);
			}

			SkinHelper.getSkins(info.playerId, info.username, NonnullConsumer.noop());
		}
	}

	public static Set<SupporterInfo> getSupporters()
	{
		return ImmutableSet.copyOf(supporterInfoMap.values());
	}

	public static boolean isSupporter(Player player)
	{
		var playerId = player.getGameProfile().getId();
		return supporterInfoMap.containsKey(playerId);
	}

	@Nullable
	public static SupporterInfo getSupporterInfo(Player player)
	{
		return findSupporterInfo(player).orElse(null);
	}

	public static Optional<SupporterInfo> findSupporterInfo(Player player)
	{
		var playerId = player.getGameProfile().getId();
		return Optional.ofNullable(supporterInfoMap.get(playerId));
	}

	public static void loadSupporters()
	{
		ApexCore.LOGGER.info("Loading Supporter Data...");
		supporterInfoMap.clear();
		supporterInfos.clear();
		new ReloadThread().start();
	}

	public static void loadSupportersFromNetwork(ClientSyncSupportersPacket packet)
	{
		ApexCore.LOGGER.info("Loading Supporter Data (From Network)...");
		supporterInfoMap.clear();
		supporterInfos.clear();
		ReloadThread.finalizeParsing(packet.networkInfos);
		ApexCore.LOGGER.info("Loaded {} Supporters (From Network)!", packet.networkInfos.size());
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> SupporterManager::precacheSupporterSkins);
	}

	public static final class ReloadThread extends Thread
	{
		private ReloadThread()
		{
			setName("ApexCore Supporter Reload Thread");
			setDaemon(true);
		}

		@Override
		public void run()
		{
			var infos = parseJson();
			finalizeParsing(infos);
			ApexCore.LOGGER.info("Loaded {} Supporters!", infos.size());

			if(ServerLifecycleHooks.getCurrentServer() != null)
				ApexCore.NETWORK.sendTo(new ClientSyncSupportersPacket(infos), PacketDistributor.ALL.noArg());
		}

		private Set<SupporterInfo> parseJson()
		{
			var infos = Sets.<SupporterInfo>newHashSet();
			var unknownElements = Lists.<Pair<Integer, JsonElement>>newArrayList();

			try(var reader = new BufferedReader(new InputStreamReader(new URL(SUPPORTER_URL).openStream())))
			{
				var json = GSON.fromJson(reader, JsonArray.class);

				for(var i = 0; i < json.size(); i++)
				{
					var element = json.get(i);
					var info = parseInfo(element);

					if(info == null)
					{
						unknownElements.add(Pair.createImmutable(i, element));
						continue;
					}

					infos.add(info);
				}
			}
			catch(IOException e)
			{
				ApexCore.LOGGER.fatal("Fatal Error occurred while parsing Supporter Json!", e);
			}

			if(!unknownElements.isEmpty())
			{
				var longest = 0;
				var lines = Lists.<String>newArrayList();

				for(var pair : unknownElements)
				{
					var index = pair.getKey();
					var element = pair.getValue();
					var elementStr = GSON.toJson(element);
					var line = "* \t#" + index + "\t=>\t`" + elementStr + "`";
					var length = line.length();
					lines.add(line);
					longest = Math.max(longest, length);
				}

				var warn = "* Found Unknown Supporter Json Elements:";
				var report = "* Please Report this to the Author (https://discord.apexmods.xyz/ | https://github.com/ApexStudios-Dev/ApexCore/issues)";
				longest = Math.max(longest, warn.length());
				longest = Math.max(longest, report.length());
				var header = Strings.repeat("*", longest);

				ApexCore.LOGGER.warn(header);
				ApexCore.LOGGER.warn(warn);
				lines.forEach(ApexCore.LOGGER::warn);
				ApexCore.LOGGER.warn("* ");
				ApexCore.LOGGER.warn(report);
				ApexCore.LOGGER.warn(header);
			}

			return infos;
		}

		private static void finalizeParsing(Set<SupporterInfo> infos)
		{
			supporterInfos.addAll(infos);
			infos.forEach(ReloadThread::finalizeInfo);
		}

		private static void finalizeInfo(SupporterInfo info)
		{
			if(!supporterInfoMap.containsKey(info.playerId))
				supporterInfoMap.put(info.playerId, info);

			for(var alias : info.aliases)
			{
				if(!supporterInfoMap.containsKey(alias))
					supporterInfoMap.put(alias, info);
			}
		}
	}

	@Nullable
	private static SupporterInfo parseInfo(JsonElement element)
	{
		if(element.isJsonPrimitive())
		{
			var playerId = parseUUID(element);

			if(playerId != null)
				return new SupporterInfo(playerId, SupporterLevel.DEFAULT, Collections.emptyList(), "Unknown");
		}
		else if(element.isJsonObject())
		{
			var json = element.getAsJsonObject();

			if(!json.has("uuid"))
				return null;

			var playerId = parseUUID(json.get("uuid"));

			if(playerId != null)
			{
				var aliases = Sets.<UUID>newHashSet();
				var level = SupporterLevel.DEFAULT;
				var username = "Unknown";

				if(json.has("level"))
					level = parseLevel(json.get("level"));
				if(json.has("aliases"))
					parseAliases(json.get("aliases"), aliases);
				if(json.has("name"))
					username = json.get("name").getAsString();

				return new SupporterInfo(playerId, level, aliases, username);
			}
		}

		return null;
	}

	private static void parseAliases(JsonElement element, Set<UUID> aliases)
	{
		if(element.isJsonArray())
		{
			var array = element.getAsJsonArray();
			var bound = array.size();
			IntStream.range(0, bound).mapToObj(i -> parseUUID(array.get(i))).filter(Objects::nonNull).forEach(aliases::add);
		}
	}

	private static SupporterLevel parseLevel(JsonElement element)
	{
		if(element.isJsonPrimitive())
		{
			var str = element.getAsString();

			for(var level : SupporterLevel.values())
			{
				if(level.serializedName.equals(str))
					return level;
			}
		}

		return SupporterLevel.DEFAULT;
	}

	@Nullable
	private static UUID parseUUID(JsonElement element)
	{
		if(element.isJsonPrimitive())
		{
			try
			{
				return UUID.fromString(element.getAsString());
			}
			catch(IllegalArgumentException e)
			{
				return null;
			}
		}

		return null;
	}

	public static final class SupporterInfo
	{
		private final UUID playerId;
		private final Set<UUID> aliases;
		private final SupporterLevel level;
		private final String username;

		public SupporterInfo(UUID playerId, SupporterLevel level, Collection<UUID> aliases, String username)
		{
			this.playerId = playerId;
			this.level = level;
			this.aliases = ImmutableSet.copyOf(aliases);
			this.username = username;
		}

		public UUID getPlayerId()
		{
			return playerId;
		}

		public SupporterLevel getLevel()
		{
			return level;
		}

		public String getUsername()
		{
			return username;
		}

		public Set<UUID> getAliases()
		{
			return aliases;
		}

		public boolean isFor(UUID playerId)
		{
			if(this.playerId.equals(playerId))
				return true;

			return aliases.contains(playerId);
		}

		public boolean isFor(Player player)
		{
			var playerId = player.getGameProfile().getId();
			return isFor(playerId);
		}

		@Nullable
		public Player getPlayer(Function<UUID, Player> playerIdToPlayerFunction)
		{
			var player = playerIdToPlayerFunction.apply(playerId);

			if(player != null)
				return player;

			for(var alias : aliases)
			{
				player = playerIdToPlayerFunction.apply(alias);

				if(player != null)
					return player;
			}

			return null;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(this == obj)
				return true;
			if(obj instanceof SupporterInfo other)
				return playerId.equals(other.playerId) && level.equals(other.level) && aliases.containsAll(other.aliases) && username.equals(other.username);

			return false;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(playerId, level, aliases, username);
		}

		@Override
		public String toString()
		{
			var str = new StringBuilder("SupporterInfo{ '" + playerId + "', '" + level.serializedName + '\'' + "', '" + username + '\'');

			if(!aliases.isEmpty())
			{
				str.append(", [");
				var counter = 0;

				for(var alias : aliases)
				{
					str.append('\'').append(alias).append('\'');

					if(counter < aliases.size() - 1)
						str.append(',');

					counter++;
				}

				str.append(']');
			}

			return str.append(" }").toString();
		}

		public JsonElement toJson()
		{
			if(!level.isDefault() || !aliases.isEmpty())
			{
				var json = new JsonObject();
				json.addProperty("uuid", playerId.toString());

				if(!level.isDefault())
					json.addProperty("level", level.serializedName);

				if(!aliases.isEmpty())
				{
					var array = new JsonArray();
					aliases.stream().map(UUID::toString).forEach(array::add);
					json.add("aliases", array);
				}

				return json;
			}
			else
				return GSON.fromJson(GSON.toJson(playerId.toString()), JsonElement.class);
		}
	}

	public enum SupporterLevel implements StringRepresentable
	{
		// Levels specific to internal contributors
		// People within ApexStudios
		OWNER("owner", 4),
		DEVELOPER("developer", 3),
		ARTIST("artist", 2),

		// Public levels either someone who has
		// contributed towards one of our mods development
		// or is just supporting our mods in some way (nitro boosting, patron, etc)
		CONTRIBUTOR("contributor", 1),
		SUPPORTER("supporter", 0);

		// default level given to all supporters if level is specific for a given user
		public static final SupporterLevel DEFAULT = SUPPORTER;

		private final String serializedName;
		private final int level;

		SupporterLevel(String serializedName, int level)
		{
			this.serializedName = serializedName;
			this.level = level;
		}

		@Override
		public String getSerializedName()
		{
			return serializedName;
		}

		public int getLevel()
		{
			return level;
		}

		public boolean isBetterThan(SupporterLevel other)
		{
			return level > other.level;
		}

		public boolean isDefault()
		{
			return this == DEFAULT;
		}
	}
}
