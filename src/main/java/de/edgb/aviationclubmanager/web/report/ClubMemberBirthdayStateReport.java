package de.edgb.aviationclubmanager.web.report;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.web.Util;

public class ClubMemberBirthdayStateReport extends AviationClubManagerReport {

	public ClubMemberBirthdayStateReport(MessageSource messageSource,
			String format) {
		super(
				messageSource,
				messageSource
						.getMessage(
								"e_edgb_aviationclubmanager_web_report_clubmemberbirthdaystatereport_title",
								new String[] { messageSource.getMessage(
										"app.club", null,
										LocaleContextHolder.getLocale()) },
								LocaleContextHolder.getLocale()), format);

		setDataSource(ClubMember.findAllClubMembers(false));

		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_lastname",
				"lastName");
		createTextColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_firstname",
				"firstName");
		createLocalDateColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_birthday",
				"birthday");
		createEnumColumn(
				"label_de_edgb_aviationclubmanager_domain_clubmember_clubmemberstate",
				"clubMemberState");
	}

	@Override
	protected String getFilename() {
		return messageSource
				.getMessage(
						"de_edgb_aviationclubmanager_web_report_clubmemberbirthdaystatereport_filename",
						null, LocaleContextHolder.getLocale())
				+ "_"
				+ DateTimeFormat.forPattern("yyyy-MM-dd").print(
						Util.getCurrentDate());
	}

	@Override
	protected void addDataLayout(JasperReportBuilder reportBuilder) {
		reportBuilder.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
	}
}
