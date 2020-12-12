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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

import com.misterveiga.cds.utils.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class TelegramService.
 */
@PropertySource("classpath:application.properties")
public class TelegramService {

	/** The dev note. */
	public static String DEV_NOTE;

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

	/**
	 * Instantiates a new telegram service.
	 *
	 * @param telegramToken the telegram token
	 * @param devNote       the dev note
	 */
	public TelegramService(final String telegramToken, final String devNote) {
		TelegramService.TELEGRAM_TOKEN = telegramToken;
		TelegramService.DEV_NOTE = devNote;
	}

	/**
	 * Send to telegram.
	 *
	 * @param instant the instant
	 * @param message the message
	 */
	public static void sendToTelegram(final Instant instant, final String message) {
		String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
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

	/**
	 * On start.
	 */
	@PostConstruct
	public void onStart() {
		// TelegramService.sendToTelegram(Instant.now(), TelegramService.CDS_START +
		// DEV_NOTE);
	}

}
