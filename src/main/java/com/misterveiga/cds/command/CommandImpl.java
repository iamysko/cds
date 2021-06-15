package com.misterveiga.cds.command;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsDataImpl;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.entities.MutedUser;
import com.misterveiga.cds.utils.RoleUtils;

import net.dv8tion.jda.api.entities.Member;
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

	public static void executeMute(final TextChannel commandChannel, final Member author, final String authorMention,
			final List<String> userIdsToMute, final Instant muteEndDate, final String reason) {
		try {
			for (final String userId : userIdsToMute) {

				final Long id = Long.valueOf(userId);

				if (!cdsData.isMuted(id)) {
					try {
						commandChannel.getGuild().retrieveMemberById(userId).queue(member -> {
							final MutedUser mutedUser = new MutedUser();
							mutedUser.setStartDate(Instant.now());
							mutedUser.setEndDate(muteEndDate);
							mutedUser.setModeratorUserId(author.getIdLong());
							mutedUser.setModeratorDiscordTag(author.getUser().getAsTag());
							mutedUser.setMutedUserId(id);
							mutedUser.setMutedUserDiscordTag(commandChannel.getGuild().retrieveMemberById(userId)
									.complete().getUser().getAsTag());
							mutedUser.setMuteReason(reason);

							commandChannel.getGuild()
									.addRoleToMember(id,
											RoleUtils.getRoleById(commandChannel.getGuild(), RoleUtils.ROLE_MUTED))
									.queue(success -> {
										cdsData.insertMutedUser(mutedUser);
										log.info("Successfully muted user {} (mute executed by {})",
												mutedUser.getMutedUserDiscordTag(), mutedUser.getModeratorDiscordTag());
										commandChannel.sendMessage((new StringBuilder().append(authorMention)
												.append(" Successfully muted ")
												.append(mutedUser.getMutedUserDiscordTag()).append(" (").append(userId)
												.append(")\nThe mute will be lifted automatically on ")
												.append(mutedUser.getEndDate().toString())
												.append(" (UTC)\n*To unmute this user manually, use rdss:unmute (see rdss:? for help).*")))
												.queue();
									});

						}, failure -> {
							log.error("Rest Error -- User {} could not be received for mute.", userId);
							commandChannel.sendMessage(new StringBuilder().append(authorMention)
									.append(" Could not mute user ").append(userId)).queue();
						});
					} catch (final ErrorResponseException e) {
						commandChannel.sendMessage(
								new StringBuilder().append(authorMention).append(" User ").append(userId).append(
										" could not be muted (User ID not resolvable or possible internal error)."))
								.queue();
					}
				} else {
					commandChannel.sendMessage(new StringBuilder().append(authorMention).append(" User ").append(userId)
							.append(" is already muted.")).queue();
				}

			}
		} catch (final NumberFormatException e) {
			log.error("Mute command NumberFormatException: {}", e.getMessage());
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append(" Command parameters incorrect. For more information, see rdss:?")).queue();
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
										RoleUtils.getRoleById(commandChannel.getGuild(), RoleUtils.ROLE_MUTED))
								.queue(success -> {
									cdsData.removeMutedUser(id);
									log.info("Successfully unmuted user {} (mute executed by {})", id, authorMention);
									commandChannel.sendMessage(new StringBuilder().append(authorMention)
											.append(" Successfully unmuted user ").append(userId)).queue();
								});
					}, failure -> {
						log.error("Rest Error -- User {} could not be received for unmute.", userId);
						commandChannel.sendMessage(new StringBuilder().append(authorMention)
								.append(" Could not unmute user ").append(userId)).queue();
					});
				} catch (final ErrorResponseException e) {
					commandChannel.sendMessage(new StringBuilder().append(authorMention).append("User ").append(userId)
							.append(" could not be unmuted (User ID not resolvable or possible internal error)."))
							.queue();
				}
			}
		} catch (final NumberFormatException e) {
			log.error("Unmute command NumberFormatException: {}", e.getMessage());
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append(" Command parameters incorrect. For more information, see rdss:?")).queue();
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
									.append(" Successfully banned user ").append(bannedUser.getBannedUserDiscordTag())
									.append(" (").append(bannedUser.getBannedUserId()).append(")")).queue();
						});

					}, failure -> {
						log.error("Rest Error -- User {} could not be received for ban.", userId);
						commandChannel.sendMessage(
								new StringBuilder().append(authorMention).append(" Could not ban user ").append(userId))
								.queue();
					});
				} catch (final ErrorResponseException e) {
					commandChannel
							.sendMessage(new StringBuilder().append(authorMention)
									.append("This user could not be banned (User ID not resolvable): ").append(userId))
							.queue();
				}
			}
		} catch (final NumberFormatException e) {
			log.error("Ban command NumberFormatException: {}", e.getMessage());
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append(" Command parameters incorrect. For more information, see rdss:?")).queue();
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
						}, failure -> {
							log.error("Rest Error -- User {} could not be received for unban.", userId);
							commandChannel.sendMessage(new StringBuilder().append(authorMention)
									.append(" Could not unban user ").append(userId)).queue();
						});
					});
				} catch (final ErrorResponseException e) {
					commandChannel.sendMessage(new StringBuilder().append(authorMention)
							.append("This user could not be unbanned (User ID not resolvable): ").append(userId))
							.queue();
				}
			}
		} catch (final NumberFormatException e) {
			log.error("Unban command NumberFormatException: {}", e.getMessage());
			commandChannel.sendMessage(new StringBuilder().append(authorMention)
					.append(" Command parameters incorrect. For more information, see rdss:?")).queue();
		}
	}

}
