package xyz.apex.forge.apexcore.lib.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import org.apache.commons.lang3.Validate;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

import xyz.apex.forge.commonality.Mods;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class ProfileHelper
{
	private static final UUID UUID_EXAMPLE = UUID.randomUUID();
	private static final ProfileHelper INSTANCE = new ProfileHelper();
	public static final GameProfile DUMMY_PROFILE = new GameProfile(UUID_EXAMPLE, "steve");

	private final Map<String, GameProfile> gameProfileCache = Collections.synchronizedMap(Maps.newHashMap());
	private final Map<UUID, GameProfile> gameProfileCacheUUID = Collections.synchronizedMap(Maps.newHashMap());

	@Nullable private GameProfileCache profileCache;
	@Nullable private MinecraftSessionService sessionService;

	private GameProfile getGameProfile0(@Nullable UUID playerId, @Nullable String playerName) // never both null
	{
		Validate.isTrue(playerId != null || playerName != null);

		if(playerName != null && gameProfileCache.containsKey(playerName))
			return gameProfileCache.get(playerName);
		if(playerId != null && gameProfileCacheUUID.containsKey(playerId))
			return gameProfileCacheUUID.get(playerId);

		if(profileCache == null || sessionService == null)
		{
			if(FMLEnvironment.dist.isDedicatedServer())
			{
				var currentServer = ServerLifecycleHooks.getCurrentServer();
				sessionService = currentServer.getSessionService();
				profileCache = currentServer.getProfileCache();
			}
			else
				setClientProfileLookupObjects();
		}

		Validate.notNull(profileCache);
		Validate.notNull(sessionService);

		var profile = (playerId != null ? profileCache.get(playerId) : profileCache.get(playerName)).orElseGet(() -> sessionService.fillProfileProperties(new GameProfile(playerId, playerName), true));

		var property = Iterables.getFirst(profile.getProperties().get("textures"), null);

		if(property == null)
			profile = sessionService.fillProfileProperties(profile, true);

		gameProfileCache.put(profile.getName(), profile);
		gameProfileCacheUUID.put(profile.getId(), profile);
		return profile;
	}

	private void setClientProfileLookupObjects()
	{
		var mc = Minecraft.getInstance();
		var authenticationService = new YggdrasilAuthenticationService(mc.getProxy(), UUID.randomUUID().toString());
		var profileRepository = authenticationService.createProfileRepository();

		sessionService = authenticationService.createMinecraftSessionService();
		profileCache = new GameProfileCache(profileRepository, new File(mc.gameDirectory, MinecraftServer.USERID_CACHE_FILE.getName()));
	}

	public static GameProfile getGameProfile(@Nullable UUID playerId, @Nullable String playerName)
	{
		return INSTANCE.getGameProfile0(playerId, playerName);
	}

	public static void setup()
	{
		Validate.isTrue(ModLoadingContext.get().getActiveContainer().getModId().equals(Mods.APEX_CORE));

		EventBusHelper.addEnqueuedListener(FMLClientSetupEvent.class, event -> INSTANCE.setClientProfileLookupObjects());

		EventBusHelper.addListener(ServerAboutToStartEvent.class, event -> {
			var server = event.getServer();
			INSTANCE.profileCache = server.getProfileCache();
			INSTANCE.sessionService = server.getSessionService();
		});
	}
}