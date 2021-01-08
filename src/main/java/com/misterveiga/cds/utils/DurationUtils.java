package com.misterveiga.cds.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurationUtils {

	/** The log. */
	private final static Logger log = LoggerFactory.getLogger(DurationUtils.class);

	public static Instant addDurationStringToCurrentDate(String durationString) {
		// Format is XdXhXm for days, hours and minutes. Ignore others.
		durationString = durationString.toLowerCase();
		try {
			if (durationString.indexOf('d') != -1) {
				if (durationString.indexOf('h') != -1) {
					if (durationString.indexOf('m') != -1) {
						// d h m present
						final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
						final Long hours = Long.parseLong(
								durationString.substring(durationString.indexOf('d') + 1, durationString.indexOf('h')));
						final Long minutes = Long.parseLong(
								durationString.substring(durationString.indexOf('h') + 1, durationString.indexOf('m')));
						final Long totalMinutes = (days * 1440) + (hours * 60) + minutes;
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					} else {
						// d h present
						final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
						final Long hours = Long.parseLong(
								durationString.substring(durationString.indexOf('d') + 1, durationString.indexOf('h')));
						final Long totalMinutes = (days * 1440) + (hours * 60);
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					}
				} else if (durationString.indexOf('m') != -1) {
					// d m present
					final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
					final Long minutes = Long.parseLong(
							durationString.substring(durationString.indexOf('d') + 1, durationString.indexOf('m')));
					final Long totalMinutes = (days * 1440) + minutes;
					return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
				} else {

					// d present
					final Long days = Long.parseLong(durationString.substring(0, durationString.indexOf('d')));
					final Long totalMinutes = (days * 1440);
					return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
				}
			} else {
				if (durationString.indexOf('h') != -1) {
					if (durationString.indexOf('m') != -1) {
						// h m present
						final Long hours = Long.parseLong(durationString.substring(0, durationString.indexOf('h')));
						final Long minutes = Long.parseLong(
								durationString.substring(durationString.indexOf('h') + 1, durationString.indexOf('m')));
						final Long totalMinutes = (hours * 60) + minutes;
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					} else {
						// h present
						final Long hours = Long.parseLong(durationString.substring(0, durationString.indexOf('h')));
						final Long totalMinutes = (hours * 60);
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);
					}
				} else {
					if (durationString.indexOf('m') != -1) {
						// m present
						final Long minutes = Long.parseLong(durationString.substring(0, durationString.indexOf('m')));
						final Long totalMinutes = minutes;
						return Instant.now().plus(totalMinutes, ChronoUnit.MINUTES);

					}
				}

			}
		} catch (final NumberFormatException e) {
			log.warn("Incorrect mute duration detected: {}", durationString);
		}

		return null;
	}

}
