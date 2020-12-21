package com.misterveiga.cds.utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsData;
import com.misterveiga.cds.entities.MutedUser;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

@Component
public class MuteMaintainerThread {

	@Autowired
	JDA jda;

	@Autowired
	CdsData cdsData;

	Logger log = LoggerFactory.getLogger(MuteMaintainerThread.class);

	public MuteMaintainerThread() {

	}

	@Scheduled(fixedDelay = 30000)
	public void checkMutes() {

		log.info("[MuteMaintainerThread] Checking for mutes to expire...");

		final List<MutedUser> mutedUsers = cdsData.getMutedUsers();
		log.info("[MuteMaintainerThread] Found {} muted users.", mutedUsers.size());

		if (mutedUsers.size() > 0) {
			final List<MutedUser> usersToUnmute = new ArrayList<>();
			for (final MutedUser user : mutedUsers) {
				if (Instant.now().isBefore(user.getEndDate())) {
					usersToUnmute.add(user);
				}
			}
			log.info("[MuteMaintainerThread] Found {} candidates for unmute.", usersToUnmute.size());

			for (final MutedUser user : usersToUnmute) {
				final Guild guild = jda.getGuildById(Properties.GUILD_ROBLOX_DISCORD_ID);
				guild.removeRoleFromMember(user.getMutedUserId(), RoleUtils.getRoleByName(guild, "Muted"))
						.queue(success -> {
							cdsData.removeMutedUser(user.getMutedUserId());
							log.info("[MuteMaintainerThread] Unmuted user {} ({})", user.getMutedUserDiscordTag(),
									user.getMutedUserId());
						});
			}
		}
	}

}
