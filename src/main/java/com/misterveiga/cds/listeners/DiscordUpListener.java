/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.listeners;

import java.time.Instant;

import com.misterveiga.cds.telegram.TelegramService;
import com.misterveiga.cds.utils.Properties;

import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener interface for receiving discordUp events. The class that is
 * interested in processing a discordUp event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addDiscordUpListener<code> method. When the discordUp event
 * occurs, that object's appropriate method is invoked.
 *
 * @see DiscordUpEvent
 */
public class DiscordUpListener extends ListenerAdapter {

	/**
	 * On guild available.
	 *
	 * @param event the event
	 */
	@Override
	public void onGuildAvailable(final GuildAvailableEvent event) {
		TelegramService.sendToTelegram(Instant.now(), TelegramService.DISCORD_UP);
		event.getGuild().getTextChannelById(Properties.CHANNEL_SUPERVISORS_ID).sendMessage(
				"The Roblox Discord is recovering from downtime. Please be aware of rule-breakers as automated moderation services might continue to be down.");
	}

}
