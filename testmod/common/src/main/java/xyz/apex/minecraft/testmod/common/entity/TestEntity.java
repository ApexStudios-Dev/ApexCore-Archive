package xyz.apex.minecraft.testmod.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class TestEntity extends Animal
{
    public TestEntity(EntityType<? extends TestEntity> entityType, Level level)
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

    public static AttributeSupplier.Builder attributes()
    {
        return Mob.createMobAttributes()
                  .add(Attributes.MAX_HEALTH, 10D)
                  .add(Attributes.MOVEMENT_SPEED, .5D);
    }
}
