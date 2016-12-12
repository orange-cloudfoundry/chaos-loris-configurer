package com.orange.cloudfoundry.chaos.loris.configurer.config;


import lombok.Builder;

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

}