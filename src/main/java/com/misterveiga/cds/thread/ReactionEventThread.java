package com.misterveiga.cds.thread;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.misterveiga.cds.utils.Properties;
import com.misterveiga.cds.utils.RoleUtils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class ReactionEventThread implements Runnable {

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(ReactionEventThread.class);

	/** The Constant ID_REACTION_QM_30. */
	private static final String ID_REACTION_QM_30 = "760204798984454175"; // 30 minute quick-mute emoji

	/** The Constant ID_REACTION_QM_60. */
	private static final String ID_REACTION_QM_60 = "452813334429827072"; // 60 minute quick-mute emoji

	/** The Constant ID_REACTION_APPROVE_BAN_REQUEST. */
	private static final String ID_REACTION_APPROVE_BAN_REQUEST = "762388343253106688"; // Ban request approval emoji

	/** The Constant COMMAND_MUTE_USER_DEFAULT. */
	private static final String COMMAND_MUTE_USER_DEFAULT = ";mute %s %s %s";

	/** The Constant COMMAND_BAN_USER_DEFAULT. */
	private static final String COMMAND_BAN_USER_DEFAULT = ";ban %s %s";

	/** The Constant COMMAND_FORCEBAN_USER_DEFAULT. */
	private static final String COMMAND_FORCEBAN_USER_DEFAULT = ";forceban %s %s";

	/** The Constant COMMAND_CLEAN_MESSAGES_USER. */
	private static final String COMMAND_CLEAN_MESSAGES_USER = ";clean user %s";

	/** The Constant COMMAND_REASON. */
	private static final String COMMAND_REASON = "(By %s (%s)) Message Evidence: %s";

	private final MessageReactionAddEvent event;

	public ReactionEventThread(final MessageReactionAddEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		final Long startTime = System.currentTimeMillis();

		final TextChannel commandChannel = this.event.getGuild().getTextChannelById(Properties.CHANNEL_COMMANDS_ID);
		final MessageReaction reaction = this.event.getReaction();
		final Member reactee = this.event.getMember();
		final MessageChannel channel = this.event.getTextChannel();
		final ReactionEmote emote = reaction.getReactionEmote();
		final Message message = this.event.retrieveMessage().complete();
		final Member messageAuthor = message.getMember();

		final String emoteId = emote.isEmote() ? emote.getId() : "";

		if (!RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
				RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {
			return; // Do nothing.
		}

		switch (emoteId) {

		case ID_REACTION_QM_30:

			if (RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_COMMUNITY_SUPERVISOR)) {

				if (RoleUtils.isAnyRole(messageAuthor, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
						RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {
					commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention())
							.append(" you cannot run commands on server staff.")).queue();
					return; // Do nothing.
				}

				muteUser(reactee, messageAuthor, "30m", message, commandChannel);
				clearMessages(messageAuthor, channel);

				log.info("[Reaction Command] 30m Quick-Mute executed by {} on {} for Message\"{}\"",
						reactee.getUser().getAsTag(), messageAuthor.getUser().getAsTag(), message.getContentRaw());

			}

			break;

		case ID_REACTION_QM_60:

			if (RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_COMMUNITY_SUPERVISOR)) {

				if (RoleUtils.isAnyRole(messageAuthor, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
						RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {
					commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention())
							.append(" you cannot run commands on server staff.")).queue();
					return; // Do nothing.
				}

				muteUser(reactee, messageAuthor, "1h", message, commandChannel);
				clearMessages(messageAuthor, channel);

				log.info("[Reaction Command] 1h Quick-Mute executed by {} on {} for Message\"{}\"",
						reactee.getUser().getAsTag(), messageAuthor.getUser().getAsTag(), message.getContentRaw());

			}

			break;

		case ID_REACTION_APPROVE_BAN_REQUEST:

			if (event.getChannel().getIdLong() == Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID && RoleUtils.isAnyRole(
					event.getMember(), RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {

				banUser(reactee, message, commandChannel);

				log.info("[Reaction Command] Ban request approved by {} ({}) (request: {})", reactee.getEffectiveName(),
						reactee.getId(), message.getJumpUrl());

			}

			break;

		default:
			return; // Do nothing.

		}

		log.info("Finished reaction thread in {}ms", String.valueOf(System.currentTimeMillis() - startTime));

	}

	/**
	 * Ban user.
	 *
	 * @param message        the message
	 * @param commandChannel the command channel
	 */
	private void banUser(final Member reactee, final Message message, final TextChannel commandChannel) {
		try {

			final String[] banRequestMessageContent = message.getContentStripped().split(" ");
			final StringBuilder sb = new StringBuilder();
			sb.append("(approved by ").append(reactee.getUser().getAsTag()).append(" (").append(reactee.getId())
					.append(")) ");
			for (Integer i = 2; i < banRequestMessageContent.length; i++) {
				sb.append(banRequestMessageContent[i]).append(" ");
			}

			final String evidence = sb.toString();
			final String userToBan = banRequestMessageContent[1];

			if (banRequestMessageContent[0].equalsIgnoreCase(";ban")) {
				commandChannel.sendMessage(String.format(COMMAND_BAN_USER_DEFAULT, userToBan, evidence)).queue();
			} else if (banRequestMessageContent[0].equalsIgnoreCase(";forceban")) {
				commandChannel.sendMessage(String.format(COMMAND_FORCEBAN_USER_DEFAULT, userToBan, evidence)).queue();
			} else {
				commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention()).append(
						" the ban you tried to invoke was not correctly formatted. Please run the command manually."))
						.queue();
			}

		} catch (final IndexOutOfBoundsException e) {

			commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention()).append(
					" the ban you tried to invoke was not correctly formatted. Please run the command manually."))
					.queue();

		}
	}

	/**
	 * Mute user.
	 *
	 * @param reactee        the reactee
	 * @param messageAuthor  the message author
	 * @param muteDuration   the mute duration
	 * @param message        the message
	 * @param commandChannel the command channel
	 */
	private void muteUser(final Member reactee, final Member messageAuthor, final String muteDuration,
			final Message message, final TextChannel commandChannel) {

		final String messageContent = message.getContentStripped();

		if (messageContent.replace("\n", " ").length() < 120) {
			commandChannel
					.sendMessage(String.format(COMMAND_MUTE_USER_DEFAULT, messageAuthor.getId(), muteDuration,
							String.format(COMMAND_REASON, reactee.getUser().getAsTag(), reactee.getId(),
									messageContent.replace("\n", " "))))
					.allowedMentions(new ArrayList<MentionType>()).queue();
		} else {
			final String attachmentTitle = new StringBuilder().append("Evidence against ")
					.append(messageAuthor.getEffectiveName()).append(" (").append(messageAuthor.getId()).append(") on ")
					.append(Instant.now().toString()).toString();

			commandChannel
					.sendMessage(String.format(COMMAND_MUTE_USER_DEFAULT, messageAuthor.getId(), muteDuration,
							String.format(COMMAND_REASON, reactee.getUser().getAsTag(), reactee.getId(),
									messageContent.replace("\n", " ").substring(0, 17) + "... Full evidence: "
											+ commandChannel
													.sendFile(messageContent.getBytes(), attachmentTitle + ".txt")
													.complete().getAttachments().get(0).getUrl())))
					.allowedMentions(new ArrayList<MentionType>()).queue();
		}

	}

	/**
	 * Clear messages.
	 *
	 * @param messageAuthor the message author
	 * @param channel       the channel
	 */

	private void clearMessages(final Member messageAuthor, final MessageChannel channel) {
		channel.sendMessage(String.format(COMMAND_CLEAN_MESSAGES_USER, messageAuthor.getId()))
				.queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));
	}

}
