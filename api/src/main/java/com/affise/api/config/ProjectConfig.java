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

    @DefaultValue("mongodb://localhost:27018")
    String mongodb();

    @DefaultValue("mongodb://localhost:27017")
    String mongodbCentral();

    String mysqlUrl();
    String mysqlPassword();
    String mysqlUser();

    String clientId();
    String token();

}
