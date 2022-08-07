package xyz.apex.forge.apexcore.registrate.holder;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

@SuppressWarnings("unchecked")
public interface FluidHolder<OWNER extends CoreRegistrate<OWNER> & FluidHolder<OWNER>>
{
	private OWNER self()
	{
		return (OWNER) this;
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid()
	{
		return fluid(self());
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(self(), typeFactory);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(NonNullSupplier<FluidType> fluidType)
	{
		return fluid(self(), fluidType);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return fluid(self(), stillTexture, flowingTexture);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(self(), stillTexture, flowingTexture, typeFactory);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return fluid(self(), stillTexture, flowingTexture, fluidType);
	}

	default <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(self(), stillTexture, flowingTexture, fluidFactory);
	}

	default <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(self(), stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	default <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(self(), stillTexture, flowingTexture, fluidType, fluidFactory);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name)
	{
		return fluid(self(), name);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(self(), name, typeFactory);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, NonNullSupplier<FluidType> fluidType)
	{
		return fluid(self(), name, fluidType);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return fluid(self(), name, stillTexture, flowingTexture);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(self(), name, stillTexture, flowingTexture, typeFactory);
	}

	default FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return fluid(self(), name, stillTexture, flowingTexture, fluidType);
	}

	default <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(self(), name, stillTexture, flowingTexture, fluidFactory);
	}

	default <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(self(), name, stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	default <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(self(), name, stillTexture, flowingTexture, fluidType, fluidFactory);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent)
	{
		return fluid(parent, self().currentName());
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(parent, self().currentName(), typeFactory);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, NonNullSupplier<FluidType> fluidType)
	{
		return fluid(parent, self().currentName(), fluidType);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return fluid(parent, self().currentName(), stillTexture, flowingTexture);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(parent, self().currentName(), stillTexture, flowingTexture, typeFactory);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return fluid(parent, self().currentName(), stillTexture, flowingTexture, fluidType);
	}

	default <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(parent, self().currentName(), stillTexture, flowingTexture, fluidFactory);
	}

	default <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(parent, self().currentName(), stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	default <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return fluid(parent, self().currentName(), stillTexture, flowingTexture, fluidType, fluidFactory);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name)
	{
		return fluid(parent, name, new ResourceLocation(self().modId, "block/%s_still".formatted(self().currentName())), new ResourceLocation(self().modId, "block/%s_flow".formatted(self().currentName())));
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return fluid(parent, name, new ResourceLocation(self().modId, "block/%s_still".formatted(self().currentName())), new ResourceLocation(self().modId, "block/%s_flow".formatted(self().currentName())), typeFactory);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, NonNullSupplier<FluidType> fluidType)
	{
		return fluid(parent, name, new ResourceLocation(self().modId, "block/%s_still".formatted(self().currentName())), new ResourceLocation(self().modId, "block/%s_flow".formatted(self().currentName())), fluidType);
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return self().entry(name, callback -> FluidBuilder.create(self().backend, parent, name, callback, stillTexture, flowingTexture));
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return self().entry(name, callback -> FluidBuilder.create(self().backend, parent, name, callback, stillTexture, flowingTexture, typeFactory));
	}

	default <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return self().entry(name, callback -> FluidBuilder.create(self().backend, parent, name, callback, stillTexture, flowingTexture, fluidType));
	}

	default <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return self().entry(name, callback -> FluidBuilder.create(self().backend, parent, name, callback, stillTexture, flowingTexture, fluidFactory));
	}

	default <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return self().entry(name, callback -> FluidBuilder.create(self().backend, parent, name, callback, stillTexture, flowingTexture, typeFactory, fluidFactory));
	}

	default <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return self().entry(name, callback -> FluidBuilder.create(self().backend, parent, name, callback, stillTexture, flowingTexture, fluidType, fluidFactory));
	}
}