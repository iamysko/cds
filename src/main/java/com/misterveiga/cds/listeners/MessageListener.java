/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.listeners;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.misterveiga.cds.data.CdsDataImpl;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.entities.MutedUser;
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

	/** The app name. */
	@Value("${cds.name}")
	public String appName;

	/** The app version. */
	@Value("${cds.version}")
	public String appVersion;

	/** The cds data. */
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

	/**
	 * Scan message.
	 *
	 * @param message the message
	 * @param i       the i
	 */
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
				final List<String> userIdsToBan = Arrays.asList(data.get("users").split(",")).stream().distinct()
						.collect(Collectors.toList());
				final String reason = data.get("users");

				if (!CollectionUtils.isEmpty(userIdsToBan)) {
					executeBan(commandChannel, author, authorMention, userIdsToBan, reason);
				} else {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append(" User IDs must be provided to execute bans. For help, run -?")).queue();
				}

			} else if (messageText.matches(RegexConstants.COMMAND_UNBAN)) { // UNBAN (-ub userId,userId2,userIdN)

				final Map<String, String> data = getDataFromUnbanMessage(messageText);
				final List<String> userIdsToUnban = Arrays.asList(data.get("users").split(",")).stream().distinct()
						.collect(Collectors.toList());

				if (!CollectionUtils.isEmpty(userIdsToUnban)) {
					executeUnban(commandChannel, authorMention, userIdsToUnban);
				} else {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append(" User IDs must be provided to execute bans. For help, run -?")).queue();
				}

			} else if (messageText.matches(RegexConstants.SHOW_BANNED_USERS)) { // LIST BANNED USERS (-show_bans)
				// TODO: Show banned users from bans collection.
			} else if (messageText.matches(RegexConstants.SHOW_MUTED_USERS)) { // LIST MUTED USERS (-show_mutes)
				// TODO: Show banned users from bans collection.
			}

		case 2: // SCS

		case 1: // CS
			if (messageText.matches(RegexConstants.COMMAND_WARN)) { // WARN USER (SENDS A DM TO USER)
				// final Map<String, String> data = getDataFromMuteMessage(messageText);
				// TODO: Warn user.

			} else if (messageText.matches(RegexConstants.COMMAND_MUTE)) {
				final Map<String, String> data = getDataFromMuteMessage(messageText);
				final String reason = data.get("reason");
				if (reason.isEmpty()) {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append(" Mutes must always include evidence. For more help, run -?")).queue();
				} else {
					final List<String> userIdsToMute = Arrays.asList(data.get("users").split(",")).stream().distinct()
							.collect(Collectors.toList());
					final Instant muteEndDate = getEndDateFromDuration(data.get("duration"));

					if (muteEndDate != null) {
						if (!CollectionUtils.isEmpty(userIdsToMute)) {
							executeMute(commandChannel, author, authorMention, userIdsToMute, muteEndDate, reason);
						} else {
							commandChannel
									.sendMessage(new StringBuilder().append(authorMention)
											.append(" User IDs must be provided to execute mutes. For help, run -?"))
									.queue();
						}
					} else {
						commandChannel.sendMessage(new StringBuilder().append(authorMention).append(
								" Mute duration format must follow \"XdXhXm\" (days, hours, minutes). For more help, run -?"))
								.queue();
					}
				}
			} else if (messageText.matches(RegexConstants.COMMAND_UNMUTE)) {
				final Map<String, String> data = getDataFromUnmuteMessage(messageText);
				final List<String> userIdsToUnmute = Arrays.asList(data.get("users").split(",")).stream().distinct()
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(userIdsToUnmute)) {
					executeUnmute(commandChannel, authorMention, userIdsToUnmute);
				} else {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append(" User IDs must be provided to execute bans. For help, run -?")).queue();
				}
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

	private void executeMute(final TextChannel commandChannel, final Member author, final String authorMention,
			final List<String> userIdsToMute, final Instant muteEndDate, final String reason) {
		try {
			for (final String userId : userIdsToMute) {
				final Long id = Long.valueOf(userId);
				try {
					commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
						final MutedUser mutedUser = new MutedUser();
						mutedUser.setStartDate(Instant.now());
						mutedUser.setEndDate(muteEndDate);
						mutedUser.setModeratorUserId(author.getIdLong());
						mutedUser.setModeratorDiscordTag(author.getUser().getAsTag());
						mutedUser.setMutedUserId(id);
						mutedUser.setMutedUserDiscordTag(
								commandChannel.getGuild().retrieveMemberById(userId).complete().getUser().getAsTag());
						mutedUser.setMuteReason(reason);

						commandChannel.getGuild()
								.addRoleToMember(id,
										RoleUtils.getRoleByName(commandChannel.getGuild(), RoleUtils.ROLE_MUTED))
								.queue(success -> {
									cdsData.insertMutedUser(mutedUser);
								});
						commandChannel.sendMessage((new StringBuilder().append(authorMention)
								.append(" Successfully muted ").append(mutedUser.getMutedUserDiscordTag()).append("(")
								.append(userId).append(")!\nThe mute will be lifted automatically on ")
								.append(mutedUser.getEndDate().toString())
								.append("\nTo unmute this user, use -ub (see -? for help)."))).queue();

					});
				} catch (final ErrorResponseException e) {
					commandChannel
							.sendMessage(new StringBuilder().append(authorMention).append("User ").append(userId)
									.append(" could not be muted (User ID not resolvable or possible internal error)."))
							.queue();
				}
			}
		} catch (final NumberFormatException e) {
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append("Command parameters incorrect. For more information, see -help or -?")).queue();
		}
	}

	private void executeUnmute(final TextChannel commandChannel, final String authorMention,
			final List<String> userIdsToUnmute) {
		try {
			for (final String userId : userIdsToUnmute) {
				final Long id = Long.valueOf(userId);
				try {
					commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
						commandChannel.getGuild()
								.removeRoleFromMember(id,
										RoleUtils.getRoleByName(commandChannel.getGuild(), RoleUtils.ROLE_MUTED))
								.queue(success -> {
									cdsData.removeMutedUser(id);
									commandChannel.sendMessage(new StringBuilder().append(authorMention)
											.append(" Successfully unmuted user ").append(userId)).queue();
								});
					});
				} catch (final ErrorResponseException e) {
					commandChannel.sendMessage(new StringBuilder().append(authorMention).append("User ").append(userId)
							.append(" could not be unmuted (User ID not resolvable or possible internal error)."))
							.queue();
				}
			}
		} catch (final NumberFormatException e) {
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append("Command parameters incorrect. For more information, see -help or -?")).queue();
		}
	}

	/**
	 * Execute ban.
	 *
	 * @param commandChannel the command channel
	 * @param author         the author
	 * @param authorMention  the author mention
	 * @param userIdsToBan   the user ids to ban
	 * @param reason         the reason
	 */
	private void executeBan(final TextChannel commandChannel, final Member author, final String authorMention,
			final List<String> userIdsToBan, final String reason) {
		try {
			for (final String userId : userIdsToBan) {
				final Long id = Long.valueOf(userId);
				try {
					commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
						final BannedUser bannedUser = new BannedUser();
						bannedUser.setDate(Instant.now());
						bannedUser.setModeratorUserId(author.getIdLong());
						bannedUser.setModeratorDiscordTag(author.getUser().getAsTag());
						bannedUser.setBannedUserId(id);
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
		} catch (final NumberFormatException e) {
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append("Command parameters incorrect. For more information, see -help or -?")).queue();
		}
	}

	/**
	 * Execute unban.
	 *
	 * @param commandChannel the command channel
	 * @param authorMention  the author mention
	 * @param userIdsToUnban the user ids to unban
	 */
	private void executeUnban(final TextChannel commandChannel, final String authorMention,
			final List<String> userIdsToUnban) {
		try {
			for (final String userId : userIdsToUnban) {
				final Long id = Long.valueOf(userId);
				try {
					commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
						commandChannel.getGuild().unban(userId).queue(success -> {
							cdsData.removeBannedUser(id);
							commandChannel.sendMessage("Unbanned user " + userId);
						});
					});
				} catch (final ErrorResponseException e) {
					commandChannel
							.sendMessage(new StringBuilder().append(authorMention)
									.append("This user could not be banned (User ID not resolvable): ").append(userId))
							.queue();
				}
			}
		} catch (final NumberFormatException e) {
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append("Command parameters incorrect. For more information, see -help or -?")).queue();
		}
	}

	private Map<String, String> getDataFromMuteMessage(final String messageText) {

		final Map<String, String> data = new HashMap<>();

		final String[] args = messageText.split(" ");

		final String[] argsRefined = new String[2];
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
			argsRefined[1] = "";
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
	 * Send help message.
	 *
	 * @param message       the message
	 * @param authorMention the author mention
	 */
	private void sendHelpMessage(final Message message, final String authorMention) {
		message.getChannel()
				.sendMessage(new StringBuilder().append(authorMention).append(" **Roblox Discord Services | Help**")
						.append("\nPrefix for all commands: `-<command>`")
						.append("\nIf a command doesn't work for you, you may not have permission to run it.")
						.append("\nHelp: \"-help\" or \"-?\"")
						.append("\nWarn user(s): \"-w user1,user2,userN warning message (necessary)\"")
						.append("\nMute user(s): \"-m user1,user2,userN XdXhXm reason (necessary)\"")
						.append("\nUnmute user(s): \"-um user1,user2,userN\"")
						.append("\nBan user(s): \"-b user1,user2,userN reason (optional)\"")
						.append("\nUnban user(s): \"-ub user1,user2,userN\"")
						.append("\nShow active bans: \"-show_bans\"").append("\nShow active mutes: \"-show_mutes\""))
				.queue();
	}

	/**
	 * Send about message.
	 *
	 * @param message       the message
	 * @param authorMention the author mention
	 */
	private void sendAboutMessage(final Message message, final String authorMention) {
		message.getChannel()
				.sendMessage(new StringBuilder().append(authorMention).append(" **Community Discord Services | About**")
						.append("\nApplication: ").append(appName).append("\nVersion: ").append(appVersion)
						.append("\n*Collaborate: https://github.com/misterveiga/cds*"))
				.queue();
	}

	/**
	 * Send unknown command message.
	 *
	 * @param message       the message
	 * @param authorMention the author mention
	 */
	private void sendUnknownCommandMessage(final Message message, final String authorMention) {
		message.getChannel().sendMessage(new StringBuilder().append(authorMention)
				.append(" Sorry, I don't know that command.\n*Use -? or -help for assistance.*")).queue();
	}

	private Instant getEndDateFromDuration(String durationString) {
		// Format is XdXhXm for days, hours and minutes. Ignore others.
		durationString = durationString.toLowerCase();
		try {
			if (durationString.indexOf('d') != -1) {
				if (durationString.indexOf('h') != -1) {
					if (durationString.indexOf('m') != -1) {
						// d h m present
						final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
						final Long hours = Long.parseLong(
								durationString.substring(durationString.indexOf('d') + 1, durationString.indexOf('h')));
						final Long minutes = Long.parseLong(
								durationString.substring(durationString.indexOf('h') + 1, durationString.indexOf('m')));
						final Long totalMinutes = (days * 1440) + (hours * 60) + minutes;
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					} else {
						// d h present
						final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
						final Long hours = Long.parseLong(
								durationString.substring(durationString.indexOf('d') + 1, durationString.indexOf('h')));
						final Long totalMinutes = (days * 1440) + (hours * 60);
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					}
				} else if (durationString.indexOf('m') != -1) {
					// d m present
					final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
					final Long minutes = Long.parseLong(
							durationString.substring(durationString.indexOf('d') + 1, durationString.indexOf('m')));
					final Long totalMinutes = (days * 1440) + minutes;
					return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
				} else {

					// d present
					final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
					final Long totalMinutes = (days * 1440);
					return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
				}
			} else {
				if (durationString.indexOf('h') != -1) {
					if (durationString.indexOf('m') != -1) {
						// h m present
						final Long hours = Long.parseLong(durationString.substring(0, durationString.indexOf('h')));
						final Long minutes = Long.parseLong(
								durationString.substring(durationString.indexOf('h') + 1, durationString.indexOf('m')));
						final Long totalMinutes = (hours * 60) + minutes;
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					} else {
						// h present
						final Long hours = Long.parseLong(durationString.substring(0, durationString.indexOf('h')));
						final Long totalMinutes = (hours * 60);
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					}
				} else {
					if (durationString.indexOf('m') != -1) {
						// m present
						final Long minutes = Long.parseLong(durationString.substring(0, durationString.indexOf('m')));
						final Long totalMinutes = minutes;
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);

					}
				}

			}
		} catch (final NumberFormatException e) {
			log.warn("Incorrect mute duration detected: {}", durationString);
		}

		return null;
	}

}