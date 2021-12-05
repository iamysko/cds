/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

/**
 * The Class Properties.
 */
public class Properties {

	/** The Constant ALERT_MODS_COOLDOWN. */
	public static final Long ALERT_MODS_COOLDOWN = 60L; // Seconds

	/** The Constant TELEGRAM_CHAT_ID. */
	public static final String TELEGRAM_CHAT_ID = "-759697626";

	/** The Constant GUILD_ROBLOX_DISCORD_ID. */
	public static final Long GUILD_ROBLOX_DISCORD_ID = 150074202727251969L;

	/** The Constant ROLE_MODERATOR_ID. */
	public static final Long ROLE_MODERATOR_ID = 150093661231775744L;

	/** The Constant CHANNEL_COMMANDS_ID. */
	public static final Long CHANNEL_COMMANDS_ID = 150250250471342080L;

	/** The Constant CHANNEL_TRIAL_MODERATORS_ID. */
	public static final Long CHANNEL_TRIAL_MODERATORS_ID = 678671353473269799L;
	
	/** The Constant CHANNEL_MODERATORS_ID. */
	public static final Long CHANNEL_MODERATORS_ID = 150255535927721984L;
	
	/** The Constant CHANNEL_SENIOR_MODERATORS_ID. */
	public static final Long CHANNEL_SENIOR_MODERATORS_ID = 678849276402466849L;

	/** The Constant CHANNEL_BAN_REQUESTS_QUEUE_ID. */
	public static final Long CHANNEL_BAN_REQUESTS_QUEUE_ID = 592580861543841802L;

	/** The Constant CHANNEL_CENSORED_AND_SPAM_LOGS_ID. */
	public static final Long CHANNEL_CENSORED_AND_SPAM_LOGS_ID = 366624802024325120L;

	public static final Long CHANNEL_MESSAGE_LOGS_ID = 366624514651717663L;

	/** The Constant CHANNEL_MOD_ALERTS_ID. */
	public static final Long CHANNEL_MOD_ALERTS_ID = 785821764839669791L;

	/** The Constant TIME_WAIT_ONLINE_MODERATOR_MONITORING_DEFAULT. */
	public static final Long TIME_WAIT_ONLINE_MODERATOR_MONITORING_DEFAULT = 3600000L;

	/** The time wait online moderator monitoring. */
	public static Long timeWaitOnlineModeratorMonitoring = TIME_WAIT_ONLINE_MODERATOR_MONITORING_DEFAULT;

	/**
	 * Sets the time wait online moderator monitoring.
	 *
	 * @param newTimeWaitOnlineModeratorMonitoring the new time wait online
	 *                                              moderator monitoring
	 */
	public static void setTimeWaitOnlineModeratorMonitoring(final Long newTimeWaitOnlineModeratorMonitoring) {
		timeWaitOnlineModeratorMonitoring = newTimeWaitOnlineModeratorMonitoring;
	}

}
