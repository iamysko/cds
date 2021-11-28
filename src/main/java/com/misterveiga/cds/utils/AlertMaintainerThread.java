package com.misterveiga.cds.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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

		log.debug("[AlertMaintainerThread] Checking for old alerts...");

		final Guild guild = jda.getGuildById(Properties.GUILD_ROBLOX_DISCORD_ID);

		guild.getTextChannelById(Properties.CHANNEL_MOD_ALERTS_ID).getHistoryFromBeginning(1).queue(messageHistory -> {
			if(!messageHistory.isEmpty()) {
			final Message firstMessage = messageHistory.getRetrievedHistory().get(0);
			final OffsetDateTime firstMessageDateTime = firstMessage.getTimeCreated();
			final ZoneOffset firstMessageZone = firstMessageDateTime.getOffset();
			
			if (firstMessageDateTime.isBefore(OffsetDateTime.now(firstMessageZone).minusHours(2L))) {
				EmbedBuilder embed = EmbedBuilds.alertMaintainerEmbed();
				
				log.info("[AlertMaintainerThread] Alerts over 2 hours old found. Notifying the team...");
				guild.getTextChannelById(Properties.CHANNEL_TRIAL_MODERATORS_ID).sendMessage("@here Pending moderation alerts").setEmbeds(embed.build()).queue();
			}
			}
		});
	}
}
