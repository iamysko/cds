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
	@Value("${properties.id.telegram}")
	public static String TELEGRAM_CHAT_ID;

	/** The Constant GUILD_ROBLOX_DISCORD_ID. */
	@Value("${properties.id.robloxdiscord}")
	public static Long GUILD_ROBLOX_DISCORD_ID;

	/** The Constant ROLE_COMMUNITY_SUPERVISOR_ID. */
	@Value("${properties.id.role_supervisor}")
	public static Long ROLE_COMMUNITY_SUPERVISOR_ID;

	/** The Constant CHANNEL_COMMANDS_ID. */
	@Value("${properties.id.channel_commands}")
	public static Long CHANNEL_COMMANDS_ID;

	/** The Constant CHANNEL_SUPERVISORS_ID. */
	@Value("${properties.id.channel_supervisors}")
	public static Long CHANNEL_SUPERVISORS_ID;

	/** The Constant CHANNEL_BAN_REQUESTS_QUEUE_ID. */
	@Value("${properties.id.ban_request}")
	public static Long CHANNEL_BAN_REQUESTS_QUEUE_ID;

	/** The Constant TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT. */
	@Value("${properties.time_wait}")
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

}
