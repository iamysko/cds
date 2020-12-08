/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.entities;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class MutedUser.
 */
@Document(collection = "mutes")
public class MutedUser implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6798677224243799148L;

	/** The moderator user id. */
	@JsonProperty
	private Long moderatorUserId;

	/** The moderator discord tag. */
	@JsonProperty
	private String moderatorDiscordTag;

	/** The muted user id. */
	@JsonProperty
	private Long mutedUserId;

	/** The muted user discord tag. */
	@JsonProperty
	private String mutedUserDiscordTag;

	/** The muted user reason. */
	@JsonProperty
	private String muteReason;

	/** The date when the mute was invoked. */
	@JsonProperty
	private Instant startDate;

	/** The mute's registered end date. */
	@JsonProperty
	private Instant endDate;

	public Long getModeratorUserId() {
		return moderatorUserId;
	}

	public void setModeratorUserId(final Long moderatorUserId) {
		this.moderatorUserId = moderatorUserId;
	}

	public String getModeratorDiscordTag() {
		return moderatorDiscordTag;
	}

	public void setModeratorDiscordTag(final String moderatorDiscordTag) {
		this.moderatorDiscordTag = moderatorDiscordTag;
	}

	public Long getMutedUserId() {
		return mutedUserId;
	}

	public void setMutedUserId(final Long mutedUserId) {
		this.mutedUserId = mutedUserId;
	}

	public String getMutedUserDiscordTag() {
		return mutedUserDiscordTag;
	}

	public void setMutedUserDiscordTag(final String mutedUserDiscordTag) {
		this.mutedUserDiscordTag = mutedUserDiscordTag;
	}

	public String getMuteReason() {
		return muteReason;
	}

	public void setMuteReason(final String muteReason) {
		this.muteReason = muteReason;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(final Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(final Instant endDate) {
		this.endDate = endDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((moderatorDiscordTag == null) ? 0 : moderatorDiscordTag.hashCode());
		result = prime * result + ((moderatorUserId == null) ? 0 : moderatorUserId.hashCode());
		result = prime * result + ((muteReason == null) ? 0 : muteReason.hashCode());
		result = prime * result + ((mutedUserDiscordTag == null) ? 0 : mutedUserDiscordTag.hashCode());
		result = prime * result + ((mutedUserId == null) ? 0 : mutedUserId.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

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
		final MutedUser other = (MutedUser) obj;
		if (endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!endDate.equals(other.endDate)) {
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
		if (muteReason == null) {
			if (other.muteReason != null) {
				return false;
			}
		} else if (!muteReason.equals(other.muteReason)) {
			return false;
		}
		if (mutedUserDiscordTag == null) {
			if (other.mutedUserDiscordTag != null) {
				return false;
			}
		} else if (!mutedUserDiscordTag.equals(other.mutedUserDiscordTag)) {
			return false;
		}
		if (mutedUserId == null) {
			if (other.mutedUserId != null) {
				return false;
			}
		} else if (!mutedUserId.equals(other.mutedUserId)) {
			return false;
		}
		if (startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!startDate.equals(other.startDate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MutedUser [moderatorUserId=");
		builder.append(moderatorUserId);
		builder.append(", moderatorDiscordTag=");
		builder.append(moderatorDiscordTag);
		builder.append(", mutedUserId=");
		builder.append(mutedUserId);
		builder.append(", mutedUserDiscordTag=");
		builder.append(mutedUserDiscordTag);
		builder.append(", muteReason=");
		builder.append(muteReason);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append("]");
		return builder.toString();
	}

}
