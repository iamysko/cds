/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.rdss.utils;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

/**
 * The Class RoleUtils.
 */
public class RoleUtils {

	/** The Constant ROLE_TRIAL_SUPERVISOR. */
	public static final String ROLE_TRIAL_SUPERVISOR = "Trial Supervisor";

	/** The Constant ROLE_COMMUNITY_SUPERVISOR. */
	public static final String ROLE_COMMUNITY_SUPERVISOR = "Community Supervisor";

	/** The Constant ROLE_SENIOR_COMMUNITY_SUPERVISOR. */
	public static final String ROLE_SENIOR_COMMUNITY_SUPERVISOR = "Senior Community Supervisor";

	/** The Constant ROLE_SERVER_MANAGER. */
	public static final String ROLE_SERVER_MANAGER = "Server Manager";

	/** The Constant ROLE_LEAD. */
	public static final String ROLE_LEAD = "Lead";

	/** The Constant ROLE_PUBLIC_SECTOR. */
	public static final String ROLE_PUBLIC_SECTOR = "Public Sector";

	/** The Constant ROLE_INFRASTRUCTURE. */
	public static final String ROLE_INFRASTRUCTURE = "Infrastructure";

	/**
	 * Find role.
	 *
	 * @param member the member
	 * @param name   the name
	 * @return the role
	 */
	public static Role findRole(final Member member, final String name) {
		final List<Role> roles = member.getRoles();
		return roles.stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
	}

}
