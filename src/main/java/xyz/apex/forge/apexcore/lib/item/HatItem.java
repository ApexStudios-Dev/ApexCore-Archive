package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.loading.FMLEnvironment;

import xyz.apex.forge.apexcore.lib.HatsRegistry;

import java.util.Objects;

public final class HatItem extends WearableItem
{
	private final ResourceLocation hatName;

	public HatItem(Properties properties, ResourceLocation hatName)
	{
		super(properties, EquipmentSlotType.HEAD);

		this.hatName = hatName;
	}

	@Override
	public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> stacks)
	{
		if(FMLEnvironment.dist.isClient())
		{
			if(allowdedIn(itemGroup))
			{
				HatsRegistry.CompiledHat hatData = getHatData();

				for(int i = 0; i < hatData.getHatTextures().size(); i++)
				{
					ItemStack stack = getDefaultInstance();
					stack.getOrCreateTag().putInt(HatsRegistry.NBT_HAT_VARIANT, i);
					stacks.add(stack);
				}
			}
		}
	}

	@Override
	public ItemStack getDefaultInstance()
	{
		ItemStack stack = super.getDefaultInstance();
		stack.getOrCreateTag().putInt(HatsRegistry.NBT_HAT_VARIANT, 0);
		return stack;
	}

	public HatsRegistry.CompiledHat getHatData()
	{
		return Objects.requireNonNull(HatsRegistry.getHat(hatName));
	}

	public ResourceLocation getActiveHatTexture(ItemStack stack)
	{
		int hatVariant = getActiveHatTextureIndex(stack);
		return getHatData().getHatTextures().get(hatVariant);
	}

	public int getActiveHatTextureIndex(ItemStack stack)
	{
		CompoundNBT stackTag = stack.getTag();

		if(stackTag != null && stackTag.contains(HatsRegistry.NBT_HAT_VARIANT, Constants.NBT.TAG_ANY_NUMERIC))
			return stackTag.getInt(HatsRegistry.NBT_HAT_VARIANT);

		return 0;
	}
}
