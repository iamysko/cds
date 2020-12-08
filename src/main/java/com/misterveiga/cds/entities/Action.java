/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.entities;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class CommandActionDTO.
 */
@Document(collection = "activity")
public class Action implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5567535364045755080L;

	/** The author id. */
	@JsonProperty
	private String user;

	/** The author discord tag. */
	@JsonProperty
	private Long discordId;

	/** The action type. */
	@JsonProperty
	private String actionType;

	/** The author id. */
	@JsonProperty
	private String offendingUser;

	/** The author id. */
	@JsonProperty
	private Long offendingUserId;

	/** The action date. */
	@JsonProperty
	private Instant date;

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public Long getDiscordId() {
		return discordId;
	}

	public void setDiscordId(final Long discordId) {
		this.discordId = discordId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(final String actionType) {
		this.actionType = actionType;
	}

	public String getOffendingUser() {
		return offendingUser;
	}

	public void setOffendingUser(final String offendingUser) {
		this.offendingUser = offendingUser;
	}

	public Long getOffendingUserId() {
		return offendingUserId;
	}

	public void setOffendingUserId(final Long offendingUserId) {
		this.offendingUserId = offendingUserId;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(final Instant date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((discordId == null) ? 0 : discordId.hashCode());
		result = prime * result + ((offendingUser == null) ? 0 : offendingUser.hashCode());
		result = prime * result + ((offendingUserId == null) ? 0 : offendingUserId.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		final Action other = (Action) obj;
		if (actionType == null) {
			if (other.actionType != null) {
				return false;
			}
		} else if (!actionType.equals(other.actionType)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (discordId == null) {
			if (other.discordId != null) {
				return false;
			}
		} else if (!discordId.equals(other.discordId)) {
			return false;
		}
		if (offendingUser == null) {
			if (other.offendingUser != null) {
				return false;
			}
		} else if (!offendingUser.equals(other.offendingUser)) {
			return false;
		}
		if (offendingUserId == null) {
			if (other.offendingUserId != null) {
				return false;
			}
		} else if (!offendingUserId.equals(other.offendingUserId)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Action [user=");
		builder.append(user);
		builder.append(", discordId=");
		builder.append(discordId);
		builder.append(", actionType=");
		builder.append(actionType);
		builder.append(", offendingUser=");
		builder.append(offendingUser);
		builder.append(", offendingUserId=");
		builder.append(offendingUserId);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

}
