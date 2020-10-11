/*
 * Author: {Ruben Veiga}
 */
package com.misterveiga.cds.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * The Class CommandActionDTO.
 */
@Document(collection = "actionlog")
public class CommandActionDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5567535364045755080L;

	/** The author id. */
	@Id
	private String authorId;

	/** The author discord tag. */
	private String authorDiscordTag;

	/** The action type. */
	private String actionType;

	/** The action date. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime actionDate;

	/**
	 * Gets the author id.
	 *
	 * @return the author id
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * Sets the author id.
	 *
	 * @param authorId the new author id
	 */
	public void setAuthorId(final String authorId) {
		this.authorId = authorId;
	}

	/**
	 * Gets the author discord tag.
	 *
	 * @return the author discord tag
	 */
	public String getAuthorDiscordTag() {
		return authorDiscordTag;
	}

	/**
	 * Sets the author discord tag.
	 *
	 * @param authorDiscordTag the new author discord tag
	 */
	public void setAuthorDiscordTag(final String authorDiscordTag) {
		this.authorDiscordTag = authorDiscordTag;
	}

	/**
	 * Gets the action type.
	 *
	 * @return the action type
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * Sets the action type.
	 *
	 * @param actionType the new action type
	 */
	public void setActionType(final String actionType) {
		this.actionType = actionType;
	}

	/**
	 * Gets the action date.
	 *
	 * @return the action date
	 */
	public LocalDateTime getActionDate() {
		return actionDate;
	}

	/**
	 * Sets the action date.
	 *
	 * @param actionDate the new action date
	 */
	public void setActionDate(final LocalDateTime actionDate) {
		this.actionDate = actionDate;
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
		result = prime * result + ((actionDate == null) ? 0 : actionDate.hashCode());
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((authorDiscordTag == null) ? 0 : authorDiscordTag.hashCode());
		result = prime * result + ((authorId == null) ? 0 : authorId.hashCode());
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
		final CommandActionDTO other = (CommandActionDTO) obj;
		if (actionDate == null) {
			if (other.actionDate != null) {
				return false;
			}
		} else if (!actionDate.equals(other.actionDate)) {
			return false;
		}
		if (actionType == null) {
			if (other.actionType != null) {
				return false;
			}
		} else if (!actionType.equals(other.actionType)) {
			return false;
		}
		if (authorDiscordTag == null) {
			if (other.authorDiscordTag != null) {
				return false;
			}
		} else if (!authorDiscordTag.equals(other.authorDiscordTag)) {
			return false;
		}
		if (authorId == null) {
			if (other.authorId != null) {
				return false;
			}
		} else if (!authorId.equals(other.authorId)) {
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
		builder.append("CommandActionDTO [authorId=");
		builder.append(authorId);
		builder.append(", authorDiscordTag=");
		builder.append(authorDiscordTag);
		builder.append(", actionType=");
		builder.append(actionType);
		builder.append(", actionDate=");
		builder.append(actionDate);
		builder.append("]");
		return builder.toString();
	}

}
