package xyz.apex.forge.utility.registrator.provider;

import com.google.common.collect.Maps;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import org.apache.commons.lang3.Validate;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.LogicalSide;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;
import xyz.apex.java.utility.nullness.NonnullType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public final class RegistrateLangExtProvider implements RegistrateProvider
{
	// region: Language Keys (Codes)
	// Pulled right from Minecraft, list of all vanilla supported languages
	// use these a `languageKey` (1st parameter) when using the `#lang()` methods
	public static final String DE_AT = "de_at";
	public static final String KSH = "ksh";
	public static final String RO_RO = "ro_ro";
	public static final String AF_ZA = "af_za";
	public static final String MT_MT = "mt_mt";
	public static final String PT_BR = "pt_br";
	public static final String ES_EC = "es_ec";
	public static final String CS_CZ = "cs_cz";
	public static final String LMO = "lmo";
	public static final String ES_UY = "es_uy";
	public static final String BAR = "bar";
	public static final String BRB = "brb";
	public static final String ES_MX = "es_mx";
	public static final String SK_SK = "sk_sk";
	public static final String ES_ES = "es_es";
	public static final String NL_BE = "nl_be";
	public static final String ES_VE = "es_ve";
	public static final String SQ_AL = "sq_al";
	public static final String SV_SE = "sv_se";
	public static final String DA_DK = "da_dk";
	public static final String EN_UD = "en_ud";
	public static final String FO_FO = "fo_fo";
	public static final String KO_KR = "ko_kr";
	public static final String EN_US = "en_us";
	public static final String EL_GR = "el_gr";
	public static final String IT_IT = "it_it";
	public static final String PL_PL = "pl_pl";
	public static final String BE_BY = "be_by";
	public static final String EN_AU = "en_au";
	public static final String TR_TR = "tr_tr";
	public static final String ID_ID = "id_id";
	public static final String QYA_AA = "qya_aa";
	public static final String HE_IL = "he_il";
	public static final String JA_JP = "ja_jp";
	public static final String SE_NO = "se_no";
	public static final String DE_DE = "de_de";
	public static final String ZH_HK = "zh_hk";
	public static final String DE_CH = "de_ch";
	public static final String EU_ES = "eu_es";
	public static final String OVD = "ovd";
	public static final String LI_LI = "li_li";
	public static final String OC_FR = "oc_fr";
	public static final String ES_CL = "es_cl";
	public static final String YO_NG = "yo_ng";
	public static final String LA_LA = "la_la";
	public static final String FR_CA = "fr_ca";
	public static final String FRA_DE = "fra_de";
	public static final String VAL_ES = "val_es";
	public static final String ENWS = "enws";
	public static final String AZ_AZ = "az_az";
	public static final String FI_FI = "fi_fi";
	public static final String ESAN = "esan";
	public static final String VI_VN = "vi_vn";
	public static final String EN_CA = "en_ca";
	public static final String KA_GE = "ka_ge";
	public static final String LV_LV = "lv_lv";
	public static final String BS_BA = "bs_ba";
	public static final String UK_UA = "uk_ua";
	public static final String GA_IE = "ga_ie";
	public static final String SL_SI = "sl_si";
	public static final String MI_NZ = "mi_nz";
	public static final String FY_NL = "fy_nl";
	public static final String PT_PT = "pt_pt";
	public static final String BR_FR = "br_fr";
	public static final String SWG = "swg";
	public static final String TL_PH = "tl_ph";
	public static final String TH_TH = "th_th";
	public static final String IG_NG = "ig_ng";
	public static final String ISV = "isv";
	public static final String CA_ES = "ca_es";
	public static final String HU_HU = "hu_hu";
	public static final String AR_SA = "ar_sa";
	public static final String KW_GB = "kw_gb";
	public static final String GL_ES = "gl_es";
	public static final String KN_IN = "kn_in";
	public static final String GV_IM = "gv_im";
	public static final String SO_SO = "so_so";
	public static final String EN_PT = "en_pt";
	public static final String IO_EN = "io_en";
	public static final String EO_UY = "eo_uy";
	public static final String AST_ES = "ast_es";
	public static final String LZH = "lzh";
	public static final String NL_NL = "nl_nl";
	public static final String IS_IS = "is_is";
	public static final String ES_AR = "es_ar";
	public static final String GD_GB = "gd_gb";
	public static final String TA_IN = "ta_in";
	public static final String VEC_IT = "vec_it";
	public static final String HR_HR = "hr_hr";
	public static final String SXU = "sxu";
	public static final String SR_SP = "sr_sp";
	public static final String KT_KT = "lt_lt";
	public static final String KK_KZ = "kk_kz";
	public static final String NO_NO = "no_no";
	public static final String RU_RU = "ru_ru";
	public static final String HI_IN = "hi_in";
	public static final String YI_DE = "yi_de";
	public static final String JBO_EN = "jbo_en";
	public static final String MN_MN = "mn_mn";
	public static final String FR_FR = "fr_fr";
	public static final String LOL_US = "lol_us";
	public static final String MS_MY = "ms_my";
	public static final String BAR_RU = "ba_ru";
	public static final String ZH_TW = "zh_tw";
	public static final String HAW_US = "haw_us";
	public static final String TLH_AA = "tlh_aa";
	public static final String EN_NZ = "en_nz";
	public static final String NN_NO = "nn_no";
	public static final String SZL = "szl";
	public static final String ENP = "enp";
	public static final String FIL_PH = "fil_ph";
	public static final String FA_IR = "fa_ir";
	public static final String NDS_DE = "nds_de";
	public static final String BG_BG = "bg_bg";
	public static final String HY_AM = "hy_am";
	public static final String LB_LU = "lb_lu";
	public static final String MK_MK = "mk_mk";
	public static final String CY_CB = "cy_gb";
	public static final String EN_GB = "en_gb";
	public static final String ET_EE = "et_ee";
	public static final String RPR = "rpr";
	public static final String TT_RU = "tt_ru";
	public static final String ZH_CN = "zh_cn";
	// endregion

	private final AbstractRegistrate<?> owner;
	private final DataGenerator generator;
	private final Map<String, TranslationMap> translationMap = Maps.newHashMap();

	public RegistrateLangExtProvider(AbstractRegistrate<?> owner, DataGenerator generator)
	{
		this.owner = owner;
		this.generator = generator;
	}

	@Override
	public LogicalSide getSide()
	{
		return LogicalSide.CLIENT;
	}

	@Override
	public void run(HashCache cache) throws IOException
	{
		owner.genData(AbstractRegistrator.LANG_EXT_PROVIDER, this);

		for(var translationMap : translationMap.values())
		{
			translationMap.run(cache);
		}
	}

	@Override
	public String getName()
	{
		return "Translations";
	}

	private TranslationMap translationMap(String languageKey)
	{
		Validate.isTrue(!languageKey.equals(EN_US), "English (en_us) is not supported when using the extended language provider (languageKey='%s')", languageKey);
		return translationMap.computeIfAbsent(languageKey, TranslationMap::new);
	}

	public void add(String languageKey, String translationKey, String localizedValue)
	{
		translationMap(languageKey).add(translationKey, localizedValue);
	}

	// region: Vanilla Wrappers
	public void add(String languageKey, Block block, String name)
	{
		translationMap(languageKey).add(block, name);
	}

	public void add(String languageKey, Item item, String localizedValue)
	{
		translationMap(languageKey).add(item, localizedValue);
	}

	public void add(String languageKey, ItemStack stack, String localizedValue)
	{
		translationMap(languageKey).add(stack, localizedValue);
	}

	public void add(String languageKey, Enchantment enchantment, String localizedValue)
	{
		translationMap(languageKey).add(enchantment, localizedValue);
	}

	public void add(String languageKey, MobEffect effect, String localizedValue)
	{
		translationMap(languageKey).add(effect, localizedValue);
	}

	public void add(String languageKey, EntityType<?> entityType, String localizedValue)
	{
		translationMap(languageKey).add(entityType, localizedValue);
	}

	public void addBlock(String languageKey, Supplier<? extends Block> block, String localizedValue)
	{
		translationMap(languageKey).addBlock(block, languageKey);
	}

	public void addItem(String languageKey, Supplier<? extends Item> item, String localizedValue)
	{
		translationMap(languageKey).addItem(item, localizedValue);
	}

	public void addItemStack(String languageKey, Supplier<ItemStack> stack, String localizedValue)
	{
		translationMap(languageKey).addItemStack(stack, localizedValue);
	}

	public void addEnchantment(String languageKey, Supplier<? extends Enchantment> enchantment, String localizedValue)
	{
		translationMap(languageKey).addEnchantment(enchantment, localizedValue);
	}

	public void addEffect(String languageKey, Supplier<? extends MobEffect> effect, String localizedValue)
	{
		translationMap(languageKey).addEffect(effect, localizedValue);
	}

	public void addEntityType(String languageKey, Supplier<? extends EntityType<?>> entityType, String localizedValue)
	{
		translationMap(languageKey).addEntityType(entityType, localizedValue);
	}
	// endregion

	// region: Registrate Wrappers
	public void addBlockWithTooltip(String languageKey, NonnullSupplier<? extends Block> block, String localizedValue, String localizedTooltip)
	{
		translationMap(languageKey).addBlockWithTooltip(block, localizedValue, localizedTooltip);
	}

	public void addItemWithTooltip(String languageKey, NonnullSupplier<? extends Item> item, String localizedValue, List<@NonnullType String> localizedTooltips)
	{
		translationMap(languageKey).addItemWithTooltip(item, localizedValue, localizedTooltips);
	}

	public void addTooltip(String languageKey, NonnullSupplier<? extends ItemLike> item, String localizedTooltip)
	{
		translationMap(languageKey).addTooltip(item, localizedTooltip);
	}

	public void addTooltip(String languageKey, NonnullSupplier<? extends ItemLike> item, List<@NonnullType String> localizedTooltips)
	{
		translationMap(languageKey).addTooltip(item, localizedTooltips);
	}

	public void add(String languageKey, CreativeModeTab itemGroup, String localizedValue)
	{
		translationMap(languageKey).add(itemGroup, localizedValue);
	}

	public void addItemGroup(String languageKey, NonnullSupplier<? extends CreativeModeTab> itemGroup, String localizedValue)
	{
		translationMap(languageKey).addItemGroup(itemGroup, localizedValue);
	}
	// endregion

	public class TranslationMap extends LanguageProvider
	{
		public TranslationMap(String languageKey)
		{
			super(generator, owner.getModid(), languageKey);
		}

		@Override
		protected void addTranslations()
		{
		}

		// region: Registrate Wrappers
		public void addBlockWithTooltip(NonnullSupplier<? extends Block> block, String localizedValue, String localizedTooltip)
		{
			addBlock(block, localizedValue);
			addTooltip(block, localizedTooltip);
		}

		public void addItemWithTooltip(NonnullSupplier<? extends Item> item, String localizedValue, List<@NonnullType String> localizedTooltips)
		{
			addItem(item, localizedValue);
			addTooltip(item, localizedTooltips);
		}

		public void addTooltip(NonnullSupplier<? extends ItemLike> item, String localizedTooltip)
		{
			add(item.get().asItem().getDescriptionId() + ".desc", localizedTooltip);
		}

		public void addTooltip(NonnullSupplier<? extends ItemLike> item, List<@NonnullType String> localizedTooltips)
		{
			String translationKey = item.get().asItem().getDescriptionId() + ".desc.";
			IntStream.range(0, localizedTooltips.size()).forEach(i -> add(translationKey + i, localizedTooltips.get(i)));
		}

		public void add(CreativeModeTab itemGroup, String localizedValue)
		{
			// String langId = ObfuscationReflectionHelper.getPrivateValue(ItemGroup.class, itemGroup, "field_78034_o");
			String langId = itemGroup.langId;
			add("itemGroup." + langId, localizedValue);
		}

		public void addItemGroup(NonnullSupplier<? extends CreativeModeTab> itemGroup, String localizedValue)
		{
			add(itemGroup.get(), localizedValue);
		}
		// endregion
	}
}
