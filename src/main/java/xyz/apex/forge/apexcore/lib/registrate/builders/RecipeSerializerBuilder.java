package xyz.apex.forge.apexcore.lib.registrate.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import xyz.apex.forge.apexcore.lib.registrate.entry.RecipeSerializerEntry;

public class RecipeSerializerBuilder<T extends IRecipeSerializer<R>, P, R extends IRecipe<I>, I extends IInventory> extends AbstractBuilder<IRecipeSerializer<?>, T, P, RecipeSerializerBuilder<T, P, R, I>>
{
	private final RecipeSerializerFactory<T, R, I> factory;

	protected RecipeSerializerBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, RecipeSerializerFactory<T, R, I> factory)
	{
		super(owner, parent, name, callback, IRecipeSerializer.class);

		this.factory = factory;
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate)
	{
		return new RecipeSerializerEntry<>(getOwner(), delegate);
	}

	@Override
	protected @NonnullType T createEntry()
	{
		return factory.create();
	}

	@Override
	public RecipeSerializerEntry<T, R, I> register()
	{
		return (RecipeSerializerEntry<T, R, I>) super.register();
	}

	public static <T extends IRecipeSerializer<R>, P, R extends IRecipe<I>, I extends IInventory> RecipeSerializerBuilder<T, P, R, I> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, RecipeSerializerFactory<T, R, I> factory)
	{
		return new RecipeSerializerBuilder<>(owner, parent, name, callback, factory);
	}

	@FunctionalInterface
	public interface RecipeSerializerFactory<T extends IRecipeSerializer<R>, R extends IRecipe<I>, I extends IInventory>
	{
		T create();
	}
}
