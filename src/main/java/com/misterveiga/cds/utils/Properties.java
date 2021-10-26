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
	public static final String TELEGRAM_CHAT_ID = "0";

	/** The Constant GUILD_ROBLOX_DISCORD_ID. */
	public static final Long GUILD_ROBLOX_DISCORD_ID = 864117798066585650L;

	/** The Constant ROLE_COMMUNITY_SUPERVISOR_ID. */
	public static final Long ROLE_COMMUNITY_SUPERVISOR_ID = 864249787503083570L;

	/** The Constant CHANNEL_COMMANDS_ID. */
	public static final Long CHANNEL_COMMANDS_ID = 864250679485136907L;

	/** The Constant CHANNEL_SUPERVISORS_ID. */
	public static final Long CHANNEL_SUPERVISORS_ID = 864250700040503306L;

	/** The Constant CHANNEL_BAN_REQUESTS_QUEUE_ID. */
	public static final Long CHANNEL_BAN_REQUESTS_QUEUE_ID = 864250710061482015L;

	/** The Constant CHANNEL_CENSORED_AND_SPAM_LOGS_ID. */
	public static final Long CHANNEL_CENSORED_AND_SPAM_LOGS_ID = 864250720814891018L;

	public static final Long CHANNEL_MESSAGE_LOGS_ID = 864250730750803989L;

	/** The Constant CHANNEL_MOD_ALERTS_ID. */
	public static final Long CHANNEL_MOD_ALERTS_ID = 864250737689362433L;

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

	public Properties(){}

	public String getAlertModsCooldown(){return ALERT_MODS_COOLDOWN.toString();}
	public String getTelegramChatId(){return TELEGRAM_CHAT_ID;}
	public String getGuildRobloxDiscordId(){return GUILD_ROBLOX_DISCORD_ID.toString();}
	public String getRoleCommunitySupervisorId(){return ROLE_COMMUNITY_SUPERVISOR_ID.toString();}
	public String getChannelCommandsId(){return CHANNEL_COMMANDS_ID.toString();}
	public String getChannelSupervisorsId(){return CHANNEL_SUPERVISORS_ID.toString();}
	public String getChannelBanRequestsQueueId(){return CHANNEL_BAN_REQUESTS_QUEUE_ID.toString();}
	public String getChannelCensoredAndSpamLogsId(){return CHANNEL_CENSORED_AND_SPAM_LOGS_ID.toString();}
	public String getChannelMessageLogsId(){return CHANNEL_MESSAGE_LOGS_ID.toString();}
	public String getChannelModAlertsId(){return CHANNEL_MOD_ALERTS_ID.toString();}
	public String getTimeWaitOnlineSupervisorMonitoringDefault(){return TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT.toString();}
	public String getTimeWaitOnlineSupervisorMonitoring(){return TIME_WAIT_ONLINE_SUPERVISOR_MONITORING_DEFAULT.toString();}
}
