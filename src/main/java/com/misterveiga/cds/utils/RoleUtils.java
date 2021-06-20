/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

/**
 * The Class RoleUtils.
 */
public class RoleUtils {

	/** The Constant ROLE_TRIAL_SUPERVISOR. */
	public static final long ROLE_TRIAL_SUPERVISOR = 218513797659230209L;

	/** The Constant ROLE_COMMUNITY_SUPERVISOR. */
	public static final long ROLE_COMMUNITY_SUPERVISOR = 150093661231775744L;

	/** The Constant ROLE_SENIOR_COMMUNITY_SUPERVISOR. */
	public static final long ROLE_SENIOR_COMMUNITY_SUPERVISOR = 234520161720205312L;

	/** The Constant ROLE_SERVER_MANAGER. */
	public static final long ROLE_SERVER_MANAGER = 150074509393788929L;

	/** The Constant ROLE_LEAD. */
	public static final long ROLE_LEAD = 310871622150258698L;

	/** The Constant ROLE_PUBLIC_SECTOR. */
	public static final long ROLE_PUBLIC_SECTOR = 452841584749445131L;

	/** The Constant ROLE_INFRASTRUCTURE. */
	public static final long ROLE_INFRASTRUCTURE = 366311313875533824L;

	/** The Constant ROLE_BOT. */
	public static final long ROLE_BOT = 150075195971862528L;

	public static final long ROLE_MUTED = 320659703992680448L;

	/**
	 * Find a role owned by a member by role name.
	 *
	 * @param member The member to search
	 * @param name   The name of the role
	 * @return The role if the member has it, otherwise null
	 */
	public static Role findRole(final Member member, final String name) {

		if (member != null) {
			final List<Role> roles = member.getRoles();

			return roles.stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
		}

		return null;

	}

	/**
	 * Find a role owned by a member by role ID.
	 *
	 * @param member The member to search
	 * @param roleId   The ID of the role
	 * @return The role if the member has it, otherwise null
	 */	 
	public static Role findRole(final Member member, final long roleId) {
		if (member != null) {
			final List<Role> roles = member.getRoles();
			return roles.stream().filter(role -> role.getIdLong() == roleId).findFirst().orElse(null);
		}
		return null;
	}
	/**	
	 * Find whether a member has any of the roles passed in.
	 *
	 * @param member The member to search
	 * @param roles The array of Role Names
	 * @return True if a member has any of the roles, otherwise false
	 */	 
	public static boolean isAnyRole(final Member member, final String... roles) {

		for (final String role : roles) {

			if (findRole(member, role) != null) {

				return true;

			}

		}

		return false;

	}

	/**
	 * Find whether a member has any of the roles passed in.
	 *
	 * @param member The member to search
	 * @param roles The varargs array of Role IDs
	 * @return True if a member has any of the roles, otherwise false
	 */	 
	public static boolean isAnyRole(final Member member, final long... roles) {
		for (final long role : roles) {
			if (findRole(member, role) != null) {
				return true;
			}
		}
		return false;
	}

	public static Role getRoleByName(final Guild guild, final String name) {

		if (guild != null) {
			// final List<Role> roles = guild.getRoles();

			return guild.getRolesByName(name, true).get(0) != null ? guild.getRolesByName(name, true).get(0) : null;

			// return roles.stream().filter(role ->
			// role.getName().equals(name)).findFirst().orElse(null);
		}

		return null;

	}

	public static Role getRoleById(final Guild guild, final long roleId) {
		if (guild != null) {
			return guild.getRoleById(roleId);
		}
		return null;
	}

}
