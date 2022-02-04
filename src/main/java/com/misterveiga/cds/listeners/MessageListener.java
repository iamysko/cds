/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.listeners;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.misterveiga.cds.command.CommandImpl;
import com.misterveiga.cds.utils.DurationUtils;
import com.misterveiga.cds.utils.MessageFilter;
import com.misterveiga.cds.utils.Properties;
import com.misterveiga.cds.utils.RegexConstants;
import com.misterveiga.cds.utils.RoleUtils;
import com.misterveiga.cds.utils.StringBuilds;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;


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
public class MessageListener extends ListenerAdapter {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(MessageListener.class);

	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		if (event.getAuthor().equals(event.getJDA().getSelfUser())) { // Do nothing if sender is self.
			return;
		}

		if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER) != null) {
			log.debug("Message received from a server manager.");
			scanMessage(event.getMessage(), 3);
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SENIOR_MODERATOR) != null) {
			log.debug("Message received from a senior moderator.");
			scanMessage(event.getMessage(), 2);
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_MODERATOR) != null) {
			log.debug("Message received from a moderator.");
			scanMessage(event.getMessage(), 1);
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_TRIAL_MODERATOR) != null) {
			log.debug("Message received from a trial moderator.");
			if (!MessageFilter.filterMessage()) {
				log.debug("Message filter triggered. Deleting.");
				event.getMessage().delete().queue();
			} else {
				scanMessage(event.getMessage(), 0);
			}

		} else {
			if (!MessageFilter.filterMessage()) {
				log.debug("Message filter triggered. Deleting.");
				event.getMessage().delete().queue();
			} else {
				scanMessage(event.getMessage(), -1);
			}
		}
	}

	/**
	 * Scan message.
	 *
	 * @param message the message
	 * @param i       the i
	 */
	private void scanMessage(final Message message, final int i) {

		final String messageText = message.getContentRaw();
		
		if (message.getChannel().getIdLong() == Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID) {
			if (!RoleUtils.isAnyRole(message.getMember(), RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_SENIOR_MODERATOR,
					RoleUtils.ROLE_LEAD, RoleUtils.ROLE_BOT)) {
				validateBanRequest(message);
			}
		}

		if (!messageText.matches(RegexConstants.GENERIC)) { // If not a command, do nothing.
			return;
		}

		final TextChannel commandChannel = message.getGuild().getTextChannelById(Properties.CHANNEL_COMMANDS_ID);
		final Member author = message.getMember();
		final String authorMention = author.getAsMention();

		log.info("Command received from authorized user {}: {}", author.getEffectiveName(), messageText);

		switch (i) {
		case 3: // MGMT

			if (messageText.matches(RegexConstants.COMMAND_BAN)) { // BAN (-b userId,userId2,userIdN reason)

				final Map<String, String> data = getDataFromBanMessage(messageText);
				final List<String> userIdsToBan = Arrays.asList(data.get("users").split(",")).stream().distinct()
						.collect(Collectors.toList());
				final String reason = data.get("users");

				if (!CollectionUtils.isEmpty(userIdsToBan)) {
					CommandImpl.executeBan(commandChannel, author, authorMention, userIdsToBan, reason);
				} else {
					message.getChannel().sendMessage(new StringBuilder().append(authorMention)
							.append(" User IDs must be provided to execute bans. For help, run -?")).queue();
				}

				break;

			} else if (messageText.matches(RegexConstants.COMMAND_UNBAN)) { // UNBAN (-ub userId,userId2,userIdN)

				final Map<String, String> data = getDataFromUnbanMessage(messageText);
				final List<String> userIdsToUnban = Arrays.asList(data.get("users").split(",")).stream().distinct()
						.collect(Collectors.toList());

				if (!CollectionUtils.isEmpty(userIdsToUnban)) {
					CommandImpl.executeUnban(commandChannel, authorMention, userIdsToUnban);
				} else {
					message.getChannel()
							.sendMessage(new StringBuilder().append(authorMention)
									.append(" User IDs must be provided to execute bans. For help, run rdss:?"))
							.queue();
				}

				break;

			}

		case 2: // SCS

		case 1: // CS
			if (messageText.matches(RegexConstants.COMMAND_WARN)) { // WARN USER (SENDS A DM TO USER)
				// final Map<String, String> data = getDataFromMuteMessage(messageText);
				// TODO: Warn user.
				break;

			} else if (messageText.matches(RegexConstants.COMMAND_MUTE)) {
				final Map<String, String> data = getDataFromMuteMessage(messageText);
				final String reason = data.get("reason");
				if (reason.isEmpty()) {
					message.getChannel().sendMessage(new StringBuilder().append(authorMention)
							.append(" Mutes must always include evidence. For more help, run rdss:?")).queue();
				} else {
					final List<String> userIdsToMute = Arrays.asList(data.get("users").split(",")).stream().distinct()
							.collect(Collectors.toList());
					final Instant muteEndDate = DurationUtils.addDurationStringToCurrentDate(data.get("duration"));

					if (muteEndDate != null) {
						if (!CollectionUtils.isEmpty(userIdsToMute)) {
							CommandImpl.executeMute(commandChannel, author, authorMention, userIdsToMute, muteEndDate,
									reason);
						} else {
							message.getChannel()
									.sendMessage(new StringBuilder().append(authorMention).append(
											" User IDs must be provided to execute mutes. For help, run rdss:?"))
									.queue();
						}
					} else {
						message.getChannel().sendMessage(new StringBuilder().append(authorMention).append(
								" Mute duration format must follow \"XdXhXm\" (days, hours, minutes). For more help, run rdss:?"))
								.queue();
					}
				}
				break;
			} else if (messageText.matches(RegexConstants.COMMAND_UNMUTE)) {
				final Map<String, String> data = getDataFromUnmuteMessage(messageText);
				final List<String> userIdsToUnmute = Arrays.asList(data.get("users").split(",")).stream().distinct()
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(userIdsToUnmute)) {
					CommandImpl.executeUnmute(commandChannel, authorMention, userIdsToUnmute);
				} else {
					message.getChannel()
							.sendMessage(new StringBuilder().append(authorMention)
									.append(" User IDs must be provided to execute bans. For help, run rdss:?"))
							.queue();
				}
				break;
			}
		case 0: // TS
			if (messageText.matches(RegexConstants.COMMAND_HELP)
					|| messageText.matches(RegexConstants.COMMAND_HELP_ALT)) {
				message.getChannel().sendMessage(StringBuilds.getHelpMessage(authorMention)).queue();
				break;
			} else if (messageText.matches(RegexConstants.COMMAND_ABOUT)) {
				message.getChannel().sendMessage(StringBuilds.getAboutMessage(authorMention)).queue();
				break;
			} else if (messageText.matches(RegexConstants.COMMAND_USER_INFO)) {
				message.getChannel().sendMessage("This command will show you the users info").queue();
			} else {
				sendUnknownCommandMessage(message, authorMention);
				break;
			}

		case -1: // Member with no command roles.
			break;
		}
	}

	private Map<String, String> getDataFromMuteMessage(final String messageText) {

		final Map<String, String> data = new HashMap<>();

		final String[] args = messageText.split(" ");

		final String[] argsRefined = new String[3];
		argsRefined[0] = args[1]; // "-m <userId1,userId2,userIdN> 1d1h1m1s This is an example of evidence."
		argsRefined[1] = args[2]; // "-m userId1,userId2,userIdN <1d1h1m1s> This is an example of evidence."

		final StringBuilder sb = new StringBuilder();

		for (int i = 3; i < args.length; i++) {
			if (i < args.length - 1) {
				sb.append(args[i]);
			} else {
				sb.append(args[i]).append(" ");
			}
		}

		argsRefined[2] = sb.toString(); // "-m userId1,userId2,userIdN 1d1h1m1s <This is an example of evidence.>"
		if (argsRefined[2] == null) {
			argsRefined[2] = "";
		}

		data.put("users", argsRefined[0]);
		data.put("duration", argsRefined[1]);
		data.put("reason", argsRefined[2]);

		return data;
	}

	/**
	 * Gets the data from ban message.
	 *
	 * @param messageText the message text
	 * @return the data from ban message
	 */
	private Map<String, String> getDataFromBanMessage(final String messageText) {

		final Map<String, String> data = new HashMap<>();

		final String[] args = messageText.split(" ");

		final String[] argsRefined = new String[2];
		argsRefined[0] = args[1]; // "-m <userId1,userId2,userIdN> This is an example of evidence."

		final StringBuilder sb = new StringBuilder();

		for (int i = 2; i < args.length; i++) {
			if (i < args.length - 1) {
				sb.append(args[i]);
			} else {
				sb.append(args[i]).append(" ");
			}
		}

		argsRefined[1] = sb.toString(); // "-m userId1,userId2,userIdN <This is an example of evidence.>"
		if (argsRefined[1] == null) {
			argsRefined[1] = "(none)";
		}

		data.put("users", argsRefined[0]);
		data.put("reason", argsRefined[1]);

		return data;
	}

	private Map<String, String> getDataFromUnmuteMessage(final String messageText) {

		final Map<String, String> data = new HashMap<>();

		final String[] args = messageText.split(" ");

		data.put("users", args[1]); // "-um <userId1,userId2,userIdN>"

		return data;
	}

	/**
	 * Gets the data from unban message.
	 *
	 * @param messageText the message text
	 * @return the data from unban message
	 */
	private Map<String, String> getDataFromUnbanMessage(final String messageText) {

		final Map<String, String> data = new HashMap<>();

		final String[] args = messageText.split(" ");

		data.put("users", args[1]); // "-b <userId1,userId2,userIdN>"

		return data;
	}


	/**
	 * Send unknown command message.
	 *
	 * @param message       the message
	 * @param authorMention the author mention
	 */
	private void sendUnknownCommandMessage(final Message message, final String authorMention) {
		message.getChannel().sendMessage(new StringBuilder().append(authorMention)
				.append(" Sorry, I don't know that command.\n*Use rdss:? for assistance.*")).queue();
	}
	
	/**
	 * Validate a ban request by checking if the:
	 * Correct format is used
	 * User ID exists
	 * User ID doesn't belong to a staff member
	 *
	 * @param message       the message
	 */
	private void validateBanRequest(final Message message) {
		if (!message.getContentRaw().matches("(?si)^;(?:force)?ban\\s\\d{17,19}\\s.+$")) {
			message.reply("Incorrect ban request format. Please use `;ban <user id> <reason>`")
					.mentionRepliedUser(true)
					.setActionRow(Button.primary("DeleteMessage", "Hide Alert"))
					.queue();
		} else {
			Pattern regexPattern = Pattern.compile("(?si)^;(?:force)?ban\\s(\\d{17,19})\\s.+$");
			Matcher matchedResults = regexPattern.matcher(message.getContentRaw());
			matchedResults.find();

			message.getJDA().getGuildById(Properties.GUILD_ROBLOX_DISCORD_ID).retrieveMemberById(matchedResults.group(1)).queue(targetedMember -> {

				if (RoleUtils.isAnyRole(targetedMember, RoleUtils.ROLE_LEAD, RoleUtils.ROLE_SERVER_MANAGER, RoleUtils.ROLE_MODERATOR,
						RoleUtils.ROLE_SENIOR_MODERATOR, RoleUtils.ROLE_BOT, RoleUtils.ROLE_TRIAL_MODERATOR)) {

					message.reply("The provided ID belongs to a staff member.")
							.mentionRepliedUser(true)
							.setActionRow(Button.primary("DeleteMessage", "Hide Alert"))
							.queue();
				}
			}, new ErrorHandler()
					.ignore(ErrorResponse.UNKNOWN_MEMBER)
					.handle(ErrorResponse.UNKNOWN_USER,
							(error) -> message.reply("The provided ID is not a valid user ID")
									.mentionRepliedUser(true)
									.setActionRow(Button.primary("DeleteMessage", "Hide Alert"))
									.queue()));

		}
	}
}
