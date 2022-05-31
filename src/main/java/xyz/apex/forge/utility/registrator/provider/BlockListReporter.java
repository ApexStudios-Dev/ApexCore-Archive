package xyz.apex.forge.utility.registrator.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class BlockListReporter implements DataProvider
{
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final AbstractRegistrator<?> registrator;
	private final DataGenerator generator;

	public BlockListReporter(AbstractRegistrator<?> registrator, DataGenerator generator)
	{
		this.registrator = registrator;
		this.generator = generator;
	}

	@Override
	public void run(HashCache cache) throws IOException
	{
		var path = generator.getOutputFolder().resolve(Paths.get("reports", registrator.getModId(), "blocks.json"));
		var json = serializeBlocks();
		DataProvider.save(GSON, cache, json, path);
	}

	@Override
	public String getName()
	{
		return "Block List: " + registrator.getModId();
	}

	private Iterable<Block> getBlocks()
	{
		// return ForgeRegistries.BLOCKS;

		return registrator
				.getAll(Block.class)
				.stream()
				.filter(RegistryEntry::isPresent)
				.sorted((a, b) -> a.getId().compareNamespaced(b.getId()))
				.distinct()
				.map(RegistryEntry::get)
				.collect(ImmutableList.toImmutableList())
		;
	}

	private JsonObject serializeBlocks()
	{
		var json = new JsonObject();
		getBlocks().forEach(block -> json.add(Objects.requireNonNull(block.getRegistryName()).toString(), serializeBlock(block)));
		return json;
	}

	private JsonObject serializeBlock(Block block)
	{
		var blockJson = new JsonObject();
		blockJson.add("properties", serializeBlockProperties(block));
		blockJson.add("states", serializeBlockStates(block));
		return blockJson;
	}

	private JsonObject serializeBlockProperties(Block block)
	{
		var json = new JsonObject();
		block.getStateDefinition().getProperties().forEach(property -> json.add(property.getName(), serializeBlockPropertyPossibleValues(property)));
		return json;
	}

	private JsonArray serializeBlockPropertyPossibleValues(Property<?> property)
	{
		var json = new JsonArray();
		property.getPossibleValues().stream().map(comparable -> Util.getPropertyName(property, comparable)).forEach(json::add);
		return json;
	}

	private JsonArray serializeBlockStates(Block block)
	{
		var json = new JsonArray();
		block.getStateDefinition().getPossibleStates().stream().map(blockState -> serializeBlockState(block, blockState)).forEach(json::add);
		return json;
	}

	private JsonObject serializeBlockState(Block block, BlockState blockState)
	{
		var json = new JsonObject();
		json.add("properties", serializeBlockStateProperties(block, blockState));
		json.addProperty("id", Block.getId(blockState));
		json.addProperty("default", blockState == block.defaultBlockState());
		return json;
	}

	private JsonObject serializeBlockStateProperties(Block block, BlockState blockState)
	{
		var json = new JsonObject();
		block.getStateDefinition().getProperties().forEach(property -> json.addProperty(property.getName(), Util.getPropertyName(property, blockState.getValue(property))));
		return json;
	}
}
