package com.affise.api.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {

    String baseUrl();

    @Key("localization")
    @DefaultValue("en")
    String locale();

    @DefaultValue("false")
    Boolean logging();

//    String env();
//    public static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());

}
