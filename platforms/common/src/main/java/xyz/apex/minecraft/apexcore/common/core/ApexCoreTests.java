package xyz.apex.minecraft.apexcore.common.core;

import joptsimple.internal.Strings;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;

import java.util.stream.Stream;

@ApiStatus.Internal
public final class ApexCoreTests
{
    private static final String PROPERTY = "%s.test_elements.enabled".formatted(ApexCore.ID);
    public static final boolean ENABLED = BooleanUtils.toBoolean(System.getProperty(PROPERTY, "false"));
    public static final Marker MARKER = MarkerManager.getMarker("Tests");

    static void register()
    {
        if(!ENABLED)
            return;

        var messages = new String[] {
                "ApexCore Test Elements Enabled!",
                "Errors, Bugs and Glitches may arise, you are on your own.",
                "No support will be provided while Test Elements are Enabled!"
        };

        var maxLen = Stream.of(messages).mapToInt(String::length).max().orElse(1);
        var header = Strings.repeat('*', maxLen + 4);
        ApexCore.LOGGER.warn(MARKER, header);
        Stream.of(messages).map(str -> StringUtils.center(str, maxLen, ' ')).forEach(str -> ApexCore.LOGGER.warn(MARKER, "* {} *", str));
        ApexCore.LOGGER.warn(MARKER, header);

        var registrar = Registrar.create(ApexCore.ID);

        var testItem = registrar
                .object("test_item")
                .item()
                .defaultModel()
                .model((provider, lookup, entry) -> provider.generated(
                        entry.getRegistryName().withPrefix("item/"),
                        new ResourceLocation("item/diamond")
                ))
        .register();

        var testBlock = registrar
                .object("test_block")
                .block()
                .defaultBlockState((provider, lookup, entry) -> provider.withParent(
                        entry.getRegistryName().withPrefix("block/"),
                        "block/cube_all"
                ).texture("all", new ResourceLocation("block/debug")))
                .defaultItem()
        .register();

        var testEnchantment = registrar
                .object("test_enchantment")
                .enchantment(EnchantmentCategory.DIGGER)
                .equipmentSlots(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)
        .register();

        var testEntity = registrar
                .object("test_entity")
                .entityType(MobCategory.CREATURE, TestEntity::new)
                .attributes(TestEntity::attributes)
                .renderer(() -> () -> TestEntityRenderer::new)
                .defaultSpawnEgg(0xF2D7D5, 0xFFC300)
                .sized(.9F, .9F)
                .clientTrackingRange(10)
                .tag(EntityTypeTags.DISMOUNTS_UNDERWATER)
                .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules)
        .register();

        var creativeModeTab = registrar
                .creativeModeTab("test")
                .lang("en_us", "ApexCore - Test Elements")
                .icon(testBlock::asStack)
                .displayItems((parameters, output) -> {
                    output.accept(testItem);
                    output.accept(testBlock);
                    output.accept(testEnchantment.asStack(1));
                    output.accept(testEntity);
                })
        .register();

        registrar.register();
    }

    private static final class TestEntity extends Animal
    {
        private TestEntity(EntityType<? extends TestEntity> entityType, Level level)
        {
            super(entityType, level);
        }

        @Override
        protected void registerGoals()
        {
            super.registerGoals();

            goalSelector.addGoal(0, new FloatGoal(this));
            goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
            goalSelector.addGoal(3, new BreedGoal(this, 1D));
            goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(ItemTags.PIGLIN_LOVED), false));
            goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
            goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1D));
            goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6F));
            goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        }

        @Override
        protected SoundEvent getAmbientSound()
        {
            return SoundEvents.PIG_AMBIENT;
        }

        @Override
        protected SoundEvent getHurtSound(DamageSource source)
        {
            return SoundEvents.PIG_HURT;
        }

        @Override
        protected SoundEvent getDeathSound()
        {
            return SoundEvents.PIG_DEATH;
        }

        @Override
        protected void playStepSound(BlockPos pos, BlockState state)
        {
            playSound(SoundEvents.PIG_STEP, .15F, 1F);
        }

        @Nullable
        @Override
        public TestEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent)
        {
            return (TestEntity) getType().create(level);
        }

        @Override
        public boolean isFood(ItemStack stack)
        {
            return stack.is(ItemTags.PIGLIN_LOVED);
        }

        @Override
        protected Vec3 getLeashOffset()
        {
            return new Vec3(0D, .6F * getEyeHeight(), getBbWidth() * .4F);
        }

        private static AttributeSupplier.Builder attributes()
        {
            return Mob.createMobAttributes()
                      .add(Attributes.MAX_HEALTH, 10D)
                      .add(Attributes.MOVEMENT_SPEED, .5D);
        }
    }

    private static final class TestEntityRenderer extends MobRenderer<TestEntity, PigModel<TestEntity>>
    {
        private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig.png");

        public TestEntityRenderer(EntityRendererProvider.Context context)
        {
            super(context, new PigModel<>(context.bakeLayer(ModelLayers.PIG)), .7F);
        }

        @Override
        public ResourceLocation getTextureLocation(TestEntity entity)
        {
            return TEXTURE;
        }
    }
}
