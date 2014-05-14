package de.edgb.aviationclubmanager.web.report;

import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.edgb.aviationclubmanager.web.Util;

public class ClubMemberListReport extends AviationClubManagerReport {

	public ClubMemberListReport(MessageSource messageSource) {
		super(
				messageSource,
				messageSource
						.getMessage(
								"de_edgb_aviationclubmanager_web_report_clubmemberlistreport_title",
								new String[] { messageSource.getMessage(
										"app.club", null,
										LocaleContextHolder.getLocale()) },
								LocaleContextHolder.getLocale()));
		getReportBuilder()
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE);

		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_lastname",
				"lastName");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_firstname",
				"firstName");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_address",
				"address", 100);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_zipcode",
				"zipCode");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_city",
				"city", 100);
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_landline",
				"landline");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_cellphone",
				"cellphone");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_email",
				"email", 125);
		createLocalDateColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_birthday",
				"birthday");
		createEnumColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_clubmemberstate",
				"clubMemberState");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_comment",
				"comment", 60);
	}

	@Override
	protected String getFilename() {
		return messageSource
				.getMessage(
						"de_edgb_aviationclubmanager_web_report_clubmemberlistreport_filename",
						null, LocaleContextHolder.getLocale())
				+ "_"
				+ DateTimeFormat.forPattern("yyyy-MM-dd").print(
						Util.getCurrentDate());
	}
}