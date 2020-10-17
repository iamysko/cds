/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.misterveiga.cds.utils.enums.CDSRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import static com.misterveiga.cds.utils.enums.CDSRole.*;

/**
 * The Class RoleUtils.
 */
public class RoleUtils {

	public static final Map<CDSRole, Integer> roleMap = Stream.of(new Object[][] {
			{ ROLE_LEAD, -1 },
			{ ROLE_PUBLIC_SECTOR, -1 },
			{ ROLE_INFRASTRUCTURE, -1 },
			{ ROLE_TRIAL_SUPERVISOR, 0 },
			{ ROLE_COMMUNITY_SUPERVISOR, 1 },
			{ ROLE_SENIOR_COMMUNITY_SUPERVISOR, 2 },
			{ ROLE_SERVER_MANAGER, 3 }
	}).collect(Collectors.toMap(data -> (CDSRole) data[0], data -> (Integer) data[1]));


	/**
	 * Find role.
	 *
	 * @param member the member
	 * @param name   the name
	 * @return the role
	 */
	public static Role findRole(final Member member, final CDSRole name) {

		if (member != null) {
			final List<Role> roles = member.getRoles();

			return roles.stream().filter(role -> role.getName().equals(extractRoleName(name))).findFirst().orElse(null);
		}

		return null;

	}

	public static boolean isAnyRole(final Member member, final CDSRole... roles) {

		for (final CDSRole role : roles) {

			if (findRole(member, (role)) != null) {

				return true;

			}

		}

		return false;

	}

	/**
	 * Used to get the name out of a role, Since there's no
	 * role repository yet, an Enum can be used, we can convert
	 * a role to string by splitting on underscores and capitalize
	 * first letter of each word.
	 * @param role Role
	 * @return role name
	 */
	public static String extractRoleName(CDSRole role){
		return Stream.of(role.toString().replace("ROLE_","").split("_")).map(word -> (word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()))
				.collect(Collectors.joining());
	}

}
