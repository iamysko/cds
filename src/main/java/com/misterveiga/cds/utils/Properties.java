/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The Class Properties.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class Properties {

	/** The Constant TELEGRAM_CHAT_ID. */

	public static String TELEGRAM_CHAT_ID;

	/** The Constant GUILD_ROBLOX_DISCORD_ID. */

	public static Long GUILD_ROBLOX_DISCORD_ID;

	/** The Constant ROLE_COMMUNITY_SUPERVISOR_ID. */

	public static Long ROLE_COMMUNITY_SUPERVISOR_ID;

	/** The Constant CHANNEL_COMMANDS_ID. */

	public static Long CHANNEL_COMMANDS_ID;

	/** The Constant CHANNEL_SUPERVISORS_ID. */

	public static Long CHANNEL_SUPERVISORS_ID;

	/** The Constant CHANNEL_BAN_REQUESTS_QUEUE_ID. */

	public static Long CHANNEL_BAN_REQUESTS_QUEUE_ID;

	/** The Constant TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT. */

	public static Long TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT;

	/** The time wait online supervisor monitoring. */
	public static Long timeWaitOnlineSupervisorMonitoring = 3600000L;

	/**
	 * Sets the time wait online supervisor monitoring.
	 *
	 * @param newTimeWaitOnlineSupervisorMonitoring the new time wait online
	 *                                              supervisor monitoring
	 */
	public static void setTimeWaitOnlineSupervisorMonitoring(final Long newTimeWaitOnlineSupervisorMonitoring) {
		timeWaitOnlineSupervisorMonitoring = newTimeWaitOnlineSupervisorMonitoring;
	}

	@Value("${properties.id.telegram}")
	public void setTelegramChatId(final String telegramChatId) {
		TELEGRAM_CHAT_ID = telegramChatId;
	}

	@Value("${properties.id.robloxdiscord}")
	public void setRobloxDiscordId(final Long id) {
		GUILD_ROBLOX_DISCORD_ID = id;
	}

	@Value("${properties.id.role_supervisor}")
	public void setCommunitySupervisorId(final Long id) {
		ROLE_COMMUNITY_SUPERVISOR_ID = id;
	}

	@Value("${properties.id.channel_commands}")
	public void setChannelCommandsId(final Long id) {
		CHANNEL_COMMANDS_ID = id;
	}

	@Value("${properties.id.channel_supervisors}")
	public void setChannelSupervisorsId(final Long id) {
		CHANNEL_SUPERVISORS_ID = id;
	}

	@Value("${properties.id.ban_request}")
	public void setChannelBanRequestsQueueId(final Long id) {
		CHANNEL_BAN_REQUESTS_QUEUE_ID = id;
	}

	@Value("${properties.time_wait}")
	public void setSupervisorMonitoringWait(final Long time) {
		TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT = time;
	}

}
