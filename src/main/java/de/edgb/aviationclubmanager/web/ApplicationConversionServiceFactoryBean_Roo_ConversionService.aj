// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.edgb.aviationclubmanager.web;

import de.edgb.aviationclubmanager.domain.Accounting;
import de.edgb.aviationclubmanager.domain.Aircraft;
import de.edgb.aviationclubmanager.domain.Bug;
import de.edgb.aviationclubmanager.domain.ClubCapacity;
import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.Flight;
import de.edgb.aviationclubmanager.domain.InstructorPresence;
import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.domain.UserAccount;
import de.edgb.aviationclubmanager.domain.UserRole;
import de.edgb.aviationclubmanager.domain.WinchDriverPresence;
import de.edgb.aviationclubmanager.web.ApplicationConversionServiceFactoryBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Accounting, String> ApplicationConversionServiceFactoryBean.getAccountingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.Accounting, java.lang.String>() {
            public String convert(Accounting accounting) {
                return new StringBuilder().append(accounting.getName()).toString();
            }
        };
    }
    
    public Converter<Long, Accounting> ApplicationConversionServiceFactoryBean.getIdToAccountingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.Accounting>() {
            public de.edgb.aviationclubmanager.domain.Accounting convert(java.lang.Long id) {
                return Accounting.findAccounting(id);
            }
        };
    }
    
    public Converter<String, Accounting> ApplicationConversionServiceFactoryBean.getStringToAccountingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.Accounting>() {
            public de.edgb.aviationclubmanager.domain.Accounting convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Accounting.class);
            }
        };
    }
    
    public Converter<Long, Aircraft> ApplicationConversionServiceFactoryBean.getIdToAircraftConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.Aircraft>() {
            public de.edgb.aviationclubmanager.domain.Aircraft convert(java.lang.Long id) {
                return Aircraft.findAircraft(id);
            }
        };
    }
    
    public Converter<String, Aircraft> ApplicationConversionServiceFactoryBean.getStringToAircraftConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.Aircraft>() {
            public de.edgb.aviationclubmanager.domain.Aircraft convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Aircraft.class);
            }
        };
    }
    
    public Converter<Bug, String> ApplicationConversionServiceFactoryBean.getBugToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.Bug, java.lang.String>() {
            public String convert(Bug bug) {
                return new StringBuilder().append(bug.getBugDate()).append(' ').append(bug.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Bug> ApplicationConversionServiceFactoryBean.getIdToBugConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.Bug>() {
            public de.edgb.aviationclubmanager.domain.Bug convert(java.lang.Long id) {
                return Bug.findBug(id);
            }
        };
    }
    
    public Converter<String, Bug> ApplicationConversionServiceFactoryBean.getStringToBugConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.Bug>() {
            public de.edgb.aviationclubmanager.domain.Bug convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Bug.class);
            }
        };
    }
    
    public Converter<Long, ClubCapacity> ApplicationConversionServiceFactoryBean.getIdToClubCapacityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.ClubCapacity>() {
            public de.edgb.aviationclubmanager.domain.ClubCapacity convert(java.lang.Long id) {
                return ClubCapacity.findClubCapacity(id);
            }
        };
    }
    
    public Converter<String, ClubCapacity> ApplicationConversionServiceFactoryBean.getStringToClubCapacityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.ClubCapacity>() {
            public de.edgb.aviationclubmanager.domain.ClubCapacity convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ClubCapacity.class);
            }
        };
    }
    
    public Converter<Long, ClubMember> ApplicationConversionServiceFactoryBean.getIdToClubMemberConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.ClubMember>() {
            public de.edgb.aviationclubmanager.domain.ClubMember convert(java.lang.Long id) {
                return ClubMember.findClubMember(id);
            }
        };
    }
    
    public Converter<String, ClubMember> ApplicationConversionServiceFactoryBean.getStringToClubMemberConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.ClubMember>() {
            public de.edgb.aviationclubmanager.domain.ClubMember convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ClubMember.class);
            }
        };
    }
    
    public Converter<Flight, String> ApplicationConversionServiceFactoryBean.getFlightToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.Flight, java.lang.String>() {
            public String convert(Flight flight) {
                return new StringBuilder().append(flight.getFlightDate()).append(' ').append(flight.getDepartureTime()).append(' ').append(flight.getLandingTime()).append(' ').append(flight.getLastManipulationDate()).toString();
            }
        };
    }
    
    public Converter<Long, Flight> ApplicationConversionServiceFactoryBean.getIdToFlightConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.Flight>() {
            public de.edgb.aviationclubmanager.domain.Flight convert(java.lang.Long id) {
                return Flight.findFlight(id);
            }
        };
    }
    
    public Converter<String, Flight> ApplicationConversionServiceFactoryBean.getStringToFlightConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.Flight>() {
            public de.edgb.aviationclubmanager.domain.Flight convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Flight.class);
            }
        };
    }
    
    public Converter<InstructorPresence, String> ApplicationConversionServiceFactoryBean.getInstructorPresenceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.InstructorPresence, java.lang.String>() {
            public String convert(InstructorPresence instructorPresence) {
                return new StringBuilder().append(instructorPresence.getPresenceDate()).append(' ').append(instructorPresence.getComment()).toString();
            }
        };
    }
    
    public Converter<Long, InstructorPresence> ApplicationConversionServiceFactoryBean.getIdToInstructorPresenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.InstructorPresence>() {
            public de.edgb.aviationclubmanager.domain.InstructorPresence convert(java.lang.Long id) {
                return InstructorPresence.findInstructorPresence(id);
            }
        };
    }
    
    public Converter<String, InstructorPresence> ApplicationConversionServiceFactoryBean.getStringToInstructorPresenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.InstructorPresence>() {
            public de.edgb.aviationclubmanager.domain.InstructorPresence convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), InstructorPresence.class);
            }
        };
    }
    
    public Converter<Long, Person> ApplicationConversionServiceFactoryBean.getIdToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.Person>() {
            public de.edgb.aviationclubmanager.domain.Person convert(java.lang.Long id) {
                return Person.findPerson(id);
            }
        };
    }
    
    public Converter<String, Person> ApplicationConversionServiceFactoryBean.getStringToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.Person>() {
            public de.edgb.aviationclubmanager.domain.Person convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Person.class);
            }
        };
    }
    
    public Converter<UserAccount, String> ApplicationConversionServiceFactoryBean.getUserAccountToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.UserAccount, java.lang.String>() {
            public String convert(UserAccount userAccount) {
                return new StringBuilder().append(userAccount.getUsername()).append(' ').append(userAccount.getPassword()).toString();
            }
        };
    }
    
    public Converter<Long, UserAccount> ApplicationConversionServiceFactoryBean.getIdToUserAccountConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.UserAccount>() {
            public de.edgb.aviationclubmanager.domain.UserAccount convert(java.lang.Long id) {
                return UserAccount.findUserAccount(id);
            }
        };
    }
    
    public Converter<String, UserAccount> ApplicationConversionServiceFactoryBean.getStringToUserAccountConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.UserAccount>() {
            public de.edgb.aviationclubmanager.domain.UserAccount convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UserAccount.class);
            }
        };
    }
    
    public Converter<UserRole, String> ApplicationConversionServiceFactoryBean.getUserRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.UserRole, java.lang.String>() {
            public String convert(UserRole userRole) {
                return new StringBuilder().append(userRole.getName()).toString();
            }
        };
    }
    
    public Converter<Long, UserRole> ApplicationConversionServiceFactoryBean.getIdToUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.UserRole>() {
            public de.edgb.aviationclubmanager.domain.UserRole convert(java.lang.Long id) {
                return UserRole.findUserRole(id);
            }
        };
    }
    
    public Converter<String, UserRole> ApplicationConversionServiceFactoryBean.getStringToUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.UserRole>() {
            public de.edgb.aviationclubmanager.domain.UserRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), UserRole.class);
            }
        };
    }
    
    public Converter<WinchDriverPresence, String> ApplicationConversionServiceFactoryBean.getWinchDriverPresenceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.edgb.aviationclubmanager.domain.WinchDriverPresence, java.lang.String>() {
            public String convert(WinchDriverPresence winchDriverPresence) {
                return new StringBuilder().append(winchDriverPresence.getPresenceDate()).append(' ').append(winchDriverPresence.getComment()).toString();
            }
        };
    }
    
    public Converter<Long, WinchDriverPresence> ApplicationConversionServiceFactoryBean.getIdToWinchDriverPresenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.edgb.aviationclubmanager.domain.WinchDriverPresence>() {
            public de.edgb.aviationclubmanager.domain.WinchDriverPresence convert(java.lang.Long id) {
                return WinchDriverPresence.findWinchDriverPresence(id);
            }
        };
    }
    
    public Converter<String, WinchDriverPresence> ApplicationConversionServiceFactoryBean.getStringToWinchDriverPresenceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.edgb.aviationclubmanager.domain.WinchDriverPresence>() {
            public de.edgb.aviationclubmanager.domain.WinchDriverPresence convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), WinchDriverPresence.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getAccountingToStringConverter());
        registry.addConverter(getIdToAccountingConverter());
        registry.addConverter(getStringToAccountingConverter());
        registry.addConverter(getAircraftToStringConverter());
        registry.addConverter(getIdToAircraftConverter());
        registry.addConverter(getStringToAircraftConverter());
        registry.addConverter(getBugToStringConverter());
        registry.addConverter(getIdToBugConverter());
        registry.addConverter(getStringToBugConverter());
        registry.addConverter(getClubCapacityToStringConverter());
        registry.addConverter(getIdToClubCapacityConverter());
        registry.addConverter(getStringToClubCapacityConverter());
        registry.addConverter(getClubMemberToStringConverter());
        registry.addConverter(getIdToClubMemberConverter());
        registry.addConverter(getStringToClubMemberConverter());
        registry.addConverter(getFlightToStringConverter());
        registry.addConverter(getIdToFlightConverter());
        registry.addConverter(getStringToFlightConverter());
        registry.addConverter(getInstructorPresenceToStringConverter());
        registry.addConverter(getIdToInstructorPresenceConverter());
        registry.addConverter(getStringToInstructorPresenceConverter());
        registry.addConverter(getPersonToStringConverter());
        registry.addConverter(getIdToPersonConverter());
        registry.addConverter(getStringToPersonConverter());
        registry.addConverter(getUserAccountToStringConverter());
        registry.addConverter(getIdToUserAccountConverter());
        registry.addConverter(getStringToUserAccountConverter());
        registry.addConverter(getUserRoleToStringConverter());
        registry.addConverter(getIdToUserRoleConverter());
        registry.addConverter(getStringToUserRoleConverter());
        registry.addConverter(getWinchDriverPresenceToStringConverter());
        registry.addConverter(getIdToWinchDriverPresenceConverter());
        registry.addConverter(getStringToWinchDriverPresenceConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
