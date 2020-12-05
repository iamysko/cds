package com.misterveiga.cds.utils;

public class RegexConstants {

	public static final String GENERIC = "^-.*$";

	public static final String COMMAND_HELP = "^-help$";

	public static final String COMMAND_HELP_ALT = "^-\\?$";

	public static final String COMMAND_ABOUT = "^-about$";

	public static final String COMMAND_SET_COVERAGE_CHECK_TIMER = "^-set_coverage_timer \\d{1,4}$";

	public static final String COMMAND_BAN = "^-b\\s.*$";

	public static final String COMMAND_UNBAN = "^-ub\\s.*$";

	public static final String COMMAND_WARN = "^-w\\s.*$";

	public static final String COMMAND_MUTE = "^-m\\s.*$";

	public static final String SHOW_BANNED_USERS = "^-show_bans$";

}
