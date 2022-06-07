/*
 * Author: {Noah}
 */
package com.misterveiga.cds.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.utils.EmbedBuilds;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener interface for receiving message events. The class that is
 * interested in processing a message event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addMessageListener<code> method. When the message event
 * occurs, that object's appropriate method is invoked.
 *
 * @see VoiceChannelEvent
 */
@Component
public class VoiceChannelListener extends ListenerAdapter {

	/** The Constant ROLE_MODERATOR_ID. */
	public static final Long CHANNEL_VOICE_LOGS = 366624876544655360L;

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(VoiceChannelListener.class);

	@Override
	public void onGuildVoiceJoin(final GuildVoiceJoinEvent event) {
		final TextChannel logsChannel = event.getGuild().getTextChannelById(VoiceChannelListener.CHANNEL_VOICE_LOGS);

		logsChannel.sendMessageEmbeds(EmbedBuilds.getUserJoinedVCEmbed(event).build()).queue();
	}
	
	@Override
	public void onGuildVoiceMove(final GuildVoiceMoveEvent event) {
		final TextChannel logsChannel = event.getGuild().getTextChannelById(983022264696078366L);

		logsChannel.sendMessageEmbeds(EmbedBuilds.getUserMovedVCEmbed(event).build()).queue();
	}
	
	@Override
	public void onGuildVoiceLeave(final GuildVoiceLeaveEvent event) {
		final TextChannel logsChannel = event.getGuild().getTextChannelById(983022264696078366L);

		logsChannel.sendMessageEmbeds(EmbedBuilds.getUserLeftVCEmbed(event).build()).queue();
	}

}
