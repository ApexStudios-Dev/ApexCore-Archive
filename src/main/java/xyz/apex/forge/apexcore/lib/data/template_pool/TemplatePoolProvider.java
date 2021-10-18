package xyz.apex.forge.apexcore.lib.data.template_pool;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import xyz.apex.forge.apexcore.core.ApexCore;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public abstract class TemplatePoolProvider implements IDataProvider
{
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	protected final DataGenerator generator;
	private final Map<TemplatePools, TemplatePoolBuilder> poolBuilderMap = Maps.newHashMap();

	protected TemplatePoolProvider(DataGenerator generator)
	{
		this.generator = generator;
	}

	protected abstract void registerPools();

	public TemplatePoolBuilder pool(TemplatePools pool)
	{
		return poolBuilderMap.computeIfAbsent(pool, TemplatePoolBuilder::builder);
	}

	public TemplatePoolBuilder pool(ResourceLocation poolName)
	{
		return pool(TemplatePools.of(poolName));
	}

	public TemplatePoolBuilder pool(String poolNamespace, String poolPath)
	{
		return pool(TemplatePools.of(poolNamespace, poolPath));
	}

	@Override
	public final void run(DirectoryCache cache) throws IOException
	{
		poolBuilderMap.clear();
		registerPools();
		Path dataPath = generator.getOutputFolder().resolve("data");
		poolBuilderMap.forEach((poolName, poolBuilder) -> saveTemplatePool(cache, poolBuilder, dataPath));

	}

	@Override
	public String getName()
	{
		return "TemplatePollProvider";
	}

	public static void saveTemplatePool(DirectoryCache cache, TemplatePoolBuilder builder, Path dataPath)
	{
		try
		{
			ResourceLocation poolName = builder.getPoolName();
			Path poolPath = dataPath.resolve(Paths.get(poolName.getNamespace(), "worldgen", "template_pool", poolName.getPath() + ".json"));
			JsonObject serialized = builder.serialize();
			String toWrite = GSON.toJson(serialized);
			String hashed = SHA1.hashUnencodedChars(toWrite).toString();

			if(!Files.exists(poolPath) || !Objects.equals(cache.getHash(poolPath), hashed))
			{
				Files.createDirectories(poolPath.getParent());

				try(BufferedWriter writer = Files.newBufferedWriter(poolPath))
				{
					writer.write(toWrite);
				}

				cache.putNew(poolPath, hashed);
			}
		}
		catch(IOException e)
		{
			ApexCore.LOGGER.error("Couldn't save TemplatePool {}", builder.getPoolName(), e);
		}
	}
}
