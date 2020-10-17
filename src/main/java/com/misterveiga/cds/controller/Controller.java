/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.misterveiga.cds.listeners.DiscordDownListener;
import com.misterveiga.cds.listeners.DiscordUpListener;
import com.misterveiga.cds.listeners.MessageListener;
import com.misterveiga.cds.listeners.ReactionListener;

import net.dv8tion.jda.api.JDA;

import static com.misterveiga.cds.utils.ApiMessages.*;

/**
 * The Class Controller.
 */
@RestController
@RequestMapping("rdss")
public class Controller {

	/** The jda. */
	@Autowired
	JDA jda;

	/** The discord up listener. */
	@Autowired
	DiscordUpListener discordUpListener;

	/** The discord down listener. */
	@Autowired
	DiscordDownListener discordDownListener;

	/** The message listener. */
	@Autowired
	MessageListener messageListener;

	/** The reaction listener. */
	@Autowired
	ReactionListener reactionListener;

	/**
	 * Ok.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String ok() {
		return OK;
	}

	/**
	 * Disable discord alerts.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/disable/discord-uptime-alerts", method = RequestMethod.GET)
	public @ResponseBody String disableDiscordAlerts() {
		if (jda.getRegisteredListeners().contains(discordUpListener)
				&& jda.getRegisteredListeners().contains(discordDownListener)) {
			jda.removeEventListener(discordUpListener, discordDownListener);
			return DISCORD_UP_DOWN_ALERTS_DISABLED;
		}
		return DISCORD_UP_DOWN_ALERTS_ALREADY_DISABLED;
	}

	/**
	 * Enable discord alerts.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/enable/discord-uptime-alerts", method = RequestMethod.GET)
	public @ResponseBody String enableDiscordAlerts() {
		if (!jda.getRegisteredListeners().contains(discordUpListener)
				&& !jda.getRegisteredListeners().contains(discordDownListener)) {
			jda.addEventListener(discordUpListener, discordDownListener);
			return DISCORD_UP_DOWN_ALERTS_ENABLED;
		}
		return DISCORD_UP_DOWN_ALERTS_ALREADY_ENABLED;
	}

	/**
	 * Enable message listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/enable/message-listener", method = RequestMethod.GET)
	public @ResponseBody String enableMessageListener() {
		if (!jda.getRegisteredListeners().contains(messageListener)) {
			jda.addEventListener(messageListener);
			return MESSAGE_LISTENER_ENABLED;
		}
		return MESSAGE_LISTENER_ALREADY_ENABLED;
	}

	/**
	 * Disable message listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/disable/message-listener", method = RequestMethod.GET)
	public @ResponseBody String disableMessageListener() {
		if (jda.getRegisteredListeners().contains(messageListener)) {
			jda.removeEventListener(messageListener);
			return MESSAGE_LISTENER_DISABLED;
		}
		return MESSAGE_LISTENER_ALREADY_DISABLED;
	}

	/**
	 * Enable reaction listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/enable/reaction-listener", method = RequestMethod.GET)
	public @ResponseBody String enableReactionListener() {
		if (!jda.getRegisteredListeners().contains(reactionListener)) {
			jda.addEventListener(reactionListener);
			return REACTION_LISTENER_ENABLED;
		}
		return REACTION_LISTENER_ALREADY_ENABLED;
	}

	/**
	 * Disable reaction listener.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/disable/reaction-listener", method = RequestMethod.GET)
	public @ResponseBody String disableReactionListener() {
		if (jda.getRegisteredListeners().contains(reactionListener)) {
			jda.removeEventListener(reactionListener);
			return REACTION_LISTENER_DISABLED;
		}
		return REACTION_LISTENER_ALREADY_DISABLED;
	}

}
