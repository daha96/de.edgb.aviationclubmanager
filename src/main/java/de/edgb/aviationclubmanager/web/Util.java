package de.edgb.aviationclubmanager.web;

import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class Util {

	public static LocalTime getCurrentTime() {
		LocalTime time = new LocalTime(DateTimeZone.UTC);
		return new LocalTime(time.getHourOfDay(), time.getMinuteOfHour());
	}

	public static LocalDate getCurrentDate() {
		return new LocalDate(DateTimeZone.UTC);
	}

	public static LocalDateTime getCurrentDateTime() {
		return new LocalDateTime(DateTimeZone.UTC);
	}

	public static LocalDateTime convertDateToLocalDateTime(Date date) {
		if (date == null)
			return null;
		else
			return new LocalDateTime(date.getTime());
	}

	public static LocalDate convertDateToLocalDate(Date date) {
		if (date == null)
			return null;
		else
			return new LocalDate(date.getTime());
	}

	public static LocalTime convertDateToLocalTime(Date date) {
		if (date == null)
			return null;
		else
			return new LocalTime(date.getTime());
	}

	public static Date convertLocalDateTimeToDate(LocalDateTime date) {
		if (date == null)
			return null;
		else
			return new Date(date.toDateTime(DateTimeZone.UTC).getMillis()
					- TimeZone.getDefault().getOffset(new Date().getTime()));
	}

	public static Date convertLocalDateToDate(LocalDate date) {
		if (date == null)
			return null;
		else
			return new Date(date.toDateTimeAtStartOfDay(DateTimeZone.UTC)
					.getMillis()
					- TimeZone.getDefault().getOffset(new Date().getTime()));
	}

	public static Date convertLocalTimeToDate(LocalTime date) {
		if (date == null)
			return null;
		else {
			// TODO: toDateTime nicht toDateTimeToday!
			DateTime dt = date.toDateTimeToday(DateTimeZone.UTC);

			return new Date(dt.getMillis()
					- TimeZone.getDefault().getOffset(new Date().getTime()));
		}
	}

	public static String convertPeriodToString(Period period) {
		int minutes = period.getHours() * 60 + period.getMinutes();
		return String.format("%02d:%02d", minutes / 60, minutes % 60);
	}
}
