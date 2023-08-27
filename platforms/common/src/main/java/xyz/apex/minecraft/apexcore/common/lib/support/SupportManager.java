package xyz.apex.minecraft.apexcore.common.lib.support;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.network.S2CPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@ApiStatus.Internal
public final class SupportManager
{
    public static final SupportManager INSTANCE = new SupportManager();
    private static final Marker MARKER = MarkerManager.getMarker("SUPPORT");
    private static final Set<SupportLevel> EMPTY_LEVELS = Collections.singleton(SupportLevel.NONE);

    // TODO: Move to ApexCore if we need network for other things in ApexCore
    private final NetworkManager network;
    private final S2CPacket<SupportSyncPacket> supportSync;
    private final Map<UUID, EnumSet<SupportLevel>> levels = Maps.newHashMap();
    private boolean enabled = true;

    private SupportManager()
    {
        network = NetworkManager.create(ApexCore.ID);
        supportSync = network.registerClientBound("support_sync", SupportSyncPacket::encode, SupportSyncPacket::new, () -> () -> SupportSyncPacket::consume);
    }

    public Set<SupportLevel> get(GameProfile profile)
    {
        if(!enabled)
            return EMPTY_LEVELS;

        var set = levels.get(profile.getId());
        return set == null ? EMPTY_LEVELS : Collections.unmodifiableSet(set);
    }

    void fromNetwork(SupportSyncPacket packet)
    {
        if(!enabled)
            return;

        ApexCore.LOGGER.info(MARKER, "Syncing SupportLevels from network");
        levels.clear();
        packet.levels.keySet().forEach(uuid -> levels.put(uuid, EnumSet.copyOf(packet.levels.get(uuid))));
    }

    public void sync(MinecraftServer server)
    {
        if(!enabled)
            return;

        ApexCore.LOGGER.info(MARKER, "Syncing SupportLevels to all clients");
        var packet = new SupportSyncPacket(levels);
        server.getPlayerList().getPlayers().forEach(player -> supportSync.sendToClient(player, packet));
    }

    public void sync(ServerPlayer player)
    {
        if(!enabled)
            return;

        ApexCore.LOGGER.info(MARKER, "Syncing SupportLevels to client: {} ({})", player.getScoreboardName(), player.getUUID());
        supportSync.sendToClient(player, new SupportSyncPacket(levels));
    }

    public void loadFromJson(JsonElement json)
    {
        levels.clear();

        var obj = json.getAsJsonObject();

        for(var key : obj.keySet())
        {
            var uuid = UUID.fromString(key);
            var levelsSets = EnumSet.noneOf(SupportLevel.class);

            for(var levelJson : obj.getAsJsonArray(key))
            {
                levelsSets.add(SupportLevel.byName(levelJson.getAsString()));
            }

            levels.put(uuid, EnumSet.copyOf(levelsSets));
        }
    }

    public void loadFromRemote()
    {
        ApexCore.LOGGER.info(MARKER, "Loading SupportLevel definitions from remote");
        levels.clear();
        var gson = new GsonBuilder().setLenient().create();
        var url = "https://raw.githubusercontent.com/ApexStudios-Dev/.github/master/supporters.json";

        try
        {
            var connection = new URL(url).openConnection();

            try(var reader = new BufferedReader(new InputStreamReader(connection.getInputStream())))
            {
                loadFromJson(gson.fromJson(reader, JsonElement.class));
            }
        }
        catch(IOException e)
        {
            ApexCore.LOGGER.error(MARKER, "Error occurred while loading Supporter data from remote! Supporter system will be disabled.", e);
            enabled = false;
            return;
        }

        ApexCore.LOGGER.info(MARKER, "Loaded {} SupporterLevel definitions", levels.size());
    }
}
