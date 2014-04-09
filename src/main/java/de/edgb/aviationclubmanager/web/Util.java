package de.edgb.aviationclubmanager.web;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadableInstant;

public class Util {
	public static LocalTime getCurrentTime()
	{
		LocalTime time = new LocalTime(DateTimeZone.UTC);
		return new LocalTime(time.getHourOfDay(), time.getMinuteOfHour());
		
	/*	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	       // cal.setTime(date);
	        cal.set(Calendar.YEAR, 1970);
	        cal.set(Calendar.MONTH, Calendar.JANUARY);
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return cal.getTime();/*Calendar c = Calendar.getInstance();
		Date date = new Date(0);//Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 + Calendar.getInstance().get(Calendar.MINUTE) * 60 * 1000);
		DateUtils.setHours(date, c.get(Calendar.HOUR_OF_DAY));
		DateUtils.setMinutes(date, c.get(Calendar.MINUTE));
		/*DateUtils.setYears(d, 1970);
		DateUtils.setMonths(d, 1);
		DateUtils.setDays(d, 1);
		
		DateUtils.setSeconds(d, 0);
		DateUtils.setMilliseconds(d, 0);*
		
		return date;//new Date(getCurrentDateTime().getTime() % (24 * 60 * 60 * 1000));
		/*
		return new Date(getCurrentDateTime().getTime() - getCurrentDate().getTime());
		/*long l = new Date(1971, 1, 1).getTime();
		return new Date(getCurrentDateTime().getTime() % new Date(1971, 1, 1).getTime());/*
		Calendar cal = Calendar.getInstance();
		cal.set(1970, Calendar.JANUARY, 1);
		return cal.getTime();/*
		DateTime now = new DateTime(DateTimeZone.UTC);
		Date d = new Date(now.toLocalTime().getMillisOfDay());
		return d;
		/*Calendar cal = Calendar.getInstance();
		cal.
		cal.setTime(new Date());
		return new Date(1970, 1, 1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));*/
	}
	
	public static LocalDate getCurrentDate()
	{
		return new LocalDate(DateTimeZone.UTC);
		/*Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
       // cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();/*
		Calendar c = Calendar.getInstance();
		Date date = new Date(0);//Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 + Calendar.getInstance().get(Calendar.MINUTE) * 60 * 1000);
		DateUtils.setYears(date, c.get(Calendar.YEAR));
		DateUtils.setMonths(date, c.get(Calendar.MONTH));
		DateUtils.setDays(date, c.get(Calendar.DAY_OF_MONTH));
		return date;
		/*
		return new Date(getCurrentDateTime().getTime() - getCurrentTime().getTime());
		/*return new Date(getCurrentDateTime().getTime() - getCurrentTime().getTime());
		/*return DateUtils.truncate(Calendar.getInstance(), Calendar.DATE).getTime();
		//return new DateTime(DateTimeZone.UTC).toDateMidnight().toDate();
		*	return DateUtils.truncate(getCurrentDateTime(), Calendar.DATE);/*
		long diff = new Date().getTime();
		long dminutes = diff / (60 * 1000) % 60;
		long dhours = diff / (60 * 60 * 1000) % 24;    			   			
		new Date(1970, 1, 1, (int)dhours, (int)dminutes);*/
	}
	
	public static LocalDateTime getCurrentDateTime()
	{
		return new LocalDateTime(DateTimeZone.UTC);
		/*return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
		/*
		return Calendar.getInstance().getTime();
		/*
		return new DateTime(DateTimeZone.UTC).toDate();
	/*	long diff = new Date().getTime();
		long dminutes = diff / (60 * 1000) % 60;
		long dhours = diff / (60 * 60 * 1000) % 24;    			   			
		new Date(1970, 1, 1, (int)dhours, (int)dminutes);*/
	}
	
	/*public static Date getNullPoint()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	       // cal.setTime(date);
		cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return cal.getTime();/*
		return new Date(0);/*Calendar cal = Calendar.getInstance();
		cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
		return cal.getTime();/*
		return new Date(0);
	}
	
	public static LocalTime getNullTime()
	{
		return new LocalTime(0);
	}
	*/

	
/*	public static Date add(Date d1, Date d2)
	{
		return new Date(d1.getTime() + d2.getTime());
	}
	
	public static boolean datesEquals(Date d1, Date d2)
	{
		return Long.valueOf(d1.getTime()).equals(Long.valueOf(d2.getTime()));
	}
	*/
	
	
	public static LocalDateTime convertDateToLocalDateTime(Date date)
	{
		if (date == null)
			return null;
		else
			return new LocalDateTime(date.getTime());
	}
	
	public static LocalDate convertDateToLocalDate(Date date)
	{
		if (date == null)
			return null;
		else
			return new LocalDate(date.getTime());
	}
	
	public static LocalTime convertDateToLocalTime(Date date)
	{
		if (date == null)
			return null;
		else
			return new LocalTime(date.getTime());
	}
	
	public static Date convertLocalDateTimeToDate(LocalDateTime date)
	{
		if (date == null)
			return null;
		else
			return new Date(date.toDateTime(DateTimeZone.UTC).getMillis() - TimeZone.getDefault().getOffset(new Date().getTime()));
	}
	
	public static Date convertLocalDateToDate(LocalDate date)
	{
		if (date == null)
			return null;
		else
			return new Date(date.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis() - TimeZone.getDefault().getOffset(new Date().getTime()));
	}
	
	public static Date convertLocalTimeToDate(LocalTime date)
	{
		if (date == null)
			return null;
		else
		{
			// TODO: toDateTime nicht toDateTimeToday!
			DateTime dt = date.toDateTimeToday(DateTimeZone.UTC);
			
			return new Date(dt.getMillis() - TimeZone.getDefault().getOffset(new Date().getTime()));
		}
	}
	
	public static String convertPeriodToString(Period period)
	{
		int minutes = period.getHours() * 60 + period.getMinutes();
		return String.format("%02d:%02d", minutes / 60, minutes % 60);
	}
}
