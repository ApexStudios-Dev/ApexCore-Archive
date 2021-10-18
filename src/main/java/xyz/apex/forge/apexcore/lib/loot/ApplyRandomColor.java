package xyz.apex.forge.apexcore.lib.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import xyz.apex.forge.apexcore.core.init.ALootFunctionTypes;
import xyz.apex.forge.apexcore.lib.constants.NbtNames;

public class ApplyRandomColor extends LootFunction
{
	private ApplyRandomColor(ILootCondition[] conditions)
	{
		super(conditions);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext ctx)
	{
		int color = generateColor(ctx);
		stack.getOrCreateTagElement(NbtNames.DISPLAY).putInt(NbtNames.COLOR, color);
		return stack;
	}

	@Override
	public LootFunctionType getType()
	{
		return ALootFunctionTypes.APPLY_RANDOM_COLOR;
	}

	private int generateColor(LootContext ctx)
	{
		return ctx.getRandom().nextInt(0xFFFFFF + 1);
	}

	public static LootFunction.Builder<?> apply()
	{
		return simpleBuilder(ApplyRandomColor::new);
	}

	public static final class Serializer extends LootFunction.Serializer<ApplyRandomColor>
	{
		@Override
		public ApplyRandomColor deserialize(JsonObject json, JsonDeserializationContext ctx, ILootCondition[] conditions)
		{
			return new ApplyRandomColor(conditions);
		}
	}
}
