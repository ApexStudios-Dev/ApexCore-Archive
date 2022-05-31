package xyz.apex.forge.apexcore.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public final class CommandApex
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(literal(ApexCore.ID)
				.then(literal("supporters")
						.then(literal("arethey")
								.then(argument("player", EntityArgument.player())
										.executes(CommandApex::onAreTheySupporting)
								)
						).then(literal("ami")
								.executes(CommandApex::onAmISupporting)
						).then(literal("reload")
								.requires(CommandApex::requiresOp)
								.executes(CommandApex::onReloadSupporters)
						)
				)
		);
	}

	private static boolean requiresOp(CommandSourceStack src)
	{
		return src.hasPermission(2);
	}

	private static int onReloadSupporters(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
	{
		var src = ctx.getSource();
		src.sendSuccess(new TextComponent("Reloading ")
						.withStyle(style -> style
								.withColor(ChatFormatting.GREEN)
						)
						.append(new TextComponent("ApexCore")
								.withStyle(style -> style
										.withBold(true)
										.withColor(ChatFormatting.AQUA)
								)
						).append(new TextComponent(" Supporters..")
								.withStyle(style -> style
										.withColor(ChatFormatting.GREEN)
								)
						), true
		);

		SupporterManager.loadSupporters();

		src.sendSuccess(new TextComponent("Reloaded ")
						.withStyle(style -> style
								.withColor(ChatFormatting.GREEN)
						)
						.append(new TextComponent("ApexCore")
								.withStyle(style -> style
										.withBold(true)
										.withColor(ChatFormatting.AQUA)
								)
						).append(new TextComponent(" Supporters!")
								.withStyle(style -> style
										.withColor(ChatFormatting.GREEN)
								)
						), true
		);

		return SINGLE_SUCCESS;
	}

	private static int onAreTheySupporting(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
	{
		var player = EntityArgument.getPlayer(ctx, "player");
		var src = ctx.getSource();
		var entity = src.getEntity();

		if(entity instanceof Player other && areSamePlayers(player, other))
			return onAmISupporting(ctx.copyFor(src.withEntity(player)));
		else
		{
			src.sendSuccess(buildSupporterComponent(player, false), true);
			return SINGLE_SUCCESS;
		}
	}

	private static int onAmISupporting(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
	{
		var src = ctx.getSource();
		var player = src.getPlayerOrException();
		src.sendSuccess(buildSupporterComponent(player, true), true);
		return SINGLE_SUCCESS;
	}

	private static Component buildSupporterComponent(ServerPlayer player, boolean you)
	{
		var infoOpt = SupporterManager.findSupporterInfo(player);

		var playerName = (you ? new TextComponent("You") : new TextComponent(player.getDisplayName().getString()))
				.withStyle(style -> style
						.withColor(ChatFormatting.GREEN)
						.withBold(true)
		);

		var apexStudios = new TextComponent("ApexStudios")
				.withStyle(style -> style
						.withColor(ChatFormatting.AQUA)
						.withItalic(true)
						.withBold(true)
				);

		Component supporting;

		if(infoOpt.isPresent())
		{
			SupporterManager.SupporterInfo info = infoOpt.get();
			supporting = you ? new TextComponent(" are Supporting ") : new TextComponent(" is Supporting ");

			var level = new TextComponent(" (")
					.withStyle(style -> style
							.withColor(ChatFormatting.GREEN)
							.withBold(true)
					)
					.append(new TextComponent(info.getLevel().getSerializedName())
							.withStyle(style -> style
									.withColor(ChatFormatting.DARK_AQUA)
									.withItalic(true)
									.withBold(true)
							)
					)
					.append(new TextComponent(")"));

			apexStudios.append(level);
		}
		else
		{
			supporting = (you ? new TextComponent(" are Not Supporting ") : new TextComponent(" is Not Supporting "))
					.withStyle(style -> style
							.withColor(ChatFormatting.RED)
					);
		}

		return playerName.append(supporting).append(apexStudios);
	}

	private static boolean areSamePlayers(Player left, Player right)
	{
		return left.getGameProfile().getId().equals(right.getGameProfile().getId());
	}
}
