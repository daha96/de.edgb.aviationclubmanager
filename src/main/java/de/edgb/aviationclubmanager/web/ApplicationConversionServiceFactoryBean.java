package de.edgb.aviationclubmanager.web;

import java.math.BigDecimal;
import java.text.NumberFormat;
import org.apache.commons.lang3.StringUtils;
import org.osgi.application.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;
import de.edgb.aviationclubmanager.domain.Aircraft;
import de.edgb.aviationclubmanager.domain.ClubCapacity;
import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.domain.UserPermission;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Autowired
	MessageSource messageSource;
	
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		
		registry.addConverter(createEnumConverter());
		registry.addConverter(createBooleanConverter());
		registry.addConverter(createBigDecimalConverter());
		
		registry.addConverter(getUserPermissionToStringConverter());
	}
	
	private Converter<Enum<?>, String> createEnumConverter() {
		  return new Converter<Enum<?>, String>() {
		 
		    @Override
		    public String convert(Enum<?> value) {
		      String output = value.toString();
		      try {
		        output = messageSource.getMessage(value.toString(), null, LocaleContextHolder.getLocale());
		      } catch (NoSuchMessageException e) {
		        System.err.println("No message resource found for " + value + " add this to the resource bundle");
		      }
		      return output;
		    }
		  };
		}

	private Converter<Boolean, String> createBooleanConverter() {
		  return new Converter<Boolean, String>() {
		 
		    @Override
		    public String convert(Boolean value) {
		      String output = value.toString();
		      try {
		        output = messageSource.getMessage(value.toString(), null, LocaleContextHolder.getLocale());
		      } catch (NoSuchMessageException e) {
		        System.err.println("No message resource found for " + value + " add this to the resource bundle");
		      }
		      return output;
		    }
		  };
		}
	
	private Converter<BigDecimal, String> createBigDecimalConverter() {
		  return new Converter<BigDecimal, String>() {
		 
		    @Override
		    public String convert(BigDecimal value) {
		      String output = value.toString();
		      try {
		        output = NumberFormat.getInstance(LocaleContextHolder.getLocale()).format(value);//applicationContext.getMessage(value.toString(), null, LocaleContextHolder.getLocale());
		      } catch (NoSuchMessageException e) {
		        System.err.println("No message resource found for " + value + " add this to the resource bundle");
		      }
		      return output;
		    }
		  };
		}
	
	
	/*private Converter<UserPermission, String> createUserPermissionConverter() {
		  return new Converter<UserPermission, String>() {
		 
		    @Override
		    public String convert(UserPermission value) {
		      String output = value.toString();
		      try {
		    	  output = messageSource.getMessage(value.getName(), null, LocaleContextHolder.getLocale());
		      } catch (NoSuchMessageException e) {
		        System.err.println("No message resource found for " + value + " add this to the resource bundle");
		      }
		      return output;
		    }
		  };
		}
	*/
	// Entinitäten
	
	public Converter<ClubCapacity, String> getClubCapacityToStringConverter() {
		return new Converter<ClubCapacity, String>() {
			public String convert(ClubCapacity clubCapacity) {
				String output = clubCapacity.getName();

				if (clubCapacity.getEmail() != null
						&& !StringUtils.isEmpty(clubCapacity.getEmail()))
					output += " (" + clubCapacity.getEmail() + ")";

				return output;

			}
		};
	}

	public Converter<Aircraft, String> getAircraftToStringConverter() {
        return new Converter<Aircraft, String>() {
            public String convert(Aircraft aircraft) {
            	String output = aircraft.getRegistration();

				if (aircraft.getCallsign() != null
						&& !StringUtils.isEmpty(aircraft.getCallsign()))
					output += " (" + aircraft.getCallsign() + ")";

				return output;
			}
        };
    }

/*	public Converter<Bug, String> getBugToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.airfieldmanager.domain.Bug, java.lang.String>() {
            public String convert(Bug bug) {
                return new StringBuilder().append(bug.getBugDate()).append(": ").append(bug.getDescription()).toString();
            }
        };
    }

	public Converter<ClubAircraft, String> getClubAircraftToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.airfieldmanager.domain.ClubAircraft, java.lang.String>() {
            public String convert(ClubAircraft clubAircraft) {
            	String output = clubAircraft.getRegistration();

				if (clubAircraft.getCallsign() != null
						&& !StringUtils.isEmpty(clubAircraft.getCallsign()))
					output += " (" + clubAircraft.getCallsign() + ")";

				return output;
            }
        };
    }
*/
	public Converter<ClubMember, String> getClubMemberToStringConverter() {
        return new Converter<ClubMember, String>() {
            public String convert(ClubMember clubMember) {
            	if (clubMember.getLastName() != null && !StringUtils.isEmpty(clubMember.getLastName()) &&
            			clubMember.getFirstName() != null && !StringUtils.isEmpty(clubMember.getFirstName()))
            			return clubMember.getLastName() + ", " + clubMember.getFirstName();
            	else
            			return new StringBuilder().append(clubMember.getLastName()).append(' ').append(clubMember.getFirstName()).append(' ').append(clubMember.getAddress()).append(' ').toString();
            }
        };
    }

	public Converter<Person, String> getPersonToStringConverter() {
        return new Converter<Person, String>() {
            public String convert(Person person) {
            	if (person.getLastName() != null && !StringUtils.isEmpty(person.getLastName()) &&
            			person.getFirstName() != null && !StringUtils.isEmpty(person.getFirstName()))
            			return person.getLastName() + ", " + person.getFirstName();
            	else
            			return new StringBuilder().append(person.getLastName()).append(' ').append(person.getFirstName()).append(' ').append(person.getAddress()).append(' ').toString();
            }
        };
    }

	public Converter<UserPermission, String> getUserPermissionToStringConverter() {
		return new Converter<UserPermission, String>() {
			 
		    @Override
		    public String convert(UserPermission value) {
		      String output = value.toString();
		      try {
		    	  output = messageSource.getMessage(value.getName(), null, LocaleContextHolder.getLocale());
		      } catch (NoSuchMessageException e) {
		        System.err.println("No message resource found for " + value + " add this to the resource bundle");
		      }
		      return output;
		    }
		  };
    }
}
