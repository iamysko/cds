/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.data;

import java.util.List;

import com.misterveiga.cds.entities.Action;
import com.misterveiga.cds.entities.BannedUser;
import com.misterveiga.cds.entities.MutedUser;

/**
 * The Interface CdsData.
 */
public interface CdsData {

	/**
	 * Insert action.
	 *
	 * @param commandAction the command action
	 */
	public void insertAction(final Action commandAction);

	/**
	 * Insert banned user.
	 *
	 * @param bannedUser the banned user
	 */
	public void insertBannedUser(final BannedUser bannedUser);

	/**
	 * Removes the banned user.
	 *
	 * @param userId the user id
	 */
	public void removeBannedUser(final Long userId);

	/**
	 * Insert muted user.
	 *
	 * @param mutedUser the muted user
	 */
	public void insertMutedUser(final MutedUser mutedUser);

	/**
	 * Removes the muted user.
	 *
	 * @param userId the user id
	 */
	public void removeMutedUser(final Long userId);

	/**
	 * Gets the muted user.
	 *
	 * @param userId the user id
	 * @return the muted user
	 */
	public MutedUser getMutedUser(final Long userId);

	/**
	 * Gets the banned user.
	 *
	 * @param userId the user id
	 * @return the banned user
	 */
	public BannedUser getBannedUser(final Long userId);

	/**
	 * Gets the muted users.
	 *
	 * @return the muted users
	 */
	public List<MutedUser> getMutedUsers();

	/**
	 * Gets the banned users.
	 *
	 * @return the banned users
	 */
	public List<BannedUser> getBannedUsers();

	/**
	 * Checks if is muted.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	public Boolean isMuted(final Long userId);

	/**
	 * Checks if is banned.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	public Boolean isBanned(final Long userId);

}
