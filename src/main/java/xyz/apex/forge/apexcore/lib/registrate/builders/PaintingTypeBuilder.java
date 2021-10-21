package xyz.apex.forge.apexcore.lib.registrate.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.entity.item.PaintingType;

import java.util.function.IntSupplier;

public class PaintingTypeBuilder<T extends PaintingType, P> extends AbstractBuilder<PaintingType, T, P, PaintingTypeBuilder<T, P>>
{
	private final PaintingTypeFactory<T> factory;
	private final IntSupplier widthSupplier;
	private final IntSupplier heightSupplier;

	protected PaintingTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, PaintingTypeFactory<T> factory, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		super(owner, parent, name, callback, PaintingType.class);

		this.factory = factory;
		this.widthSupplier = widthSupplier;
		this.heightSupplier = heightSupplier;
	}

	@Override
	protected @NonnullType T createEntry()
	{
		return factory.create(widthSupplier.getAsInt(), heightSupplier.getAsInt());
	}

	public static <T extends PaintingType, P> PaintingTypeBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, PaintingTypeFactory<T> factory, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return new PaintingTypeBuilder<>(owner, parent, name, callback, factory, widthSupplier, heightSupplier);
	}

	@FunctionalInterface
	public interface PaintingTypeFactory<T extends PaintingType>
	{
		T create(int width, int height);
	}
}
