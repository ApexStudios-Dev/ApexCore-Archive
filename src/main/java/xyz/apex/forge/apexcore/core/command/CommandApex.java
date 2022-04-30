package xyz.apex.forge.apexcore.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import java.util.Optional;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public final class CommandApex
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
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

	private static boolean requiresOp(CommandSource src)
	{
		return src.hasPermission(2);
	}

	private static int onReloadSupporters(CommandContext<CommandSource> ctx) throws CommandSyntaxException
	{
		CommandSource src = ctx.getSource();
		src.sendSuccess(new StringTextComponent("Reloading ")
						.withStyle(style -> style
								.withColor(TextFormatting.GREEN)
						)
						.append(new StringTextComponent("ApexCore")
								.withStyle(style -> style
										.withBold(true)
										.withColor(TextFormatting.AQUA)
								)
						).append(new StringTextComponent(" Supporters..")
								.withStyle(style -> style
										.withColor(TextFormatting.GREEN)
								)
						), true
		);

		SupporterManager.loadSupporters();

		src.sendSuccess(new StringTextComponent("Reloaded ")
						.withStyle(style -> style
								.withColor(TextFormatting.GREEN)
						)
						.append(new StringTextComponent("ApexCore")
								.withStyle(style -> style
										.withBold(true)
										.withColor(TextFormatting.AQUA)
								)
						).append(new StringTextComponent(" Supporters!")
								.withStyle(style -> style
										.withColor(TextFormatting.GREEN)
								)
						), true
		);

		return SINGLE_SUCCESS;
	}

	private static int onAreTheySupporting(CommandContext<CommandSource> ctx) throws CommandSyntaxException
	{
		ServerPlayerEntity player = EntityArgument.getPlayer(ctx, "player");
		CommandSource src = ctx.getSource();
		Entity entity = src.getEntity();

		if(entity instanceof PlayerEntity && areSamePlayers(player, (PlayerEntity) entity))
			return onAmISupporting(ctx.copyFor(src.withEntity(player)));
		else
		{
			src.sendSuccess(buildSupporterComponent(player, false), true);
			return SINGLE_SUCCESS;
		}
	}

	private static int onAmISupporting(CommandContext<CommandSource> ctx) throws CommandSyntaxException
	{
		CommandSource src = ctx.getSource();
		ServerPlayerEntity player = src.getPlayerOrException();
		src.sendSuccess(buildSupporterComponent(player, true), true);
		return SINGLE_SUCCESS;
	}

	private static ITextComponent buildSupporterComponent(ServerPlayerEntity player, boolean you)
	{
		Optional<SupporterManager.SupporterInfo> infoOpt = SupporterManager.findSupporterInfo(player);

		IFormattableTextComponent playerName = (you ? new StringTextComponent("You") : new StringTextComponent(player.getDisplayName().getString()))
				.withStyle(style -> style
						.withColor(TextFormatting.GREEN)
						.withBold(true)
		);

		IFormattableTextComponent apexStudios = new StringTextComponent("ApexStudios")
				.withStyle(style -> style
						.withColor(TextFormatting.AQUA)
						.withItalic(true)
						.withBold(true)
				);

		IFormattableTextComponent supporting;

		if(infoOpt.isPresent())
		{
			SupporterManager.SupporterInfo info = infoOpt.get();
			supporting = you ? new StringTextComponent(" are Supporting ") : new StringTextComponent(" is Supporting ");

			IFormattableTextComponent level = new StringTextComponent(" (")
					.withStyle(style -> style
							.withColor(TextFormatting.GREEN)
							.withBold(true)
					)
					.append(new StringTextComponent(info.getLevel().getSerializedName())
							.withStyle(style -> style
									.withColor(TextFormatting.DARK_AQUA)
									.withItalic(true)
									.withBold(true)
							)
					)
					.append(new StringTextComponent(")"));

			apexStudios.append(level);
		}
		else
		{
			supporting = (you ? new StringTextComponent(" are Not Supporting ") : new StringTextComponent(" is Not Supporting "))
					.withStyle(style -> style
							.withColor(TextFormatting.RED)
					);
		}

		return playerName.append(supporting).append(apexStudios);
	}

	private static boolean areSamePlayers(PlayerEntity left, PlayerEntity right)
	{
		return left.getGameProfile().getId().equals(right.getGameProfile().getId());
	}
}
