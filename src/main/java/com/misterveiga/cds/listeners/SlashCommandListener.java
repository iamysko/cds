/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsDataImpl;
import com.misterveiga.cds.utils.EmbedBuilds;
import com.misterveiga.cds.utils.Properties;
import com.misterveiga.cds.utils.RoleUtils;
import com.misterveiga.cds.utils.SlashCommandConstants;
import com.misterveiga.cds.utils.StringBuilds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

/**
 * The listener interface for receiving message events. The class that is
 * interested in processing a message event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addMessageListener<code> method. When the message event
 * occurs, that object's appropriate method is invoked.
 *
 * @see MessageEvent
 */
@Component
public class SlashCommandListener extends ListenerAdapter {

	@Override
	public void onSlashCommand(SlashCommandEvent event) {

		boolean perm = false;

		if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER) != null) {
			perm = true;
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SENIOR_MODERATOR) != null) {
			perm = true;
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_MODERATOR) != null) {
			perm = true;
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_TRIAL_MODERATOR) != null) {
			perm = true;
		} else {
			perm = false;
		}

		if (perm) {

			final TextChannel commandChannel = event.getGuild().getTextChannelById(Properties.CHANNEL_COMMANDS_ID);
			final Member author = event.getMember();
			final String authorMention = author.getAsMention();

			if (event.getName().equals(SlashCommandConstants.COMMAND_HELP)) {
				event.reply(StringBuilds.getHelpMessage(authorMention)).queue();
			} else if (event.getName().equals(SlashCommandConstants.COMMAND_ABOUT)) {
				event.reply(StringBuilds.getAboutMessage(authorMention)).queue();
			} else if (event.getName().equals(SlashCommandConstants.COMMAND_USER_INFO)) {
				Member theMember = event.getGuild().getMemberById(event.getOption("user").getAsString());
				RestAction<User> userData = event.getJDA().retrieveUserById(event.getOption("user").getAsString());
				User theUser = userData.complete();
				EmbedBuilder embed = EmbedBuilds.getUserInfoEmbed(theMember, theUser);
				ReplyAction reply = event.replyEmbeds(embed.build());
				if (theMember != null && theMember.getNickname() != null
						&& RoleUtils.findRole(theMember, RoleUtils.ROLE_VERIFIED) != null) {
					reply.addActionRow(
							Button.danger("RobloxInformation/" + theMember.getNickname() + "/" + theMember.getId(),
									"Roblox Information"));
				}
				reply.queue();
			} else if (event.getName().equals(SlashCommandConstants.COMMAND_ROBLOX_USER_INFO)) {
				try {
					EmbedBuilder embed = EmbedBuilds.getRobloxUserInfoEmbed(event.getOption("username").getAsString(),
							null);
					event.replyEmbeds(embed.build()).queue();
				} catch (Exception e) {
					event.reply("It appears the Roblox API is currently not responding! Please Try again later! :(" + e)
							.queue();
				}
			} else if (event.getName().equals(SlashCommandConstants.COMMAND_SCAN_URL)) {
				event.deferReply().queue();
				event.reply("Command is currently disabled");
//				InteractionHook hook = event.getHook();
//				new Thread(() -> {
//					EmbedBuilder embed = null;
//					try {
//						embed = EmbedBuilds.scanUrl(event.getOption("url").getAsString(),
//								event.getJDA().getSelfUser().getEffectiveAvatarUrl());
//					} catch (InterruptedException e) {
//						embed = EmbedBuilds.ApiError();
//					}
//					hook.editOriginalEmbeds(embed.build()).queue();
//				}).start();
			} else {
				event.reply("The Command you tried to execute does not exist!").queue();
			}
		} else {
			event.reply("Missing permissions!").setEphemeral(true).queue();
		}
	}
}
