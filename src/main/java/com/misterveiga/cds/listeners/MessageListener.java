/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.listeners;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.misterveiga.cds.data.CdsDataImpl;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.utils.Properties;
import com.misterveiga.cds.utils.RegexConstants;
import com.misterveiga.cds.utils.RoleUtils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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

	@Value("${cds.name}")
	public String appName;

	@Value("${cds.version}")
	public String appVersion;

	@Autowired
	public CdsDataImpl cdsData;

	/**
	 * On message received.
	 *
	 * @param event the event
	 */
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		if (event.getAuthor().equals(event.getJDA().getSelfUser())) { // Do nothing if sender is self.
			return;
		}

		if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SERVER_MANAGER) != null) {
			log.debug("Message received from a server manager.");
			scanMessage(event.getMessage(), 3);
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_SENIOR_COMMUNITY_SUPERVISOR) != null) {
			log.debug("Message received from a senior community supervisor.");
			scanMessage(event.getMessage(), 2);
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_COMMUNITY_SUPERVISOR) != null) {
			log.debug("Message received from a community supervisor.");
			scanMessage(event.getMessage(), 1);
		} else if (RoleUtils.findRole(event.getMember(), RoleUtils.ROLE_TRIAL_SUPERVISOR) != null) {
			log.debug("Message received from a trial supervisor.");
			scanMessage(event.getMessage(), 0);
		} else {
			scanMessage(event.getMessage(), -1);
		}
	}

	private void scanMessage(final Message message, final int i) {

		final String messageText = message.getContentRaw();

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
				List<String> userIdsToBan = null;
				String reason = null;

				for (final Map.Entry<String, String> entry : data.entrySet()) {
					switch (entry.getKey()) {
					case "users":
						userIdsToBan = Arrays.asList(entry.getValue().split(","));
						break;
					case "reason":
						reason = data.get(entry.getValue());
						break;
					default:
						break;
					}
				}

				if (!CollectionUtils.isEmpty(userIdsToBan)) {
					executeBan(commandChannel, author, authorMention, userIdsToBan, reason);
				} else {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append("User IDs must be provided to execute bans. For help, run -?"));
				}

			} else if (messageText.matches(RegexConstants.COMMAND_UNBAN)) { // UNBAN (-ub userId,userId2,userIdN)

				final Map<String, String> data = getDataFromUnbanMessage(messageText);
				List<String> userIdsToUnban = null;

				for (final Map.Entry<String, String> entry : data.entrySet()) {
					switch (entry.getKey()) {
					case "users":
						userIdsToUnban = Arrays.asList(entry.getValue().split(","));
						break;
					default:
						break;
					}
				}

				if (!CollectionUtils.isEmpty(userIdsToUnban)) {
					executeUnban(commandChannel, authorMention, userIdsToUnban);
				} else {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append("User IDs must be provided to execute bans. For help, run -?"));
				}

			} else if (messageText.matches(RegexConstants.SHOW_BANNED_USERS)) { // UNBAN (-show_bans)
				// TODO: Show banned users from bans collection.
			}

		case 2: // SCS

		case 1: // CS
			if (messageText.matches(RegexConstants.COMMAND_WARN)) { // WARN USER (SENDS A DM TO USER)
				// final Map<String, String> data = getDataFromMuteMessage(messageText);
				// TODO: Warn user.

			} else if (messageText.matches(RegexConstants.COMMAND_MUTE)) {
				// TODO: Mute user.

			}
		case 0: // TS
			if (messageText.matches(RegexConstants.COMMAND_HELP)
					|| messageText.matches(RegexConstants.COMMAND_HELP_ALT)) {
				sendHelpMessage(message, authorMention);
			} else if (messageText.matches(RegexConstants.COMMAND_ABOUT)) {
				sendAboutMessage(message, authorMention);
			} else {
				sendUnknownCommandMessage(message, authorMention);
			}
			break;
		case -1: // Member with no command roles.
			break;
		}
	}

	private void executeBan(final TextChannel commandChannel, final Member author, final String authorMention,
			final List<String> userIdsToBan, final String reason) {
		for (final String userId : userIdsToBan) {
			try {
				commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
					final BannedUser bannedUser = new BannedUser();
					bannedUser.setDate(Date.from(OffsetDateTime.now(ZoneOffset.UTC).toInstant()));
					bannedUser.setModeratorUserId(author.getIdLong());
					bannedUser.setModeratorDiscordTag(author.getUser().getAsTag());
					bannedUser.setBannedUserId(Long.valueOf(userId));
					bannedUser.setBannedUserDiscordTag(
							commandChannel.getGuild().retrieveMemberById(userId).complete().getUser().getAsTag());
					bannedUser.setBannedUserReason(reason);

					member.ban(1, reason).queue(success -> {
						cdsData.insertBannedUser(bannedUser);
					});

				});
			} catch (final ErrorResponseException e) {
				commandChannel
						.sendMessage(new StringBuilder().append(authorMention)
								.append("This user could not be banned (User ID not resolvable): ").append(userId))
						.queue();
			}
		}
	}

	private void executeUnban(final TextChannel commandChannel, final String authorMention,
			final List<String> userIdsToUnban) {
		for (final String userId : userIdsToUnban) {
			try {
				commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
					commandChannel.getGuild().unban(userId).queue(success -> {
						cdsData.removeBannedUser(userId);
					});
				});
			} catch (final ErrorResponseException e) {
				commandChannel
						.sendMessage(new StringBuilder().append(authorMention)
								.append("This user could not be banned (User ID not resolvable): ").append(userId))
						.queue();
			}
		}
	}

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
			argsRefined[1] = "";
		}

		data.put("users", argsRefined[0]);
		data.put("reason", argsRefined[1]);

		return data;
	}

	private Map<String, String> getDataFromUnbanMessage(final String messageText) {

		final Map<String, String> data = new HashMap<>();

		final String[] args = messageText.split(" ");

		data.put("users", args[1]); // "-b <userId1,userId2,userIdN>"

		return data;
	}

	private void sendHelpMessage(final Message message, final String authorMention) {
		message.getChannel()
				.sendMessage(new StringBuilder().append(authorMention).append(" **Roblox Discord Services | Help**")
						.append("\nPrefix for all commands: `-<command>`")
						.append("\nBan user(s): \"-b user1,user2,userN reason (optional)\"")
						.append("\nUnban user(s): \"-ub user1,user2,userN\"").append("Help: \"-help\" or \"-?\""))
				.queue();
	}

	private void sendAboutMessage(final Message message, final String authorMention) {
		message.getChannel()
				.sendMessage(new StringBuilder().append(authorMention).append(" **Community Discord Services | About**")
						.append("\nApplication: ").append(appName).append("\nVersion: ").append(appVersion)
						.append("\n*Collaborate: https://github.com/misterveiga/cds*"))
				.queue();
	}

	private void sendUnknownCommandMessage(final Message message, final String authorMention) {
		message.getChannel().sendMessage(new StringBuilder().append(authorMention)
				.append(" Sorry, I don't know that command.\n*Use -? or -help for assistance.*")).queue();
	}

}