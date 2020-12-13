package com.misterveiga.cds.command;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsDataImpl;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.entities.MutedUser;
import com.misterveiga.cds.utils.RoleUtils;
import com.misterveiga.cds.utils.TableUtils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

@Component
public class CommandImpl {

	static Logger log = LoggerFactory.getLogger(CommandImpl.class);

	static CdsDataImpl cdsData;

	@Autowired
	public void setCdsData(final CdsDataImpl cdsData) {
		CommandImpl.cdsData = cdsData;
	}

	public static void executeListMutedUsers(final Message message) {
		final List<MutedUser> mutedUsers = cdsData.getMutedUsers();
		log.info("MongoDB returned {} currently muted users.", mutedUsers.size());
		final String[][] tableData = new String[mutedUsers.size()][7];

		final String[] headers = { "Muted User DiscordTag", "Muted User ID", "Moderator DiscordTag", "Moderator ID",
				"Mute Start Date", "Mute End Date", "Mute Reason" };

		for (int c = 0; c < mutedUsers.size(); c++) {
			tableData[c][0] = mutedUsers.get(c).getMutedUserDiscordTag();
			tableData[c][1] = String.valueOf(mutedUsers.get(c).getMutedUserId());
			tableData[c][2] = mutedUsers.get(c).getModeratorDiscordTag();
			tableData[c][3] = String.valueOf(mutedUsers.get(c).getModeratorUserId());
			tableData[c][4] = mutedUsers.get(c).getStartDate().toString();
			tableData[c][5] = mutedUsers.get(c).getEndDate().toString();
			tableData[c][6] = mutedUsers.get(c).getMuteReason();
		}
		try {
			final File generatedImage = new File("muted_users.jpg");
			ImageIO.write(TableUtils.createImageFromData(tableData, headers), "jpg", generatedImage);

			final StringBuilder sb = new StringBuilder();
			sb.append("**Currently Banned Users**\n");
			sb.append(
					"```Banned User DiscordTag | Banned User ID | Moderator DiscordTag | Moderator ID | Ban Date | Ban Reason\n");
			mutedUsers.forEach(mutedUser -> {
				sb.append(mutedUser.getMutedUserDiscordTag()).append(" | ").append(mutedUser.getMutedUserId())
						.append(" | ").append(mutedUser.getModeratorDiscordTag()).append(" | ")
						.append(mutedUser.getModeratorUserId()).append(" | ")
						.append(mutedUser.getStartDate().toString()).append(" | ")
						.append(mutedUser.getEndDate().toString()).append(" | ").append(mutedUser.getMuteReason());
			});
			sb.append("```");
			message.getChannel().sendMessage(sb.toString()).addFile(generatedImage, "muted_users.jpg").queue();

		} catch (final IOException e) {
			log.error("Muted users table image could not be generated. Sending plaintext version.");
			final StringBuilder sb = new StringBuilder();
			sb.append("**Currently Banned Users**\n");
			sb.append(
					"```Banned User DiscordTag | Banned User ID | Moderator DiscordTag | Moderator ID | Ban Date | Ban Reason\n");
			mutedUsers.forEach(mutedUser -> {
				sb.append(mutedUser.getMutedUserDiscordTag()).append(" | ").append(mutedUser.getMutedUserId())
						.append(" | ").append(mutedUser.getModeratorDiscordTag()).append(" | ")
						.append(mutedUser.getModeratorUserId()).append(" | ")
						.append(mutedUser.getStartDate().toString()).append(" | ")
						.append(mutedUser.getEndDate().toString()).append(" | ").append(mutedUser.getMuteReason());
			});
			sb.append("```");
			message.getChannel().sendMessage(sb.toString()).queue();
		}
	}

	public static void executeListBannedUsers(final Message message) {
		final List<BannedUser> bannedUsers = cdsData.getBannedUsers();
		log.info("MongoDB returned {} currently banned users.", bannedUsers.size());
		final String[][] tableData = new String[bannedUsers.size()][6];

		final String[] headers = { "Banned User DiscordTag", "Banned User ID", "Moderator DiscordTag", "Moderator ID",
				"Ban Date", "Ban Reason" };
		for (int c = 0; c < bannedUsers.size(); c++) {
			tableData[c][0] = bannedUsers.get(c).getBannedUserDiscordTag();
			tableData[c][1] = String.valueOf(bannedUsers.get(c).getBannedUserId());
			tableData[c][2] = bannedUsers.get(c).getModeratorDiscordTag();
			tableData[c][3] = String.valueOf(bannedUsers.get(c).getModeratorUserId());
			tableData[c][4] = bannedUsers.get(c).getDate().toString();
			tableData[c][5] = bannedUsers.get(c).getBannedUserReason();
		}
		try {
			final File generatedImage = new File("banned_users.jpg");
			ImageIO.write(TableUtils.createImageFromData(tableData, headers), "jpg", generatedImage);

			final StringBuilder sb = new StringBuilder();
			sb.append("**Currently Banned Users**\n");
			sb.append(
					"```Banned User DiscordTag | Banned User ID | Moderator DiscordTag | Moderator ID | Ban Date | Ban Reason\n");
			bannedUsers.forEach(bannedUser -> {
				sb.append(bannedUser.getBannedUserDiscordTag()).append(" | ").append(bannedUser.getBannedUserId())
						.append(" | ").append(bannedUser.getModeratorDiscordTag()).append(" | ")
						.append(bannedUser.getModeratorUserId()).append(" | ").append(bannedUser.getDate().toString())
						.append(" | ").append(bannedUser.getBannedUserReason()).append("\n");
			});
			sb.append("```");
			message.getChannel().sendMessage(sb.toString()).addFile(generatedImage, "banned_users.jpg").queue();

		} catch (final IOException e) {
			log.error("Banned users table image could not be generated. Sending plaintext version.");
			final StringBuilder sb = new StringBuilder();
			sb.append("**Currently Banned Users**\n");
			sb.append(
					"```Banned User DiscordTag | Banned User ID | Moderator DiscordTag | Moderator ID | Ban Date | Ban Reason\n");
			bannedUsers.forEach(bannedUser -> {
				sb.append(bannedUser.getBannedUserDiscordTag()).append(" | ").append(bannedUser.getBannedUserId())
						.append(" | ").append(bannedUser.getModeratorDiscordTag()).append(" | ")
						.append(bannedUser.getModeratorUserId()).append(" | ").append(bannedUser.getDate().toString())
						.append(" | ").append(bannedUser.getBannedUserReason()).append("\n");
			});
			sb.append("```");
			message.getChannel().sendMessage(sb.toString()).queue();
		}
	}

	public static void executeMute(final TextChannel commandChannel, final Member author, final String authorMention,
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
									log.info("Successfully muted user {} (mute executed by {})",
											mutedUser.getMutedUserDiscordTag(), mutedUser.getModeratorDiscordTag());
									commandChannel.sendMessage((new StringBuilder().append(authorMention)
											.append(" Successfully muted ").append(mutedUser.getMutedUserDiscordTag())
											.append("(").append(userId)
											.append(")!\nThe mute will be lifted automatically on ")
											.append(mutedUser.getEndDate().toString())
											.append("\nTo unmute this user, use rdss:unban (see rdss:? for help).")))
											.queue();
								});

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
					.append("Command parameters incorrect. For more information, see rdss:?")).queue();
		}
	}

	public static void executeUnmute(final TextChannel commandChannel, final String authorMention,
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
									log.info("Successfully unmuted user {} (mute executed by {})", id, authorMention);
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
					.append("Command parameters incorrect. For more information, see rdss:?")).queue();
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
	public static void executeBan(final TextChannel commandChannel, final Member author, final String authorMention,
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
							log.info("Successfully banned user {} (ban executed by {})",
									bannedUser.getBannedUserDiscordTag(), bannedUser.getModeratorDiscordTag());
							commandChannel.sendMessage(new StringBuilder().append(authorMention)
									.append("Successfully banned user ").append(bannedUser.getBannedUserDiscordTag())
									.append(" (").append(bannedUser.getBannedUserId()).append(")")).queue();
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
					.append("Command parameters incorrect. For more information, see rdss:?")).queue();
		}
	}

	/**
	 * Execute unban.
	 *
	 * @param commandChannel the command channel
	 * @param authorMention  the author mention
	 * @param userIdsToUnban the user ids to unban
	 */
	public static void executeUnban(final TextChannel commandChannel, final String authorMention,
			final List<String> userIdsToUnban) {
		try {
			for (final String userId : userIdsToUnban) {
				final Long id = Long.valueOf(userId);
				try {
					commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
						commandChannel.getGuild().unban(userId).queue(success -> {
							cdsData.removeBannedUser(id);
							log.info("Successfully unbanned user {} (unban executed by {})", id, authorMention);
							commandChannel.sendMessage(new StringBuilder().append(authorMention)
									.append("Successfully unbanned user ").append(userId)).queue();
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
					.append("Command parameters incorrect. For more information, see rdss:?")).queue();
		}
	}

}
