/*
 * Author: {Ruben Veiga}
 * Contributor: {Liscuate}
 */

package com.misterveiga.cds.listeners;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

	/** The cds data. */
	@Autowired
	public CdsDataImpl cdsData;

	private Instant lastAlertTime = Instant.now();

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(ReactionListener.class);

	private static final String ID_REACTION_ALERT_MODS = "625429388229345280"; // Alert mods emoji

	/** The Constant ID_REACTION_QM_30. */
	private static final String ID_REACTION_QM_30 = "760204798984454175"; // 30 minute quick-mute emoji

	/** The Constant ID_REACTION_QM_60. */
	private static final String ID_REACTION_QM_60 = "452813334429827072"; // 60 minute quick-mute emoji

	/** The Constant ID_REACTION_APPROVE_BAN_REQUEST. */
	private static final String ID_REACTION_APPROVE = "762388343253106688"; // Ban request approval emoji

	/** The Constant ID_REACTION_REJECT_BAN_REQUEST. */
	private static final String ID_REACTION_REJECT = "764268551473070080"; // Ban request rejection emoji

	/** The Constant ID_REACTION_PURGE_MESSAGES. */
	private static final String ID_REACTION_PURGE_MESSAGES = "783030737552670801"; // Purge messages emoji

	/** The Constant COMMAND_MUTE_USER_DEFAULT. */
	private static final String COMMAND_MUTE_USER_DEFAULT = ";mute %s %s %s";

	/** The Constant COMMAND_BAN_USER_DEFAULT. */
	private static final String COMMAND_BAN_USER_DEFAULT = ";ban %s %s";

	/** The Constant COMMAND_FORCEBAN_USER_DEFAULT. */
	private static final String COMMAND_FORCEBAN_USER_DEFAULT = ";forceban %s %s";

	/** The Constant COMMAND_REASON. */
	private static final String COMMAND_REASON = "(By %s (%s)) Message Evidence: %s";

	/** The Constant COMMAND_UNMUTE_USER_DEFAULT. */
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
			event.getGuild().retrieveMemberById(message.getAuthor().getId()).queue(messageAuthor -> {

				if (emoteId.equals(ID_REACTION_ALERT_MODS)) {
					alertMods(event.getGuild().getTextChannelById(Properties.CHANNEL_MOD_ALERTS_ID), reactee, message,
							messageAuthor, Instant.now());
				}

				if (!RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
						RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {
					return; // Do nothing.
				}

				final Action commandAction = new Action();
				commandAction.setDate(Date.from(OffsetDateTime.now(ZoneOffset.UTC).toInstant()));
				commandAction.setUser(reactee.getUser().getAsTag());
				commandAction.setDiscordId(reactee.getIdLong());

				switch (emoteId) {

				case ID_REACTION_PURGE_MESSAGES:

					blockIfStaffOnStaff(reactee, messageAuthor, commandChannel);

					if (RoleUtils.isAnyRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER,
							RoleUtils.ROLE_COMMUNITY_SUPERVISOR)) {

						purgeMessagesInChannel(messageAuthor, channel);
						commandAction.setOffendingUser(messageAuthor.getUser().getAsTag());
						commandAction.setOffendingUserId(messageAuthor.getIdLong());
						commandAction.setActionType("REACTION_PURGE_MESSAGES");
						log.info("[Reaction Command] Message purge executed by {} on {}", reactee.getUser().getAsTag(),
								messageAuthor.getUser().getAsTag());
					}

					break;

				case ID_REACTION_QM_30:

					blockIfStaffOnStaff(reactee, messageAuthor, commandChannel);

					if (RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER,
							RoleUtils.ROLE_COMMUNITY_SUPERVISOR)) {

						muteUser(reactee, messageAuthor, "30m", message, commandChannel);
						purgeMessagesInChannel(messageAuthor, channel);
						commandAction.setOffendingUser(messageAuthor.getUser().getAsTag());
						commandAction.setOffendingUserId(messageAuthor.getIdLong());
						commandAction.setActionType("REACTION_QM_30");
						log.info("[Reaction Command] 30m Quick-Mute executed by {} on {}", reactee.getUser().getAsTag(),
								messageAuthor.getUser().getAsTag());

					}

					break;

				case ID_REACTION_QM_60:

					blockIfStaffOnStaff(reactee, messageAuthor, commandChannel);

					if (RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER,
							RoleUtils.ROLE_COMMUNITY_SUPERVISOR)) {

						muteUser(reactee, messageAuthor, "1h", message, commandChannel);
						purgeMessagesInChannel(messageAuthor, channel);
						commandAction.setOffendingUser(messageAuthor.getUser().getAsTag());
						commandAction.setOffendingUserId(messageAuthor.getIdLong());
						commandAction.setActionType("REACTION_QM_60");
						log.info("[Reaction Command] 1h Quick-Mute executed by {} on {}", reactee.getUser().getAsTag(),
								messageAuthor.getUser().getAsTag());

					}

					break;

				case ID_REACTION_APPROVE: // Used for ban requests, filtered log bans, and mod alerts.

					if (RoleUtils.isAnyRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER,
							RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {

						if (event.getChannel().getIdLong() == Properties.CHANNEL_MOD_ALERTS_ID) {
							clearAlert(commandChannel,
									event.getGuild().getTextChannelById(Properties.CHANNEL_MOD_ALERTS_ID), reactee,
									message, messageAuthor, Instant.now());
						}

						if (event.getChannel().getIdLong() == Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID) {

							approveBanRequest(reactee, message, commandChannel);

						} else if (event.getChannel().getIdLong() == Properties.CHANNEL_CENSORED_AND_SPAM_LOGS_ID) {

							approveCensoredBan(reactee, message, commandChannel);

						}

						commandAction.setActionType("REACTION_APPROVE_BAN_REQUEST");
						log.info("[Reaction Command] Ban request approved by {} ({}) (request: {})",
								reactee.getEffectiveName(), reactee.getId(), message.getJumpUrl());

					} else if (RoleUtils.isAnyRole(event.getMember(), RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
							RoleUtils.ROLE_TRIAL_SUPERVISOR)) {
						if (event.getChannel().getIdLong() == Properties.CHANNEL_MOD_ALERTS_ID) {
							clearAlert(commandChannel,
									event.getGuild().getTextChannelById(Properties.CHANNEL_MOD_ALERTS_ID), reactee,
									message, messageAuthor, Instant.now());
						}
					}

					break;

				case ID_REACTION_REJECT:

					if (event.getChannel().getIdLong() == Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID
							&& RoleUtils.isAnyRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER,
									RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {

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

		});

	}

	private void alertMods(final TextChannel alertChannel, final Member reactee, final Message message,
			final Member messageAuthor, final Instant now) {
		if (alertChannel != null && ChronoUnit.SECONDS.between(lastAlertTime, now) > Properties.ALERT_MODS_COOLDOWN) {
			lastAlertTime = now;
			alertChannel
					.sendMessage(new StringBuilder()
							.append(alertChannel.getJDA().getEmoteById(ID_REACTION_ALERT_MODS).getAsMention())
							.append(" ")
							.append(RoleUtils
									.getRoleByName(alertChannel.getGuild(), RoleUtils.ROLE_COMMUNITY_SUPERVISOR)
									.getAsMention())
							.append(" Alert received from ").append(reactee.getAsMention()).append(" (ID: ")
							.append(reactee.getId()).append("):\n").append(message.getJumpUrl()))
					.queue(msg -> {
						msg.delete().queueAfter(2, TimeUnit.HOURS);
					});
		}
	}

	private void clearAlert(final TextChannel commandChannel, final TextChannel alertChannel, final Member reactee,
			final Message message, final Member messageAuthor, final Instant now) {
		message.delete().queue();
	}

	/**
	 * Approve censored ban.
	 *
	 * @param reactee        the reactee
	 * @param message        the message
	 * @param commandChannel the command channel
	 */
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

	/**
	 * Reject ban request.
	 *
	 * @param reactee        the reactee
	 * @param message        the message
	 * @param commandChannel the command channel
	 */
	private void rejectBanRequest(final Member reactee, final Message message, final TextChannel commandChannel) {
		try {

			final String[] banRequestMessageContent = message.getContentStripped().split(" ");
			final String reportedUserId = banRequestMessageContent[1];
			final User reportedUser = commandChannel.getJDA().getUserById(reportedUserId);

			final StringBuilder sb = new StringBuilder();

			sb.append(message.getAuthor().getAsMention())
					.append(commandChannel.getJDA().getEmoteById(ID_REACTION_REJECT).getAsMention())
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
	 * Approve ban request.
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
	 * Purge messages in channel.
	 *
	 * @param messageAuthor the message author
	 * @param channel       the channel
	 */
	private void purgeMessagesInChannel(final Member messageAuthor, final MessageChannel channel) {
		channel.getIterableHistory().takeAsync(200).thenAccept(messages -> {
			final List<Message> messagesToDelete = messages.stream()
					.filter(msg -> msg.getAuthor().getIdLong() == messageAuthor.getIdLong())
					.collect(Collectors.toList());

			log.debug("Purging {} messages", messagesToDelete.size());
			channel.purgeMessages(messagesToDelete);
		});
	}

	private void blockIfStaffOnStaff(final Member reactee, final Member messageAuthor,
			final TextChannel commandChannel) {
		if (RoleUtils.isAnyRole(reactee, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
				RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)
				&& RoleUtils.isAnyRole(messageAuthor, RoleUtils.ROLE_COMMUNITY_SUPERVISOR,
						RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR)) {
			commandChannel.sendMessage(new StringBuilder().append(reactee.getAsMention())
					.append(" you cannot run commands on server staff.")).queue();
			return; // Do nothing.
		}
	}

}
