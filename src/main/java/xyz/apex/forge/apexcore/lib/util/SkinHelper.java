package xyz.apex.forge.apexcore.lib.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class SkinHelper
{
	private static final SkinHelper INSTANCE = new SkinHelper();

	private final Cache<String, ResourceLocation> skinCache = CacheBuilder.newBuilder().expireAfterAccess(14, TimeUnit.MINUTES).build();
	private final Cache<String, Set<Consumer<ResourceLocation>>> callbackMap = CacheBuilder.newBuilder().expireAfterWrite(12, TimeUnit.SECONDS).build();
	private final Map<String, Boolean> slimCache = Collections.synchronizedMap(Maps.newHashMap());
	private final Map<UUID, Boolean> slimCacheUUID = Collections.synchronizedMap(Maps.newHashMap());

	private void getSkins0(@Nullable UUID playerId, String playerName, Consumer<ResourceLocation> callback)
	{
		var skinLocation = skinCache.getIfPresent(playerName);

		if(skinLocation != null)
		{
			callback.accept(skinLocation);
			return;
		}

		var mc = Minecraft.getInstance();
		var connection = mc.getConnection();
		var playerInfo = connection == null ? null : connection.getPlayerInfo(playerName);

		if(playerInfo != null)
		{
			skinLocation = playerInfo.getSkinLocation();

			if(skinLocation != DefaultPlayerSkin.getDefaultSkin(playerInfo.getProfile().getId()))
			{
				callback.accept(skinLocation);
				skinCache.put(playerName, skinLocation);
				return;
			}
		}

		if(playerId == null)
			return;

		synchronized(callbackMap)
		{
			var consumers = callbackMap.getIfPresent(playerName);

			if(consumers == null)
			{
				var profile = ProfileHelper.getGameProfile(playerId, playerName);

				mc.getSkinManager().registerSkins(profile, (type, location, profileTexture) -> {
					if(type == MinecraftProfileTexture.Type.SKIN)
					{
						synchronized(callbackMap)
						{
							var model = profileTexture.getMetadata("model");
							var isSlim = model != null && model.equals("slim");

							slimCache.put(playerName, isSlim);
							slimCacheUUID.put(playerId, isSlim);

							var consumerSet = callbackMap.getIfPresent(playerName);

							if(consumerSet != null)
								consumerSet.forEach(consumer -> consumer.accept(location));

							callbackMap.invalidate(playerName);
							callbackMap.cleanUp();
						}

						skinCache.put(playerName, location);
					}
				}, true);

				var set = Sets.<Consumer<ResourceLocation>>newHashSet();
				set.add(callback);
				callbackMap.put(playerName, set);
			}
			else
				consumers.add(callback);
		}
	}

	public static void getSkins(@Nullable UUID playerId, String playerName, Consumer<ResourceLocation> callback)
	{
		INSTANCE.getSkins0(playerId, playerName, callback);
	}

	public static boolean isSlim(@Nullable UUID playerId, String playerName)
	{
		if(INSTANCE.slimCache.containsKey(playerName) && INSTANCE.slimCache.get(playerName))
			return true;
		if(playerId != null && INSTANCE.slimCacheUUID.containsKey(playerId) && INSTANCE.slimCacheUUID.get(playerId))
			return true;
		return false;
	}

	public static void invalidateCaches()
	{
		INSTANCE.skinCache.invalidateAll();
		INSTANCE.skinCache.cleanUp();

		INSTANCE.callbackMap.invalidateAll();
		INSTANCE.callbackMap.cleanUp();

		INSTANCE.slimCache.clear();
		INSTANCE.slimCacheUUID.clear();
	}
}
