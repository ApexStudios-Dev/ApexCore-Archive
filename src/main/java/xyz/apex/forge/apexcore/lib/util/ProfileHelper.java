package xyz.apex.forge.apexcore.lib.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import org.apache.commons.lang3.Validate;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import xyz.apex.forge.apexcore.core.ApexCore;

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

	@Nullable private PlayerProfileCache profileCache;
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
				MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
				sessionService = currentServer.getSessionService();
				profileCache = currentServer.getProfileCache();
			}
			else
				setClientProfileLookupObjects();
		}

		Validate.notNull(profileCache);
		Validate.notNull(sessionService);

		GameProfile profile = playerId != null ? profileCache.get(playerId) : profileCache.get(playerName);

		if(profile == null)
			profile = sessionService.fillProfileProperties(new GameProfile(playerId, playerName), true);

		Property property = Iterables.getFirst(profile.getProperties().get("textures"), null);

		if(property == null)
			profile = sessionService.fillProfileProperties(profile, true);

		gameProfileCache.put(profile.getName(), profile);
		gameProfileCacheUUID.put(profile.getId(), profile);
		return profile;
	}

	@OnlyIn(Dist.CLIENT)
	private void setClientProfileLookupObjects()
	{
		Minecraft mc = Minecraft.getInstance();
		YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(mc.getProxy(), UUID.randomUUID().toString());
		GameProfileRepository profileRepository = authenticationService.createProfileRepository();

		sessionService = authenticationService.createMinecraftSessionService();
		profileCache = new PlayerProfileCache(profileRepository, new File(mc.gameDirectory, MinecraftServer.USERID_CACHE_FILE.getName()));
	}

	public static GameProfile getGameProfile(@Nullable UUID playerId, @Nullable String playerName)
	{
		return INSTANCE.getGameProfile0(playerId, playerName);
	}

	public static void setup()
	{
		Validate.isTrue(ModLoadingContext.get().getActiveContainer().getModId().equals(ApexCore.ID));

		EventBusHelper.addEnqueuedListener(FMLClientSetupEvent.class, event -> INSTANCE.setClientProfileLookupObjects());

		EventBusHelper.addListener(FMLServerAboutToStartEvent.class, event -> {
			MinecraftServer server = event.getServer();
			INSTANCE.profileCache = server.getProfileCache();
			INSTANCE.sessionService = server.getSessionService();
		});
	}
}
