/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.rdss.listeners;

import java.time.Instant;

import com.misterveiga.rdss.telegram.TelegramService;

import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The listener interface for receiving discordDown events. The class that is
 * interested in processing a discordDown event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addDiscordDownListener<code> method. When the discordDown
 * event occurs, that object's appropriate method is invoked.
 *
 * @see DiscordDownEvent
 */
public class DiscordDownListener extends ListenerAdapter {

	/**
	 * On guild unavailable.
	 *
	 * @param event the event
	 */
	@Override
	public void onGuildUnavailable(final GuildUnavailableEvent event) {
		TelegramService.sendToTelegram(Instant.now(), TelegramService.DOWNTIME_ALERT);
	}

}
