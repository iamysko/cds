package com.misterveiga.cds.utils;

public class RegexConstants {

	public static final String GENERIC = "^rdss:.*$";

	public static final String COMMAND_HELP = "^rdss:help$";

	public static final String COMMAND_HELP_ALT = "^rdss:\\?$";

	public static final String COMMAND_ABOUT = "^rdss:about$";

	public static final String COMMAND_SET_COVERAGE_CHECK_TIMER = "^rdss:set_coverage_timer \\d{1,4}$";

	public static final String COMMAND_BAN = "^rdss:ban\\s.*$";

	public static final String COMMAND_UNBAN = "^rdss:unban\\s.*$";

	public static final String COMMAND_WARN = "^rdss:warn\\s.*$";

	public static final String COMMAND_MUTE = "^rdss:mute\\s.*$";

	public static final String COMMAND_UNMUTE = "^rdss:unmute\\s.*$";

	public static final String SHOW_BANNED_USERS = "^rdss:show_bans$";

	public static final String SHOW_MUTED_USERS = "^rdss:show_mutes$";

}
