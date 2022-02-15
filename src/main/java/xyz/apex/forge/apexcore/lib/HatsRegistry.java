package xyz.apex.forge.apexcore.lib;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.fml.loading.FMLEnvironment;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.core.client.hats.HatModel;
import xyz.apex.forge.apexcore.core.init.ACTags;
import xyz.apex.forge.apexcore.lib.item.HatItem;
import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.builder.ItemBuilder;
import xyz.apex.java.utility.nullness.NonnullBiFunction;
import xyz.apex.java.utility.nullness.NonnullFunction;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;
import java.util.*;

public final class HatsRegistry
{
	private static final Map<ResourceLocation, CompiledHat> hatRegistry = Maps.newHashMap();

	public static final ResourceLocation HAT_MODEL_VARIANT_ITEM_PROPERTY = new ResourceLocation(ApexCore.ID, "hat_model_variant");
	public static final String NBT_HAT_VARIANT = "HatVariant";

	public static Collection<CompiledHat> getHats()
	{
		return hatRegistry.values();
	}

	public static Set<ResourceLocation> getHatNames()
	{
		return hatRegistry.keySet();
	}

	public static <OWNER extends AbstractRegistrator<OWNER>, ITEM extends HatItem> ItemBuilder<OWNER, ITEM, OWNER> hatItem(OWNER owner, ResourceLocation hatName, NonnullBiFunction<Item.Properties, ResourceLocation, ITEM> itemFactory, ResourceLocation parentItemModelName, String textureName)
	{
		return owner.item(hatName.getPath(), properties -> itemFactory.apply(properties, hatName))
		               .stacksTo(1)
		               .model((ctx, provider) -> {
			               CompiledHat hat = ctx.get().getHatData();
			               ResourceLocation hatModelName = new ResourceLocation(hatName.getNamespace(), "item/hats/" + hatName.getPath());
			               ItemModelBuilder itemModelBuilder = provider.withExistingParent(ctx.getName(), parentItemModelName);
			               List<ResourceLocation> hatTextures = hat.getHatTextures();

			               for(int i = 0; i < hatTextures.size(); i++)
			               {
				               ResourceLocation texture = hatTextures.get(i);
				               String path = texture.getPath();
				               path = StringUtils.removeStart(path, "textures/");
				               path = StringUtils.removeEnd(path, ".png");
				               texture = new ResourceLocation(texture.getNamespace(), path);

				               itemModelBuilder.override()
				                               .predicate(HAT_MODEL_VARIANT_ITEM_PROPERTY, i)
				                               .model(provider
						                               .withExistingParent(hatModelName + "/" + i, ctx.getId())
						                               .texture(textureName, texture)
				                               )
				                               .end();
			               }
		               })
                       .onRegister(item -> {
						   if(FMLEnvironment.dist.isClient())
							   ItemModelsProperties.register(item, HAT_MODEL_VARIANT_ITEM_PROPERTY, (stack, level, entity) -> ((HatItem) stack.getItem()).getActiveHatTextureIndex(stack));
		               })
                       .tag(ACTags.Items.HATS)
		;
	}

	public static <OWNER extends AbstractRegistrator<OWNER>> ItemBuilder<OWNER, HatItem, OWNER> hatItem(OWNER owner, ResourceLocation hatName, ResourceLocation parentItemModelName, String textureName)
	{
		return hatItem(owner, hatName, HatItem::new, parentItemModelName, textureName);
	}

	@Nullable
	public static CompiledHat getHat(ResourceLocation hatName)
	{
		return hatRegistry.get(hatName);
	}

	public static HatBuilder builder(ResourceLocation hatName, NonnullSupplier<NonnullFunction<HatModel, ModelRenderer>> hatModelFactory)
	{
		return new HatBuilder(hatName, hatModelFactory);
	}

	public static HatBuilder builder(String hatNamespace, String hatName, NonnullSupplier<NonnullFunction<HatModel, ModelRenderer>> hatModelFactory)
	{
		return builder(new ResourceLocation(hatNamespace, hatName), hatModelFactory);
	}

	private static void registerHat(CompiledHat hat)
	{
		if(hatRegistry.containsKey(hat.hatName))
			throw new IllegalStateException("Attempt to register hat with duplicate registry name: " + hat.hatName);

		hatRegistry.put(hat.hatName, hat);
	}

	public static final class CompiledHat implements Comparable<CompiledHat>
	{
		private final ResourceLocation hatName;
		private final List<ResourceLocation> hatTextures = Lists.newLinkedList();
		private final ResourceLocation defaultHatTexture;
		private final NonnullSupplier<NonnullFunction<HatModel, ModelRenderer>> hatModelFactory;
		private final int textureWidth;
		private final int textureHeight;
		private final NonnullSupplier<OnSetupAnimation> animationSetup;

		private CompiledHat(HatBuilder builder)
		{
			hatName = builder.hatName;
			hatModelFactory = builder.hatModelFactory;
			textureWidth = builder.textureWidth;
			textureHeight = builder.textureHeight;
			animationSetup = builder.animationSetup;

			builder.hatTextures.stream().filter(Objects::nonNull).forEach(hatTextures::add);

			if(builder.defaultHatTexture == null)
				defaultHatTexture = hatTextures.get(0);
			else
			{
				if(!builder.hatTextures.contains(builder.defaultHatTexture))
					hatTextures.add(builder.defaultHatTexture);

				defaultHatTexture = builder.defaultHatTexture;
			}

			Validate.notNull(hatName, "Hat name must not be null");
			Validate.notNull(hatModelFactory, "Hat model factory must not be null");
			Validate.notNull(defaultHatTexture, "Default hat texture must not be null");
			Validate.isTrue(textureWidth > 0, "Hat texture width must be positive");
			Validate.isTrue(textureHeight > 0, "Hat texture height must be positive");
			Validate.notNull(animationSetup, "Animation setup must not be null");
		}

		@OnlyIn(Dist.CLIENT)
		public NonnullSupplier<NonnullFunction<HatModel, ModelRenderer>> getHatModelFactory()
		{
			return hatModelFactory;
		}

		@OnlyIn(Dist.CLIENT)
		public NonnullSupplier<OnSetupAnimation> getAnimationSetup()
		{
			return animationSetup;
		}

		public ResourceLocation getHatName()
		{
			return hatName;
		}

		public ResourceLocation getDefaultHatTexture()
		{
			return defaultHatTexture;
		}

		public List<ResourceLocation> getHatTextures()
		{
			return hatTextures;
		}

		public int getTextureWidth()
		{
			return textureWidth;
		}

		public int getTextureHeight()
		{
			return textureHeight;
		}

		public boolean matches(CompiledHat other)
		{
			return hatName.equals(other.hatName);
		}

		@Override
		public int compareTo(CompiledHat other)
		{
			return hatName.compareNamespaced(other.hatName);
		}

		@Override
		public boolean equals(Object obj)
		{
			if(this == obj)
				return true;
			if(obj instanceof CompiledHat)
				return matches((CompiledHat) obj);
			return false;
		}

		@Override
		public int hashCode()
		{
			return hatName.hashCode();
		}

		@Override
		public String toString()
		{
			return "Hat['" + hatName + "']";
		}
	}

	public static final class HatBuilder
	{
		private final ResourceLocation hatName;
		private final Set<ResourceLocation> hatTextures = Sets.newLinkedHashSet();
		@Nullable private ResourceLocation defaultHatTexture = null;
		private final NonnullSupplier<NonnullFunction<HatModel, ModelRenderer>> hatModelFactory;
		private int textureWidth = 64;
		private int textureHeight = 64;
		private NonnullSupplier<OnSetupAnimation> animationSetup = () -> (hatModel, player, playerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch) -> { };

		private HatBuilder(ResourceLocation hatName, NonnullSupplier<NonnullFunction<HatModel, ModelRenderer>> hatModelFactory)
		{
			this.hatName = hatName;
			this.hatModelFactory = hatModelFactory;
		}

		public HatBuilder withAnimationSetup(NonnullSupplier<OnSetupAnimation> animationSetup)
		{
			this.animationSetup = animationSetup;
			return this;
		}

		public HatBuilder withTextureSize(int textureWidth, int textureHeight)
		{
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			return this;
		}

		public HatBuilder withTexture(ResourceLocation hatTexture)
		{
			if(defaultHatTexture == null)
				defaultHatTexture = hatTexture;

			hatTextures.add(hatTexture);
			return this;
		}

		public HatBuilder withTextures(ResourceLocation... hatTextures)
		{
			if(defaultHatTexture == null && hatTextures.length != 0)
				defaultHatTexture = Arrays.stream(hatTextures).filter(Objects::nonNull).findFirst().orElse(null);

			Collections.addAll(this.hatTextures, hatTextures);
			return this;
		}

		public HatBuilder withTextures(Collection<ResourceLocation> hatTextures)
		{
			if(defaultHatTexture == null)
				defaultHatTexture = hatTextures.stream().filter(Objects::nonNull).findFirst().orElse(null);

			this.hatTextures.addAll(hatTextures);
			return this;
		}

		public HatBuilder withDefaultTexture(ResourceLocation defaultHatTexture)
		{
			this.defaultHatTexture = defaultHatTexture;
			return this;
		}

		public void register()
		{
			registerHat(new CompiledHat(this));
		}
	}

	@OnlyIn(Dist.CLIENT)
	@FunctionalInterface
	public interface OnSetupAnimation
	{
		void setupAnim(HatModel hatModel, AbstractClientPlayerEntity player, PlayerModel<AbstractClientPlayerEntity> playerModel, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
	}
}
