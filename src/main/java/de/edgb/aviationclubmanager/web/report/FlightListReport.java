package de.edgb.aviationclubmanager.web.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.edgb.aviationclubmanager.web.Util;

public class FlightListReport extends AviationClubManagerReport {

	public FlightListReport(MessageSource messageSource, LocalDate date,
			Integer numFlights) {
		super(
				messageSource,
				messageSource
						.getMessage(
								"de_edgb_aviationclubmanager_web_report_flightlistreport_title",
								new String[] { messageSource.getMessage(
										"app.homeLocation", null,
										LocaleContextHolder.getLocale()) },
								LocaleContextHolder.getLocale()));

		String dateText = messageSource
				.getMessage(
						"de_edgb_aviationclubmanager_web_report_flightlistreport_date_label",
						new String[] { DateTimeFormat.mediumDate()
								.withLocale(LocaleContextHolder.getLocale())
								.print(date) }, LocaleContextHolder.getLocale());

		String flightText = numFlights.toString();

		if (numFlights > 1)
			flightText = flightText
					+ " "
					+ messageSource
							.getMessage(
									"label_de_edgb_aviationclubmanager_domain_flight_plural",
									null, LocaleContextHolder.getLocale());
		else
			flightText = flightText
					+ " "
					+ messageSource.getMessage(
							"label_de_edgb_aviationclubmanager_domain_flight",
							null, LocaleContextHolder.getLocale());

		getReportBuilder()
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
				.pageHeader(
						cmp.horizontalList()
								.add(cmp.text(flightText)
										.setStyle(
												stl.style()
														.setHorizontalAlignment(
																HorizontalAlignment.LEFT)
														.setLeftPadding(15)),
										cmp.text(dateText)
												.setStyle(
														stl.style()
																.setHorizontalAlignment(
																		HorizontalAlignment.RIGHT)
																.setRightPadding(
																		15))));

		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_aircraft_registration",
				"aircraft.registration", 60);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_aircraft_model",
				"aircraft.model", 60);
		createEnumColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_flighttype",
				"flightType", 60);
		createPersonColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_pilot",
				"pilot");
		createPersonColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_copilot",
				"copilot");
		createEnumColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_launchmethod",
				"launchMethod", 60);

		createLocalTimeColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_departuretime",
				"departureTime", 50);
		createLocalTimeColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_landingtime",
				"landingTime", 50);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_duration",
				"durationString", 50);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_departurelocation",
				"departureLocation", 80);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_landinglocation",
				"landingLocation", 80);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_flight_comment",
				"comment", 60);
	}

	@Override
	protected StyleBuilder getcolumnStyle() {
		return stl.style().setFontSize(8);
	}

	@Override
	protected String getFilename() {
		return messageSource
				.getMessage(
						"de_edgb_aviationclubmanager_web_report_flightlistreport_filename",
						null, LocaleContextHolder.getLocale())
				+ "_"
				+ DateTimeFormat.forPattern("yyyy-MM-dd").print(
						Util.getCurrentDate());
	}
}
