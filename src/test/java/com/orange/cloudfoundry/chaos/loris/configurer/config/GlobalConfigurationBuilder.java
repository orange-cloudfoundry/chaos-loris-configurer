package com.orange.cloudfoundry.chaos.loris.configurer.config;


import java.util.Arrays;

/**
 * @author O. Orand
 */
public class GlobalConfigurationBuilder {


    public static GlobalConfiguration defaultTestConfig(int orgCount, int spacePerOrg, int appPerSpace) {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        Organization org;
        for (int i = 0; i < orgCount; i++) {
            org = OrganizationBuilder.defaultOrg(spacePerOrg,appPerSpace);
            globalConfiguration.getOrganizations().put(org.getName(), org);
        }

        Schedule schedule= ScheduleBuilder.atNoon();
        globalConfiguration.getSchedules().put(schedule.getName(),schedule);

        return globalConfiguration;
    }


    public static GlobalConfiguration buildWith(Organization... organizations) {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();

        Arrays.asList(organizations).forEach( organization ->
                globalConfiguration.getOrganizations().put(organization.getName(), organization)
        );

        Schedule schedule= ScheduleBuilder.atNoon();
        globalConfiguration.getSchedules().put(schedule.getName(),schedule);

        return globalConfiguration;
    }

}