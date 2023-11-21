package dev.apexstudios.testmod.common;

import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.network.Packet;
import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlock;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlockEntityType;
import dev.apexstudios.apexcore.common.registry.holder.DeferredEntityType;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public interface TestMod
{
    String ID = "testmod";
    Logger LOGGER = LogManager.getLogger();
    Register REGISTER = Register.create(ID);
    NetworkManager NETWORK = NetworkManager.get(ID);
    Packet.C2S<Boolean> TEST_PACKET_C2S = NETWORK.registerC2S("test_c2s", (flag, buffer) -> buffer.writeBoolean(flag), FriendlyByteBuf::readBoolean, (sender, $) -> sender.displayClientMessage(Component.literal("Received reply from client"), true));
    Packet.S2C<Integer> TEST_PACKET_S2C = NETWORK.registerS2C("test_s2c", (counter, buffer) -> buffer.writeVarInt(counter), FriendlyByteBuf::readVarInt, counter -> {
        Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Client Counter: %s".formatted(counter)), false);
        TEST_PACKET_C2S.send(true);
    });

    DeferredItem<Item> TEST_ITEM = REGISTER.object("test_item").item().color(() -> () -> (stack, tintIndex) -> -1).register();
    DeferredBlock<Block> TEST_BLOCK = REGISTER.object("test_block").block().color(() -> () -> (blockState, level, pos, tintIndex) -> -1).defaultItem().register();

    DeferredEntityType<Pig> TEST_ENTITY = REGISTER
            .object("test_entity")
            .entity(MobCategory.CREATURE, Pig::new)
            .properties(builder -> builder
                    .sized(.9F, .9F)
                    .clientTrackingRange(10)
            )
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND)
            // .spawnHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES) // default
            .spawnPredicate(Animal::checkAnimalSpawnRules)
            .attributes(() -> Pig.createAttributes().add(Attributes.MOVEMENT_SPEED, .75D))
            .renderer(() -> () -> PigRenderer::new)
            .defaultSpawnEgg(0x00FF00, 0xFF0000)
    .register();

    DeferredItem<DeferredSpawnEggItem> TEST_ENTITY_SPAWN_EGG = DeferredItem.createItem(TEST_ENTITY.registryName());

    DeferredBlock<BlockWithEntity> BLOCK_WITH_ENTITY = REGISTER
            .object("block_with_entity")
            .block(BlockWithEntity::new)
            .defaultItem()
            // why are you abstract?
            .defaultBlockEntity((blockEntityType, pos, blockState) -> new BlockEntity(blockEntityType, pos, blockState) { })
    .register();

    DeferredBlockEntityType<BlockEntity> TEST_BLOCK_ENTITY = DeferredBlockEntityType.createBlockEntityType(BLOCK_WITH_ENTITY.registryName());

    GameRules.Key<GameRules.BooleanValue> TEST_GAMERULE = GameRules.register("test", GameRules.Category.MISC, GameRules.BooleanValue.create(false));

    default void bootstrap()
    {
        REGISTER.register();
    }

    final class BlockWithEntity extends BaseEntityBlock
    {
        private int counter = 0;

        private BlockWithEntity(Properties properties)
        {
            super(properties);
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
        {
            return TEST_BLOCK_ENTITY.create(pos, blockState);
        }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
        {
            if(player instanceof ServerPlayer server)
            {
                var count = counter;
                player.displayClientMessage(Component.literal("Server Counter: %s".formatted(count)), false);
                TEST_PACKET_S2C.sendTo(count, server);
                counter++;
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
