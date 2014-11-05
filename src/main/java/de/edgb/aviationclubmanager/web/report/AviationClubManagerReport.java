package de.edgb.aviationclubmanager.web.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;

import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.web.Util;

public abstract class AviationClubManagerReport {

	StyleBuilder titleStyle = stl.style().bold()
			.setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(16);
	StyleBuilder columnTitleStyle = stl.style().bold().setFontSize(8);
	StyleBuilder pageOfStyle = stl.style().setHorizontalAlignment(
			HorizontalAlignment.RIGHT).setFontSize(10);
	StyleBuilder versionStyle = stl.style().setHorizontalAlignment(
			HorizontalAlignment.CENTER).setFontSize(7);
	StyleBuilder createTimestampStyle = stl.style().setHorizontalAlignment(
			HorizontalAlignment.LEFT).setFontSize(7);

	protected StyleBuilder getcolumnStyle() {
		return stl.style().setFontSize(7);
	}

	protected void addExtraLayout(JasperReportBuilder reportBuilder) {
	}
	
	protected void addDataLayout(JasperReportBuilder reportBuilder) {
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	MessageSource messageSource;
	JasperReportBuilder reportBuilder;
	String format;

	protected AviationClubManagerReport(MessageSource messageSource,
			String reportTitle, String format) {
		this.messageSource = messageSource;
		this.format = format;

		String timestampText = messageSource.getMessage(
				"de_edgb_aviationclubmanager_web_report_createtimestamp",
				new String[] { DateTimeFormat.fullDateTime()
						.withLocale(LocaleContextHolder.getLocale())
						.print(Util.getCurrentDateTime()) },
				LocaleContextHolder.getLocale());
		String versionText = messageSource.getMessage(
				"de_edgb_aviationclubmanager_web_report_version",
				new String[] { messageSource.getMessage("app.version", null,
						LocaleContextHolder.getLocale()) }, LocaleContextHolder
						.getLocale());

		reportBuilder = report().setPageFormat(PageType.A4,
				PageOrientation.LANDSCAPE);
		
		

		if (format.equals("pdf")) {
			reportBuilder

					// Page
					.setPageMargin(margin(20))

					// Page Header
					.setPageHeaderStyle(stl.style().setBottomPadding(20))
					.pageHeader(cmp.text(reportTitle).setStyle(titleStyle))

					// Column Header
					.columnHeader(cmp.line())
					.setColumnHeaderStyle(stl.style().setBottomPadding(5))

					// Column Title
					.setColumnTitleStyle(columnTitleStyle)

					// Column
					.setColumnStyle(getcolumnStyle()).highlightDetailEvenRows()

					// Column Footer
					.columnFooter(cmp.line())
					.setColumnFooterStyle(stl.style().setTopPadding(5))

					// Page Footer
					.setPageFooterStyle(stl.style().setTopPadding(20))

					.pageFooter(
							cmp.horizontalList().add(
									cmp.text(timestampText).setStyle(
											createTimestampStyle),
									cmp.text(versionText)
											.setStyle(versionStyle),
									cmp.pageXslashY().setStyle(pageOfStyle)));
		} else
			reportBuilder.setColumnTitleStyle(columnTitleStyle)
					.setColumnStyle(getcolumnStyle()).setIgnorePagination(true);
		
		addDataLayout(reportBuilder);
	}

	protected void createTextColumn(String messageCode, String propertyName) {
		reportBuilder.addColumn(col.column(
				messageSource.getMessage(messageCode, null,
						LocaleContextHolder.getLocale()), propertyName,
				type.stringType()));
	}

	protected void createTextColumn(String messageCode, String propertyName,
			Integer fixedWidth) {
		reportBuilder.addColumn(col.column(
				messageSource.getMessage(messageCode, null,
						LocaleContextHolder.getLocale()), propertyName,
				type.stringType()).setFixedWidth(fixedWidth));
	}

	protected <V extends Enum<V>> void createEnumColumn(String messageCode,
			String propertyName) {
		reportBuilder.addField(field(propertyName, Object.class)).addColumn(
				col.column(messageSource.getMessage(messageCode, null,
						LocaleContextHolder.getLocale()), new EnumColumn<V>(
						propertyName)));
	}

	protected <V extends Enum<V>> void createEnumColumn(String messageCode,
			String propertyName, Integer fixedWidth) {
		reportBuilder.addField(field(propertyName, Object.class)).addColumn(
				col.column(
						messageSource.getMessage(messageCode, null,
								LocaleContextHolder.getLocale()),
						new EnumColumn<V>(propertyName)).setFixedWidth(
						fixedWidth));
	}

	protected void createLocalDateColumn(String messageCode, String propertyName) {
		reportBuilder.addField(field(propertyName, LocalDate.class)).addColumn(
				col.column(messageSource.getMessage(messageCode, null,
						LocaleContextHolder.getLocale()), new LocalDateColumn(
						propertyName)));
	}

	protected void createLocalTimeColumn(String messageCode, String propertyName) {
		reportBuilder.addField(field(propertyName, LocalTime.class)).addColumn(
				col.column(messageSource.getMessage(messageCode, null,
						LocaleContextHolder.getLocale()), new LocalTimeColumn(
						propertyName)));

	}

	protected void createLocalTimeColumn(String messageCode,
			String propertyName, Integer fixedWidth) {
		reportBuilder.addField(field(propertyName, LocalTime.class)).addColumn(
				col.column(
						messageSource.getMessage(messageCode, null,
								LocaleContextHolder.getLocale()),
						new LocalTimeColumn(propertyName)).setFixedWidth(
						fixedWidth));

	}

	protected void createPersonColumn(String messageCode, String propertyName) {
		reportBuilder.addField(field(propertyName, Person.class)).addColumn(
				col.column(messageSource.getMessage(messageCode, null,
						LocaleContextHolder.getLocale()), new PersonColumn(
						propertyName)));

	}

	protected abstract String getFilename();

	public void writeToHttpServletResponse(HttpServletResponse response)
			throws IOException {

		try {

			response.setHeader("Content-Disposition", "attachment; filename="
					+ getFilename() + "." + format);

			OutputStream stream = response.getOutputStream();
			switch (format) {
			case "pdf":
				response.setContentType("application/pdf");
				addExtraLayout(reportBuilder);
				reportBuilder.toPdf(export.pdfExporter(stream));
				break;
			case "xls":
				response.setContentType("application/vnd.ms-excel");
				reportBuilder.toXls(export.xlsExporter(stream)
						.setDetectCellType(true).setIgnorePageMargins(true)
						.setWhitePageBackground(false)
						.setRemoveEmptySpaceBetweenColumns(true));
				break;
			case "ods":
				response.setContentType("application/vnd.oasis.opendocument.spreadsheet");
				reportBuilder.toOds(export.odsExporter(stream)
						.setIgnorePageMargins(true));
				break;
			case "csv":
				response.setContentType("text/csv");
				reportBuilder.toCsv(export.csvExporter(stream));
				break;
			default:
				throw new InvalidParameterException();
			}
			stream.close();
		} catch (DRException e) {
			e.printStackTrace();
		}

	}

	protected void setDataSource(Collection<?> dataSource) {
		if (dataSource.isEmpty())
			throw new EmptyResultDataAccessException(0);
		reportBuilder.setDataSource(dataSource);
	}

	private class EnumColumn<E extends Enum<E>> extends
			AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		String fieldName;

		public EnumColumn(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			E value = reportParameters.getValue(fieldName);
			String output = value.toString();
			try {
				output = messageSource.getMessage(value.toString(), null,
						LocaleContextHolder.getLocale());
			} catch (NoSuchMessageException e) {
				System.err.println("No message resource found for " + value
						+ " add this to the resource bundle");
			}
			return output;
		}
	}

	private class LocalDateColumn extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		String fieldName;

		public LocalDateColumn(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			LocalDate value = (LocalDate) reportParameters.getValue(fieldName);

			if (value == null)
				return "";
			else
				return DateTimeFormat.mediumDate()
						.withLocale(LocaleContextHolder.getLocale())
						.print(value);
		}
	}

	private class LocalTimeColumn extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		String fieldName;

		public LocalTimeColumn(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			LocalTime value = (LocalTime) reportParameters.getValue(fieldName);

			if (value == null)
				return "";
			else
				return DateTimeFormat.shortTime()
						.withLocale(LocaleContextHolder.getLocale())
						.print(value);
		}
	}

	private class PersonColumn extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;
		String fieldName;

		public PersonColumn(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			Person value = (Person) reportParameters.getValue(fieldName);

			if (value == null)
				return "";
			else
				return value.getLastName() + ", " + value.getFirstName();
		}
	}
}
