/*
 * Author: {Ruben Veiga}
 * Contributor: {Liscuate}
 */

package com.misterveiga.cds.listeners;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsDataImpl;
import com.misterveiga.cds.entities.Action;
import com.misterveiga.cds.utils.Properties;
import com.misterveiga.cds.utils.RoleUtils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener interface for receiving reaction events. The class that is
 * interested in processing a reaction event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addReactionListener<code> method. When the reaction event
 * occurs, that object's appropriate method is invoked.
 *
 * @see ReactionEvent
 */
@Component
@PropertySource("classpath:application.properties")
public class ReactionListener extends ListenerAdapter {

	@Autowired
	public CdsDataImpl cdsData;

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(ReactionListener.class);

	/** The Constant ID_REACTION_QM_30. */
	private static final String ID_REACTION_QM_30 = "760204798984454175"; // 30 minute quick-mute emoji

	/** The Constant ID_REACTION_QM_60. */
	private static final String ID_REACTION_QM_60 = "452813334429827072"; // 60 minute quick-mute emoji

	/** The Constant ID_REACTION_APPROVE_BAN_REQUEST. */
	private static final String ID_REACTION_APPROVE_BAN_REQUEST = "762388343253106688"; // Ban request approval emoji

	/** The Constant ID_REACTION_REJECT_BAN_REQUEST. */
	private static final String ID_REACTION_REJECT_BAN_REQUEST = "764268551473070080"; // Ban request rejection emoji

	/** The Constant COMMAND_MUTE_USER_DEFAULT. */
	private static final String COMMAND_MUTE_USER_DEFAULT = ";mute %s %s %s";

	/** The Constant COMMAND_BAN_USER_DEFAULT. */
	private static final String COMMAND_BAN_USER_DEFAULT = ";ban %s %s";

	/** The Constant COMMAND_FORCEBAN_USER_DEFAULT. */
	private static final String COMMAND_FORCEBAN_USER_DEFAULT = ";forceban %s %s";

	/** The Constant COMMAND_REASON. */
	private static final String COMMAND_REASON = "(By %s (%s)) Message Evidence: %s";

	private static final String COMMAND_UNMUTE_USER_DEFAULT = ";unmute %s";

	/**
	 * On message reaction add.
	 *
	 * @param event the event
	 */
	@Override
	public void onMessageReactionAdd(final MessageReactionAddEvent event) {

		final TextChannel commandChannel = event.getGuild().getTextChannelById(Properties.CHANNEL_COMMANDS_ID);
		final MessageReaction reaction = event.getReaction();
		final Member reactee = event.getMember();
		final MessageChannel channel = event.getTextChannel();
		final ReactionEmote emote = reaction.getReactionEmote();

		final String emoteId = emote.isEmote() ? emote.getId() : "";

		event.retrieveMessage().queue(message -> {
			final Member messageAuthor = message.getMember();

			if (!RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
					RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {
				return; // Do nothing.
			}

			final Action commandAction = new Action();
			commandAction.setDate(Date.from(OffsetDateTime.now(ZoneOffset.UTC).toInstant()));
			commandAction.setUser(reactee.getUser().getAsTag());
			commandAction.setDiscordId(reactee.getIdLong());

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
					purgeMessagesInChannel(messageAuthor, channel);
					commandAction.setOffendingUser(messageAuthor.getUser().getAsTag());
					commandAction.setOffendingUserId(messageAuthor.getIdLong());
					commandAction.setActionType("REACTION_QM_30");
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
					purgeMessagesInChannel(messageAuthor, channel);
					commandAction.setOffendingUser(messageAuthor.getUser().getAsTag());
					commandAction.setOffendingUserId(messageAuthor.getIdLong());
					commandAction.setActionType("REACTION_QM_60");
					log.info("[Reaction Command] 1h Quick-Mute executed by {} on {} for Message\"{}\"",
							reactee.getUser().getAsTag(), messageAuthor.getUser().getAsTag(), message.getContentRaw());

				}

				break;

			case ID_REACTION_APPROVE_BAN_REQUEST:

				if (RoleUtils.isAnyRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER,
						RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {

					if (event.getChannel().getIdLong() == Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID) {

						approveBanRequest(reactee, message, commandChannel);

					} else if (event.getChannel().getIdLong() == Properties.CHANNEL_CENSORED_AND_SPAM_LOGS_ID) {

						approveCensoredBan(reactee, message, commandChannel);

					}

					commandAction.setActionType("REACTION_APPROVE_BAN_REQUEST");
					log.info("[Reaction Command] Ban request approved by {} ({}) (request: {})",
							reactee.getEffectiveName(), reactee.getId(), message.getJumpUrl());

				}

				break;

			case ID_REACTION_REJECT_BAN_REQUEST:

				if (event.getChannel().getIdLong() == Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID && RoleUtils.isAnyRole(
						event.getMember(), RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {

					rejectBanRequest(reactee, message, commandChannel);
					commandAction.setActionType("REACTION_REJECT_BAN_REQUEST");
					log.info("[Reaction Command] Ban request rejected by {} ({}) (request: {})",
							reactee.getEffectiveName(), reactee.getId(), message.getJumpUrl());

				}

				break;

			default:
				return; // Do nothing.

			}

			cdsData.insertAction(commandAction);

		});

	}

	private void approveCensoredBan(final Member reactee, final Message message, final TextChannel commandChannel) {

		try {

			final String rawMessage = message.getContentRaw();
			final String offenderId = rawMessage.substring(rawMessage.indexOf("(`") + 2, rawMessage.indexOf("`)"));
			final String offenseReason = rawMessage.split("```")[1];

			final StringBuilder sb = new StringBuilder();
			sb.append("(Censored message ban approved by ").append(reactee.getUser().getAsTag()).append(" (")
					.append(reactee.getId()).append(")) Evidence: ").append(offenseReason);
			final String evidence = sb.toString();

			commandChannel.sendMessage(String.format(COMMAND_BAN_USER_DEFAULT, offenderId, evidence))
					.allowedMentions(new ArrayList<MentionType>()).queue();

		} catch (final IndexOutOfBoundsException e) {

			commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention()).append(
					" an unknown error occurred with your censored log ban approval. Please run the command manually."))
					.queue();

		}

	}

	private void rejectBanRequest(final Member reactee, final Message message, final TextChannel commandChannel) {
		try {

			final String[] banRequestMessageContent = message.getContentStripped().split(" ");
			final String reportedUserId = banRequestMessageContent[1];
			final User reportedUser = commandChannel.getJDA().getUserById(reportedUserId);

			final StringBuilder sb = new StringBuilder();

			sb.append(message.getAuthor().getAsMention())
					.append(commandChannel.getJDA().getEmoteById(ID_REACTION_REJECT_BAN_REQUEST).getAsMention())
					.append(" Your ban request against ")
					.append(reportedUser == null ? reportedUserId : reportedUser.getAsMention()).append(" (")
					.append(reportedUser == null ? "who has left the server" : reportedUser.getId())
					.append(") has been rejected by ").append(reactee.getAsMention())
					.append(" and the user has been unmuted.\n\nYour ban request evidence: ");

			for (Integer i = 2; i < banRequestMessageContent.length; i++) {
				sb.append(banRequestMessageContent[i]).append(" ");
			}

			final String rejectionNoticeString = sb.toString();

			commandChannel.sendMessage(String.format(COMMAND_UNMUTE_USER_DEFAULT, reportedUserId)).queue();
			commandChannel.sendMessage(rejectionNoticeString).queue();

		} catch (final IndexOutOfBoundsException e) {

			commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention()).append(
					" the ban you tried to invoke was not correctly formatted. Please run the command manually."))
					.queue();

		}
	}

	/**
	 * Ban user.
	 *
	 * @param reactee        the reactee
	 * @param message        the message
	 * @param commandChannel the command channel
	 */
	private void approveBanRequest(final Member reactee, final Message message, final TextChannel commandChannel) {
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
				commandChannel.sendMessage(String.format(COMMAND_BAN_USER_DEFAULT, userToBan, evidence))
						.allowedMentions(new ArrayList<MentionType>()).queue();
			} else if (banRequestMessageContent[0].equalsIgnoreCase(";forceban")) {
				commandChannel.sendMessage(String.format(COMMAND_FORCEBAN_USER_DEFAULT, userToBan, evidence))
						.allowedMentions(new ArrayList<MentionType>()).queue();
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

			commandChannel.sendFile(messageContent.getBytes(), attachmentTitle + ".txt").queue(messageWithEvidence -> {
				commandChannel
						.sendMessage(String.format(COMMAND_MUTE_USER_DEFAULT, messageAuthor.getId(), muteDuration,
								String.format(COMMAND_REASON, reactee.getUser().getAsTag(), reactee.getId(),
										messageContent.replace("\n", " ").substring(0, 17) + "... Full evidence: "
												+ messageWithEvidence.getAttachments().get(0).getUrl())))
						.allowedMentions(new ArrayList<MentionType>()).queue();
			});
		}

	}

	/**
	 * Clear messages.
	 *
	 * @param messageAuthor the message author
	 * @param channel       the channel
	 */
	private void purgeMessagesInChannel(final Member messageAuthor, final MessageChannel channel) {
		final List<Message> messagesToDelete = new ArrayList<>();
		channel.getIterableHistory().takeAsync(200).thenAccept(messages -> {
			for (final Message message : messages) {
				if (message.getAuthor().getIdLong() == messageAuthor.getIdLong()) {
					messagesToDelete.add(message);
				}
			}
			log.info("Purging {} messages", messagesToDelete.size());
			channel.purgeMessages(messagesToDelete);
		});
	}

}
