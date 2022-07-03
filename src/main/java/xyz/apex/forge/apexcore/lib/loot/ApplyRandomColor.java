package xyz.apex.forge.apexcore.lib.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import xyz.apex.forge.apexcore.core.init.ACLootFunctionTypes;
import xyz.apex.forge.apexcore.lib.constants.NbtNames;

@Deprecated(forRemoval = true)
public class ApplyRandomColor extends LootItemConditionalFunction
{
	private ApplyRandomColor(LootItemCondition[] conditions)
	{
		super(conditions);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext ctx)
	{
		var color = generateColor(ctx);
		stack.getOrCreateTagElement(NbtNames.DISPLAY).putInt(NbtNames.COLOR, color);
		return stack;
	}

	@Override
	public LootItemFunctionType getType()
	{
		return ACLootFunctionTypes.APPLY_RANDOM_COLOR;
	}

	private int generateColor(LootContext ctx)
	{
		return ctx.getRandom().nextInt(0xFFFFFF + 1);
	}

	public static LootItemFunction.Builder apply()
	{
		return simpleBuilder(ApplyRandomColor::new);
	}

	public static final class Serializer extends LootItemConditionalFunction.Serializer<ApplyRandomColor>
	{
		@Override
		public ApplyRandomColor deserialize(JsonObject json, JsonDeserializationContext ctx, LootItemCondition[] conditions)
		{
			return new ApplyRandomColor(conditions);
		}
	}
}