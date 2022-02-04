/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.listeners;

import org.springframework.stereotype.Component;

import com.misterveiga.cds.utils.EmbedBuilds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
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
public class ButtonClickListener extends ListenerAdapter {

	@Override
	public void onButtonClick(ButtonClickEvent event) {

		if (event.getComponentId().contains("RobloxInformation")) {
			String[] ComponentId = event.getComponentId().split("/");
			try {
				EmbedBuilder embed = EmbedBuilds.getRobloxUserInfoEmbed(ComponentId[1], ComponentId[2]);
				event.replyEmbeds(embed.build()).queue();
			} catch (Exception e) {
				event.reply("It appears the Roblox API is currently not responding! Please Try again later! :(" + e)
						.queue();
			}
		} else if (event.getComponentId().equals("DeleteMessage")) {
			event.getMessage().delete().queue();
		}

	}
}
