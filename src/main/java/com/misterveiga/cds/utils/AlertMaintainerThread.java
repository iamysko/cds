package com.misterveiga.cds.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsData;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

@Component
public class AlertMaintainerThread {

	@Autowired
	JDA jda;

	@Autowired
	CdsData cdsData;

	Logger log = LoggerFactory.getLogger(AlertMaintainerThread.class);

	public AlertMaintainerThread() {

	}

	@Scheduled(fixedDelay = 3600000)
	public void checkMutes() {

		log.debug("[AlertMaintainerThread] Checking for old ban requests and moderation alerts...");
		final Guild guild = jda.getGuildById(Properties.GUILD_ROBLOX_DISCORD_ID);

		// Alert the team if any moderation alerts are 2+ hours old
		guild.getTextChannelById(Properties.CHANNEL_MOD_ALERTS_ID).getHistoryFromBeginning(1).queue(messageHistory -> {
			if(!messageHistory.isEmpty()) {
			final Message firstMessage = messageHistory.getRetrievedHistory().get(0);
			final OffsetDateTime firstMessageDateTime = firstMessage.getTimeCreated();
			final ZoneOffset firstMessageZone = firstMessageDateTime.getOffset();
			
			if (firstMessageDateTime.isBefore(OffsetDateTime.now(firstMessageZone).minusHours(2L))) {
        			EmbedBuilder embed = EmbedBuilds.alertMaintainerEmbed();

				log.info("[AlertMaintainerThread] Alerts over 2 hours old found. Notifying the team...");
				guild.getTextChannelById(Properties.CHANNEL_MODERATORS_ID).sendMessage("@here Pending moderation alerts").setEmbeds(embed.build()).queue();
			}
			}
		});
		
		// Alert senior moderators if there are 25+ unreviewed ban requests
		guild.getTextChannelById(Properties.CHANNEL_BAN_REQUESTS_QUEUE_ID).getIterableHistory().takeAsync(200).thenAccept(messages -> {
				final List<Message> banRequests = messages.stream()
						.filter(message -> message.getReactions().isEmpty() && message.getContentRaw().matches("(?i)^;(force)?ban.*$"))
						.collect(Collectors.toList());

				if (banRequests.size() >= 25) {
					StringBuilder sb = new StringBuilder()
						.append(String.format("<@&%d> There are ", RoleUtils.ROLE_SENIOR_MODERATOR))
						.append(String.format("%d unreviewed ban requests. ", banRequests.size()))
						.append(String.format("Starting from this message:%n<%s>", banRequests.get(banRequests.size() - 1).getJumpUrl()));
					
					log.info("[AlertMaintainerThread] 25+ Ban requests found. Notifying senior moderators...");
					guild.getTextChannelById(Properties.CHANNEL_SENIOR_MODERATORS_ID).sendMessage(sb.toString()).queue();
				}
		});
		
	}
}
