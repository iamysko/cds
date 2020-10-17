/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.telegram;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.utils.Properties;

/**
 * The Class TelegramService.
 */
@Component
@PropertySource("classpath:application.properties")
public class TelegramService {

	/** The telegram token. */
	@Value("${telegram.token}")
	public static String telegramToken;

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(TelegramService.class);

	/** The Constant DOWNTIME_ALERT. */
	public static final String DOWNTIME_ALERT = "**Downtime Alert** | The Roblox Discord is currently experiencing downtime.";

	/** The Constant DISCORD_UP. */
	public static final String DISCORD_UP = "**Status Update** | The Roblox Discord is back up!";

	/** The Constant ERROR_UNKNOWN. */
	public static final String ERROR_UNKNOWN = "Unknown error.";

	/** The Constant ERROR_WAIT_JDA. */
	public static final String ERROR_WAIT_JDA = "Error awaiting JDA.";

	/**
	 * Send to telegram.
	 *
	 * @param instant the instant
	 * @param message the message
	 */
	public static void sendToTelegram(final Instant instant, final String message) {
		String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

		urlString = String.format(urlString, telegramToken, Properties.TELEGRAM_CHAT_ID, message);

		try {
			final URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			final StringBuilder sb = new StringBuilder();
			try (final InputStream is = new BufferedInputStream(conn.getInputStream());
					final BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
				String inputLine = "";
				while ((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}
				final String response = sb.toString();
				log.info("Received response to Telegram bot: ", response);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
