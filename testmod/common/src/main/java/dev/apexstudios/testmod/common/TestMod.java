package dev.apexstudios.testmod.common;

import com.mojang.serialization.MapCodec;
import dev.apexstudios.apexcore.common.inventory.BlockEntityItemHandlerProvider;
import dev.apexstudios.apexcore.common.inventory.ItemHandler;
import dev.apexstudios.apexcore.common.inventory.SimpleItemHandler;
import dev.apexstudios.apexcore.common.menu.BaseMenu;
import dev.apexstudios.apexcore.common.menu.BaseMenuScreen;
import dev.apexstudios.apexcore.common.menu.SlotManager;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.network.Packet;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlock;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlockEntityType;
import dev.apexstudios.apexcore.common.registry.holder.DeferredEntityType;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

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
    ResourceKey<CreativeModeTab> TEST_TAB = REGISTER.defaultCreativeModeTab("test_tab", builder -> builder.icon(() -> new ItemStack(Items.DIAMOND)));

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
            .defaultBlockEntity(TestBlockEntity::new)
    .register();

    DeferredBlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY = DeferredBlockEntityType.createBlockEntityType(BLOCK_WITH_ENTITY.registryName());
    DeferredHolder<MenuType<?>, MenuType<TestMenu>> TEST_MENU_TYPE = REGISTER.<TestMenu, BaseMenuScreen<TestMenu>>menu("test_menu", TestMenu::forNetwork, () -> () -> BaseMenuScreen::new);

    GameRules.Key<GameRules.BooleanValue> TEST_GAMERULE = GameRules.register("test", GameRules.Category.MISC, GameRules.BooleanValue.create(false));

    default void bootstrap()
    {
        REGISTER.register();
    }

    final class BlockWithEntity extends BaseEntityBlock
    {
        private static final MapCodec<BlockWithEntity> CODEC = simpleCodec(BlockWithEntity::new);

        private int counter = 0;

        private BlockWithEntity(Properties properties)
        {
            super(properties);
        }

        @Override
        protected MapCodec<? extends BaseEntityBlock> codec()
        {
            return CODEC;
        }

        @Override
        public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
        {
            if(stack.hasCustomHoverName())
                TEST_BLOCK_ENTITY.getOptional(level, pos).ifPresent(blockEntity -> blockEntity.setCustomName(stack.getHoverName()));
        }

        @Override
        public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean movedByPiston)
        {
            if(!blockState.is(newBlockState.getBlock()))
            {
                TEST_BLOCK_ENTITY.getOptional(level, pos).ifPresent(blockEntity -> {
                    blockEntity.oakInventory.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
                    blockEntity.birchInventory.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
                    level.updateNeighbourForOutputSignal(pos, this);
                });

                super.onRemove(blockState, level, pos, newBlockState, movedByPiston);
            }
        }

        // TODO
        /*@Override
        public boolean hasAnalogOutputSignal(BlockState state)
        {
            return super.hasAnalogOutputSignal(state);
        }

        @Override
        public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
        {
            return AbstractContainerMenu.getRedstoneSignalFromContainer();
        }*/

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

                var blockEntity = TEST_BLOCK_ENTITY.getOptional(level, pos).orElseThrow();
                MenuHelper.openMenu(server, blockEntity, blockEntity);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        @Nullable
        @Override
        public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
        {
            return TEST_BLOCK_ENTITY.getOptional(level, pos).map(blockEntity -> MenuHelper.wrapMenuProvider(blockEntity, blockEntity)).getRaw();
        }
    }

    final class TestMenu extends BaseMenu
    {
        private TestMenu(int containerId, Inventory inventory, ItemHandler oakItems, ItemHandler birchItems)
        {
            super(TEST_MENU_TYPE.value(), containerId, 176, 166);

            slotManager.addPlayerSlots(inventory);
            slotManager.addSlot(oakItems, "oak", 20, 20, 0);
            slotManager.addSlots(birchItems, "birch", 40, 20, 2, 2);
            slotManager.addGhostSlot(80, 20).ghosting(Items.DIAMOND);
            slotManager.addGhostSlot(80, 40).ghosting(Items.NETHER_STAR);
            slotManager.addShiftTarget(SlotManager.GROUP_PLAYER, "oak", true);
            slotManager.addShiftTarget("birch", "oak", false);
        }

        private static TestMenu forNetwork(int windowId, Inventory inventory, FriendlyByteBuf buffer)
        {
            var globalPos = buffer.readGlobalPos();
            var dimension = globalPos.dimension();
            var plrLevel = inventory.player.level();
            var level = plrLevel.dimension() == dimension ? plrLevel : inventory.player.getServer().getLevel(globalPos.dimension());
            var blockEntity = TEST_BLOCK_ENTITY.getOptional(level, globalPos.pos()).orElseThrow();
            return new TestMenu(windowId, inventory, blockEntity.getItemHandler(), blockEntity.getItemHandler(Direction.NORTH));
        }
    }

    final class TestBlockEntity extends BlockEntity implements BlockEntityItemHandlerProvider, MenuProvider, Consumer<FriendlyByteBuf>, Nameable
    {
        private static final String NBT_OAK = "Oak";
        private static final String NBT_BIRCH = "Birch";
        private static final String NBT_CUSTOM_NAME = "CustomName";

        private ItemHandler oakInventory = new SimpleItemHandler(1);
        private ItemHandler birchInventory = new SimpleItemHandler(4);
        @Nullable private Component customName;

        private TestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
        {
            super(type, pos, blockState);
        }

        @Override
        public void load(CompoundTag tag)
        {
            if(tag.contains(NBT_OAK, Tag.TAG_COMPOUND))
            {
                var oakTag = tag.getCompound(NBT_OAK);
                oakInventory = new SimpleItemHandler(1, oakTag);
            }

            if(tag.contains(NBT_BIRCH, Tag.TAG_COMPOUND))
            {
                var oakTag = tag.getCompound(NBT_BIRCH);
                birchInventory = new SimpleItemHandler(4, oakTag);
            }

            if(tag.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING))
                customName = Component.Serializer.fromJsonLenient(tag.getString(NBT_CUSTOM_NAME));
        }

        @Override
        protected void saveAdditional(CompoundTag tag)
        {
            tag.put(NBT_OAK, oakInventory.serialize());
            tag.put(NBT_BIRCH, birchInventory.serialize());

            if(customName != null)
                tag.putString(NBT_CUSTOM_NAME, Component.Serializer.toJson(customName));
        }

        @Override
        public ItemHandler getItemHandler(@Nullable Direction side)
        {
            return side == null ? oakInventory : birchInventory;
        }

        @Override
        public ItemHandler getItemHandler()
        {
            return oakInventory;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
        {
            return new TestMenu(windowId, inventory, oakInventory, birchInventory);
        }

        @Override
        public void accept(FriendlyByteBuf buffer)
        {
            buffer.writeGlobalPos(GlobalPos.of(level.dimension(), worldPosition));
        }

        public void setCustomName(Component customName)
        {
            this.customName = customName;
        }

        @Nullable
        @Override
        public Component getCustomName()
        {
            return customName;
        }

        @Override
        public Component getName()
        {
            return getBlockState().getBlock().getName();
        }

        @Override
        public Component getDisplayName()
        {
            return customName == null ? getName() : customName;
        }
    }
}
