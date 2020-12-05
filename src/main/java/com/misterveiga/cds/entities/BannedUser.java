/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.entities;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class BannedUser.
 */
@Document(collection = "bans")
public class BannedUser implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4131100872635323220L;

	/** The moderator user id. */
	@JsonProperty
	private Long moderatorUserId;

	/** The moderator discord tag. */
	@JsonProperty
	private String moderatorDiscordTag;

	/** The banned user id. */
	@JsonProperty
	private Long bannedUserId;

	/** The banned user discord tag. */
	@JsonProperty
	private String bannedUserDiscordTag;

	/** The banned user reason. */
	@JsonProperty
	private String bannedUserReason;

	/** The date. */
	@JsonProperty
	private Date date;

	/**
	 * Gets the moderator user id.
	 *
	 * @return the moderator user id
	 */
	public Long getModeratorUserId() {
		return moderatorUserId;
	}

	/**
	 * Sets the moderator user id.
	 *
	 * @param moderatorUserId the new moderator user id
	 */
	public void setModeratorUserId(final Long moderatorUserId) {
		this.moderatorUserId = moderatorUserId;
	}

	/**
	 * Gets the moderator discord tag.
	 *
	 * @return the moderator discord tag
	 */
	public String getModeratorDiscordTag() {
		return moderatorDiscordTag;
	}

	/**
	 * Sets the moderator discord tag.
	 *
	 * @param moderatorDiscordTag the new moderator discord tag
	 */
	public void setModeratorDiscordTag(final String moderatorDiscordTag) {
		this.moderatorDiscordTag = moderatorDiscordTag;
	}

	/**
	 * Gets the banned user id.
	 *
	 * @return the banned user id
	 */
	public Long getBannedUserId() {
		return bannedUserId;
	}

	/**
	 * Sets the banned user id.
	 *
	 * @param bannedUserId the new banned user id
	 */
	public void setBannedUserId(final Long bannedUserId) {
		this.bannedUserId = bannedUserId;
	}

	/**
	 * Gets the banned user discord tag.
	 *
	 * @return the banned user discord tag
	 */
	public String getBannedUserDiscordTag() {
		return bannedUserDiscordTag;
	}

	/**
	 * Sets the banned user discord tag.
	 *
	 * @param bannedUserDiscordTag the new banned user discord tag
	 */
	public void setBannedUserDiscordTag(final String bannedUserDiscordTag) {
		this.bannedUserDiscordTag = bannedUserDiscordTag;
	}

	/**
	 * Gets the banned user reason.
	 *
	 * @return the banned user reason
	 */
	public String getBannedUserReason() {
		return bannedUserReason;
	}

	/**
	 * Sets the banned user reason.
	 *
	 * @param bannedUserReason the new banned user reason
	 */
	public void setBannedUserReason(final String bannedUserReason) {
		this.bannedUserReason = bannedUserReason;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bannedUserDiscordTag == null) ? 0 : bannedUserDiscordTag.hashCode());
		result = prime * result + ((bannedUserId == null) ? 0 : bannedUserId.hashCode());
		result = prime * result + ((bannedUserReason == null) ? 0 : bannedUserReason.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((moderatorDiscordTag == null) ? 0 : moderatorDiscordTag.hashCode());
		result = prime * result + ((moderatorUserId == null) ? 0 : moderatorUserId.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BannedUser other = (BannedUser) obj;
		if (bannedUserDiscordTag == null) {
			if (other.bannedUserDiscordTag != null) {
				return false;
			}
		} else if (!bannedUserDiscordTag.equals(other.bannedUserDiscordTag)) {
			return false;
		}
		if (bannedUserId == null) {
			if (other.bannedUserId != null) {
				return false;
			}
		} else if (!bannedUserId.equals(other.bannedUserId)) {
			return false;
		}
		if (bannedUserReason == null) {
			if (other.bannedUserReason != null) {
				return false;
			}
		} else if (!bannedUserReason.equals(other.bannedUserReason)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (moderatorDiscordTag == null) {
			if (other.moderatorDiscordTag != null) {
				return false;
			}
		} else if (!moderatorDiscordTag.equals(other.moderatorDiscordTag)) {
			return false;
		}
		if (moderatorUserId == null) {
			if (other.moderatorUserId != null) {
				return false;
			}
		} else if (!moderatorUserId.equals(other.moderatorUserId)) {
			return false;
		}
		return true;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BannedUser [moderatorUserId=");
		builder.append(moderatorUserId);
		builder.append(", moderatorDiscordTag=");
		builder.append(moderatorDiscordTag);
		builder.append(", bannedUserId=");
		builder.append(bannedUserId);
		builder.append(", bannedUserDiscordTag=");
		builder.append(bannedUserDiscordTag);
		builder.append(", bannedUserReason=");
		builder.append(bannedUserReason);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

}
