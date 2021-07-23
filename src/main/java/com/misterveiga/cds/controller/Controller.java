/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.misterveiga.cds.listeners.DiscordDownListener;
import com.misterveiga.cds.listeners.DiscordUpListener;
import com.misterveiga.cds.listeners.MessageListener;
import com.misterveiga.cds.listeners.ReactionListener;

import net.dv8tion.jda.api.JDA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
		return "OK";
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
			return "Discord uptime/downtime alerts have been disabled.";
		}
		return "Discord uptime/downtime alerts are already disabled.";
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
			return "Discord uptime/downtime alerts have been enabled.";
		}
		return "Discord uptime/downtime alerts are already enabled.";
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
			return "Message Listener has been enabled.";
		}
		return "Message Listener is already enabled.";
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
			return "Message Listener has been disabled.";
		}
		return "Message Listener is already disabled.";
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
			return "Reaction Listener has been enabled.";
		}
		return "Reaction Listener is already enabled.";
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
			return "Reaction Listener has been disabled.";
		}
		return "Reaction Listener is already disabled.";
	}

	@CrossOrigin
	@RequestMapping(value = "/panel/get-user-data", method = RequestMethod.GET)
	public @ResponseBody String getUserData() throws IOException {

		try {
			URL url = new URL("https://rickandmortyapi.com/api/location");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("GET");

			String line ="";

			int status = con.getResponseCode();

			InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder response = new StringBuilder();
			while ((line=bufferedReader.readLine()) != null){
				response.append(line);
			}
			bufferedReader.close();

			return response.toString();

		} catch (Exception e) {
			System.out.println("Error in making get request");

		}

		return "Done";
	}

}
