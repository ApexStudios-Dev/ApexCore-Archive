package xyz.apex.forge.apexcore.lib.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.versions.forge.ForgeVersion;
import xyz.apex.forge.apexcore.core.ApexCore;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface IMod
{
	IMod APEX_CORE = createCaching(ApexCore.ID);
	IMod FANTASY_COTTAGE = createCaching("fantasycottage");
	IMod FORGE = createCaching(ForgeVersion.MOD_ID);
	IMod MINECRAFT = createCaching("minecraft");
	IMod JEI = create("jei");
	IMod TOP = create("theoneprobe");
	IMod TERRAFORGED = create("terraforged");

	String getModId();

	default boolean isLoaded()
	{
		return ModList.get().isLoaded(getModId());
	}

	default Optional<? extends ModContainer> getModContainer()
	{
		return ModList.get().getModContainerById(getModId());
	}

	default String getDisplayName()
	{
		return getModContainer().map(ModContainer::getModInfo).map(IModInfo::getDisplayName).orElse(getModId());
	}

	default ResourceLocation id(String path)
	{
		return new ResourceLocation(getModId(), path);
	}

	default String prefix(String path)
	{
		return getModId() + ':' + path;
	}

	static IMod create(String modId, boolean caching)
	{
		return caching ? new CachingMod(modId) : new SimpleMod(modId);
	}

	static IMod create(String modId)
	{
		return create(modId, false);
	}

	static IMod createCaching(String modId)
	{
		return create(modId, true);
	}

	class SimpleMod implements IMod
	{
		public final String modId;

		protected SimpleMod(String modId)
		{
			this.modId = modId;
		}

		@Override
		public String getModId()
		{
			return modId;
		}
	}

	class CachingMod extends SimpleMod
	{
		private final Cache<String, ResourceLocation> resourceLocationCache = CacheBuilder.newBuilder().weakValues().build();

		protected CachingMod(String modId)
		{
			super(modId);
		}

		@Override
		public ResourceLocation id(String path)
		{
			try
			{
				return resourceLocationCache.get(path, () -> super.id(path));
			}
			catch(ExecutionException e)
			{
				return super.id(path);
			}
		}
	}
}
