package xyz.apex.forge.apexcore.lib.event.client;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;

import xyz.apex.forge.apexcore.core.client.BlockVisualizer;
import xyz.apex.forge.commonality.SideOnly;

@SideOnly(SideOnly.Side.CLIENT)
public class BlockVisualizerEvent extends Event
{
	private final BlockVisualizer.Context context;

	protected BlockVisualizerEvent(BlockVisualizer.Context context)
	{
		this.context = context;
	}

	public final BlockVisualizer.Context getContext()
	{
		return context;
	}

	public static class Render extends BlockVisualizerEvent
	{
		public final Stage stage;

		protected Render(BlockVisualizer.Context context, Stage stage)
		{
			super(context);

			this.stage = stage;
		}

		public enum Stage
		{
			PRE,
			POST
		}

		public static final class Pre extends Render
		{
			public Pre(BlockVisualizer.Context context)
			{
				super(context, Stage.PRE);
			}
		}

		public static final class Post extends Render
		{
			public Post(BlockVisualizer.Context context)
			{
				super(context, Stage.POST);
			}
		}
	}

	public static class ModifyBlockState extends BlockVisualizerEvent
	{
		public final Reason reason;
		private BlockState blockState;

		public ModifyBlockState(BlockVisualizer.Context context, Reason reason)
		{
			super(context);

			this.reason = reason;
			blockState = context.blockState();
		}

		public BlockState getBlockState()
		{
			return blockState;
		}

		public void setBlockState(BlockState blockState)
		{
			this.blockState = blockState;
		}

		public enum Reason
		{
			DEFAULT_BLOCKSTATE,
			EXISTING_BLOCKSTATE
		}
	}
}