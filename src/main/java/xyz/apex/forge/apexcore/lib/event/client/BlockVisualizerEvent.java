package xyz.apex.forge.apexcore.lib.event.client;

import net.minecraftforge.eventbus.api.Event;

import xyz.apex.forge.apexcore.core.client.BlockVisualizer;

public class BlockVisualizerEvent extends Event
{
	private BlockVisualizer.Context context;

	protected BlockVisualizerEvent(BlockVisualizer.Context context)
	{
		this.context = context;
	}

	public final BlockVisualizer.Context getContext()
	{
		return context;
	}

	protected void setContext(BlockVisualizer.Context context)
	{
		this.context = context;
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

	public static class ModifyContext extends BlockVisualizerEvent
	{
		public final Reason reason;

		public ModifyContext(BlockVisualizer.Context context, Reason reason)
		{
			super(context);

			this.reason = reason;
		}

		@Override
		public void setContext(BlockVisualizer.Context context)
		{
			super.setContext(context);
		}

		public enum Reason
		{
			DEFAULT_BLOCKSTATE,
			EXISTING_BLOCKSTATE
		}
	}
}