/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

/**
 * The Class Properties.
 */
public class Properties {

	/** The Constant ALERT_MODS_COOLDOWN. */
	public static final Long ALERT_MODS_COOLDOWN = 30L; // Seconds

	/** The Constant TELEGRAM_CHAT_ID. */
	public static final String TELEGRAM_CHAT_ID = "-1001446733742";

	/** The Constant GUILD_ROBLOX_DISCORD_ID. */
	public static final Long GUILD_ROBLOX_DISCORD_ID = 150074202727251969L;

	/** The Constant ROLE_COMMUNITY_SUPERVISOR_ID. */
	public static final Long ROLE_COMMUNITY_SUPERVISOR_ID = 150093661231775744L;

	/** The Constant CHANNEL_COMMANDS_ID. */
	public static final Long CHANNEL_COMMANDS_ID = 150250250471342080L;

	/** The Constant CHANNEL_SUPERVISORS_ID. */
	public static final Long CHANNEL_SUPERVISORS_ID = 150255535927721984L;

	/** The Constant CHANNEL_BAN_REQUESTS_QUEUE_ID. */
	public static final Long CHANNEL_BAN_REQUESTS_QUEUE_ID = 592580861543841802L;

	/** The Constant CHANNEL_CENSORED_AND_SPAM_LOGS_ID. */
	public static final Long CHANNEL_CENSORED_AND_SPAM_LOGS_ID = 366624802024325120L;

	/** The Constant CHANNEL_MOD_ALERTS_ID. */
	public static final Long CHANNEL_MOD_ALERTS_ID = 785821764839669791L;

	/** The Constant TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT. */
	public static final Long TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT = 3600000L;

	/** The time wait online supervisor monitoring. */
	public static Long timeWaitOnlineSupervisorMonitoring = TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT;

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
