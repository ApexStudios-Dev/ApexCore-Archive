package xyz.apex.forge.apexcore.lib.data.template_pool;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.function.Supplier;

public class TemplatePoolBuilder
{
	private final TemplatePools pool;
	private final LinkedList<ElementBuilder> elements = Lists.newLinkedList();
	@Nullable private TemplatePools fallbackPool = TemplatePools.EMPTY;

	private TemplatePoolBuilder(TemplatePools pool)
	{
		this.pool = pool;
	}

	public TemplatePoolBuilder fallback(@Nullable TemplatePools fallbackPool)
	{
		this.fallbackPool = fallbackPool;
		return this;
	}

	public ElementBuilder element()
	{
		ElementBuilder element = new ElementBuilder(this);
		elements.add(element);
		return element;
	}

	public ElementBuilder element(int elementIndex)
	{
		return elements.get(elementIndex);
	}

	public ElementBuilder firstElement()
	{
		return elements.getFirst();
	}

	public ElementBuilder lastElement()
	{
		return elements.getLast();
	}

	public TemplatePools getPool()
	{
		return pool;
	}

	public ResourceLocation getPoolName()
	{
		return getPool().getPoolName();
	}

	public TemplatePools getFallbackPool()
	{
		return fallbackPool == null ? TemplatePools.EMPTY : fallbackPool;
	}

	public ResourceLocation getFallbackPoolName()
	{
		return getFallbackPool().getPoolName();
	}

	public JsonObject serialize()
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", getPoolName().toString());
		json.addProperty("fallback", getFallbackPoolName().toString());

		JsonArray elementsJson = new JsonArray();
		elements.stream().map(ElementBuilder::serialize).forEach(elementsJson::add);
		json.add("elements", elementsJson);

		return json;
	}

	public static TemplatePoolBuilder builder(TemplatePools pool)
	{
		return new TemplatePoolBuilder(pool);
	}

	public static class ElementBuilder
	{
		private final TemplatePoolBuilder poolBuilder;
		private int weight = 100;
		private Supplier<Structure<?>> structureSupplier = () -> null;
		private NonNullSupplier<StructureProcessorList> processorSupplier = () -> ProcessorLists.EMPTY;
		private JigsawPattern.PlacementBehaviour projection = JigsawPattern.PlacementBehaviour.RIGID;
		private NonNullSupplier<IJigsawDeserializer<?>> elementTypeSupplier = () -> IJigsawDeserializer.SINGLE;

		private ElementBuilder(TemplatePoolBuilder poolBuilder)
		{
			this.poolBuilder = poolBuilder;
		}

		public ElementBuilder weight(int weight)
		{
			this.weight = weight;
			return this;
		}

		public ElementBuilder location(NonNullSupplier<Structure<?>> structureSupplier)
		{
			this.structureSupplier = structureSupplier;
			return this;
		}

		public ElementBuilder processor(NonNullSupplier<StructureProcessorList> processorSupplier)
		{
			this.processorSupplier = processorSupplier;
			return this;
		}

		public ElementBuilder projection(JigsawPattern.PlacementBehaviour projection)
		{
			this.projection = projection;
			return this;
		}

		public ElementBuilder elementType(NonNullSupplier<IJigsawDeserializer<?>> elementTypeSupplier)
		{
			this.elementTypeSupplier = elementTypeSupplier;
			return this;
		}

		public int getWeight()
		{
			return MathHelper.clamp(weight, 0, 100);
		}

		public Structure<?> getStructure()
		{
			return Validate.notNull(structureSupplier.get());
		}

		public ResourceLocation getStructureName()
		{
			return Validate.notNull(getStructure().getRegistryName());
		}

		public StructureProcessorList getProcessor()
		{
			return processorSupplier.get();
		}

		public ResourceLocation getProcessorName()
		{
			return Validate.notNull(WorldGenRegistries.PROCESSOR_LIST.getKey(getProcessor()));
		}

		public JigsawPattern.PlacementBehaviour getProjection()
		{
			return projection;
		}

		public String getProjectionName()
		{
			return getProjection().getSerializedName();
		}

		public IJigsawDeserializer<?> getElementType()
		{
			return elementTypeSupplier.get();
		}

		public ResourceLocation getElementTypeName()
		{
			return Validate.notNull(Registry.STRUCTURE_POOL_ELEMENT.getKey(getElementType()));
		}

		public JsonObject serialize()
		{
			JsonObject element = new JsonObject();
			element.addProperty("location", getStructureName().toString());
			element.addProperty("processors", getProcessorName().toString());
			element.addProperty("projection", getProjectionName());
			element.addProperty("element_type", getElementTypeName().toString());

			JsonObject json = new JsonObject();
			json.addProperty("weight", getWeight());
			json.add("element", element);
			return json;
		}

		public TemplatePoolBuilder end()
		{
			return poolBuilder;
		}
	}
}
