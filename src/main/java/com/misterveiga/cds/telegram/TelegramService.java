/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.telegram;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.misterveiga.cds.utils.Properties;

/**
 * The Class TelegramService.
 */
public class TelegramService {

	/** The telegram token. */
	public static String TELEGRAM_TOKEN;

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(TelegramService.class);

	/** The Constant CDS_START. */
	public static final String CDS_START = "CDS has restarted.%0AUpdates: ";

	/** The Constant CDS_END. */
	public static final String CDS_END = "CDS has disconnected from Discord.";

	/** The Constant DOWNTIME_ALERT. */
	public static final String DOWNTIME_ALERT = "**Downtime Alert** | The Roblox Discord is currently experiencing downtime.";

	/** The Constant DISCORD_UP. */
	public static final String DISCORD_UP = "**Status Update** | The Roblox Discord is back up!";

	/** The Constant ERROR_UNKNOWN. */
	public static final String ERROR_UNKNOWN = "Unknown error.";

	/** The Constant ERROR_WAIT_JDA. */
	public static final String ERROR_WAIT_JDA = "Error awaiting JDA.";

	public TelegramService(final String telegramToken) {
		TelegramService.TELEGRAM_TOKEN = telegramToken;
	}

	/**
	 * Send to telegram.
	 *
	 * @param instant the instant
	 * @param message the message
	 */
	public static void sendToTelegram(final Instant instant, final String message) {
		String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
		log.info(TelegramService.TELEGRAM_TOKEN);
		urlString = String.format(urlString, TelegramService.TELEGRAM_TOKEN, Properties.TELEGRAM_CHAT_ID, message);
		log.warn("Sending to Telegram: {}", message);

		try {
			final URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			try (final InputStream is = new BufferedInputStream(conn.getInputStream());) {
				// Do nothing.
			}
		} catch (final IOException e) {
			log.error(e.getMessage());
		}

	}

}
