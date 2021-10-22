package xyz.apex.forge.apexcore.lib.util.reflection;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public enum Fields
{
	// 1.16.5: private ItemStack iconItemStack;
	ITEM_GROUP_ICON("field_151245_t", true, ItemGroup.class),
	// 1.16.5: private final String langId;
	ITEM_GROUP_LABEL("field_78034_o", true, ItemGroup.class),
	// 1.16.5: private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
	ARMOR_MATERIAL_HEALTH_PER_SLOT("field_77882_bY", true, ArmorMaterial.class)
	;

	private final Class<?> classToAccess;
	private final String fieldName;
	private final boolean isObfuscated;

	Fields(String fieldName, boolean isObfuscated, Class<?> classToAccess)
	{
		this.fieldName = fieldName;
		this.isObfuscated = isObfuscated;
		this.classToAccess = classToAccess;
	}

	public Field findField()
	{
		return FieldHelper.findField(this);
	}

	@Nullable
	public <E> E getPrivateValue(@Nullable Object instance)
	{
		return FieldHelper.getPrivateValue(instance, this);
	}

	public <E> E setPrivateValue(@Nullable Object instance, @Nullable E newValue)
	{
		return FieldHelper.setPrivateValue(instance, newValue, this);
	}

	public String getFieldName()
	{
		return FieldHelper.getObfuscatedFieldName(this);
	}

	public Class<?> getClassToAccess()
	{
		return classToAccess;
	}

	public String getFieldNameRaw()
	{
		return fieldName;
	}

	public boolean isObfuscated()
	{
		return isObfuscated;
	}
}
