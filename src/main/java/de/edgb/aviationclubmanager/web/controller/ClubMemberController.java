package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.ClubCapacity;
import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.ClubMemberState;
import de.edgb.aviationclubmanager.domain.Gender;
import de.edgb.aviationclubmanager.web.UserAccountDetails;
import de.edgb.aviationclubmanager.web.Util;
import de.edgb.aviationclubmanager.web.report.ClubMemberBirthdayStateReport;
import de.edgb.aviationclubmanager.web.report.ClubMemberListReport;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/clubmembers")
@Controller
@RooWebScaffold(path = "clubmembers", formBackingObject = ClubMember.class)
public class ClubMemberController {

	@Autowired
	MessageSource messageSource;

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER_CREATE')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid ClubMember clubMember,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, clubMember);
			return "clubmembers/create";
		}
		uiModel.asMap().clear();
		clubMember.persist();
		return "redirect:/clubmembers/"
				+ encodeUrlPathSegment(clubMember.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER_CREATE')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new ClubMember());
		return "clubmembers/create";
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("clubmember", ClubMember.findClubMember(id));
		uiModel.addAttribute("itemId", id);
		return "clubmembers/show";
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("clubmembers",
					ClubMember.findClubMemberEntries(firstResult, sizeNo));
			float nrOfPages = (float) ClubMember.countClubMembers() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("clubmembers",
					ClubMember.findAllClubMembers(true));
		}
		addDateTimeFormatPatterns(uiModel);
		return "clubmembers/list";
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER_UPDATE')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid ClubMember clubMember,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, clubMember);
			return "clubmembers/update";
		}
		uiModel.asMap().clear();
		clubMember.merge();
		return "redirect:/clubmembers/"
				+ encodeUrlPathSegment(clubMember.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER_UPDATE')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, ClubMember.findClubMember(id));
		return "clubmembers/update";
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER_DELETE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		ClubMember clubMember = ClubMember.findClubMember(id);

		// TODO: Fehlermeldung!
		// aktueller Benutzer kann nicht gelöscht werden!
		if (clubMember.getId().equals(
				((UserAccountDetails) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal()).getUserAccount()
						.getPerson().getId()))
			throw new AccessDeniedException("Access denied!");

		clubMember.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/clubmembers";
	}

	void populateEditForm(Model uiModel, ClubMember clubMember) {
		uiModel.addAttribute("clubMember", clubMember);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("clubcapacitys",
				ClubCapacity.findAllClubCapacitys());
		uiModel.addAttribute("clubmemberstates",
				Arrays.asList(ClubMemberState.values()));
		uiModel.addAttribute("genders", Arrays.asList(Gender.values()));
	}

	// Finder

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(params = "winchdriver", method = RequestMethod.GET)
	public String findWinchDriver(Model uiModel) {
		uiModel.addAttribute("clubmembers", ClubMember
				.findClubMembersBeingWinchDriver().getResultList());
		addDateTimeFormatPatterns(uiModel);
		return "clubmembers/list";
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(params = "instructor", method = RequestMethod.GET)
	public String findInstructor(Model uiModel) {
		uiModel.addAttribute("clubmembers", ClubMember
				.findClubMembersBeingInstructor().getResultList());
		addDateTimeFormatPatterns(uiModel);
		return "clubmembers/list";
	}

	// Reports

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(value = "/reports/clubmemberlist", method = RequestMethod.GET)
	public void generateClubmemberlist(
			@RequestParam(value = "format", required = true) String format,
			HttpServletResponse response) throws IOException {

		ClubMemberListReport report = new ClubMemberListReport(messageSource,
				format);
		report.writeToHttpServletResponse(response);
	}

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(value = "/reports/clubmemberlist/vcard", method = RequestMethod.GET)
	public void generateClubmemberlistVCard(HttpServletResponse response)
			throws IOException {

		response.setHeader(
				"Content-Disposition",
				"attachment; filename="
						+ messageSource
								.getMessage(
										"de_edgb_aviationclubmanager_web_report_clubmemberlistreport_vcard_filename",
										null, LocaleContextHolder.getLocale())
						+ "_"
						+ DateTimeFormat.forPattern("yyyy-MM-dd").print(
								Util.getCurrentDate()) + ".vcf");

		OutputStream stream = response.getOutputStream();

		for (ClubMember m : ClubMember.findAllClubMembers(false)) {
			String gender;
			if (m.getGender().equals(Gender.male))
				gender = "M";
			else
				gender = "F";
			String string = "BEGIN:VCARD\n" + "VERSION:4.0\n" + "N:"
					+ m.getLastName() + ";" + m.getFirstName() + ";;;\n"
					+ "FN:" + m.getFirstName() + " " + m.getLastName() + "\n"
					+ "ADR;TYPE=home:;;" + emptyIfNull(m.getAddress()) + ";"
					+ emptyIfNull(m.getCity()) + ";;"
					+ emptyIfNull(m.getZipCode()) + ";\n" + "TEL;TYPE=home:"
					+ emptyIfNull(m.getLandline()) + "\n" + "TEL;TYPE=cell:"
					+ emptyIfNull(m.getCellphone()) + "\n" + "TEL;TYPE=fax:"
					+ emptyIfNull(m.getFax()) + "\n" + "EMAIL:"
					+ emptyIfNull(m.getEmail()) + "\n";
			LocalDate birthday = m.getBirthday();
			if (birthday != null)
				string = string
						+ "BDAY:"
						+ DateTimeFormat.forPattern("yyyy-MM-dd").print(
								birthday) + "\n";
			string = string + "GENDER:" + gender + "\n" + "NOTE:"
					+ emptyIfNull(m.getComment()) + "\n" + "END:VCARD\n";
			stream.write(string.getBytes(Charset.forName("UTF-8")));
		}
		stream.close();

		response.setContentType("text/vcard");
	}
	
	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(value = "/reports/clubmemberbirthdaystate", method = RequestMethod.GET)
	public void generateClubMemberBirthdayStateReport(
			@RequestParam(value = "format", required = true) String format,
			HttpServletResponse response) throws IOException {

		ClubMemberBirthdayStateReport report = new ClubMemberBirthdayStateReport(messageSource,
				format);
		report.writeToHttpServletResponse(response);
	}

	String emptyIfNull(String str) {
		if (str == null)
			return "";
		else
			return str;
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"clubMember_birthday_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"clubMember_joiningdate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"clubMember_exitdate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
	}
}
