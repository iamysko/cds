package com.misterveiga.cds.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.misterveiga.cds.data.CdsDataImpl;

@Component
public class StringBuilds {
	
	/** The app name. */
	@Value("${cds.name}")
	public static String appName;

	/** The app version. */
	@Value("${cds.version}")
	public static String appVersion;

	/** The cds data. */
	@Autowired
	public static CdsDataImpl cdsData;

	/**
	 * On message received.
	 *
	 * @param event the event
	 */

	
	/**
	 * Send help message.
	 *
	 * @param message       the message
	 * @param authorMention the author mention
	 */
	public static String getHelpMessage(final String authorMention) {
		return new StringBuilder().append(authorMention).append(" **Roblox Discord Services | Help**")
				.append("\nPrefix for all commands: `rdss:<command>`")
				.append("\nIf a command doesn't work for you, you may not have permission to run it.")
				.append("\nHelp: \"rdss:help\" or \"rdss:?\"")
				.append("\nWarn user(s): \"rdss:warn user1,user2,userN warning message\"")
				.append("\nMute user(s): \"rdss:mute user1,user2,userN XdXhXm reason\"")
				.append("\nUnmute user(s): \"rdss:unmute user1,user2,userN\"")
				.append("\nBan user(s): \"rdss:ban user1,user2,userN reason (reason is optional)\"")
				.append("\nUnban user(s): \"rdss:unban user1,user2,userN\"").toString();
	}

	/**
	 * Send about message.
	 *
	 * @param message       the message
	 * @param authorMention the author mention
	 */
	public static String getAboutMessage(final String authorMention) {
		return new StringBuilder().append(authorMention).append(" **Community Discord Services | About**")
				.append("\nApplication: ").append(appName).append("\nVersion: ").append(appVersion)
				.append("\n*Collaborate: https://github.com/misterveiga/cds*").toString();
	}

}
