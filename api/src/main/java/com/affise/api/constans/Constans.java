package com.affise.api.constans;

public class Constans {

    public static class Run{
        public static String host = Hosts.DEV_URL;
        public static String apipath = Path.API_PATH;
        public static String goapipath = Path.GO_API_PATH;
        public static String otherApiPath = Path.OTHER_API_PATH;
    }

    public static class Hosts{
        public static String STAGE_URL = "https://api-staging.affise.com";
        public static String DEV_URL = "http://api.dev.affise.com";
        public static String GO_API_URL = "http://localhost:50603";
    }

    public static class Path{
        public static String API_PATH = "3.0";
        public static String GO_API_PATH = "4.0";
        public static String OTHER_API_PATH = "3.1";
    }

    public static class User{
        public static String ROOT = "f87d35d9e7ed3fe153ce95b259133019";
        public static String ADMIN = "f20a8d4ed68ea0ea2212791ccf09d75f";
        public static String AFFILIATE = "f87d35d9e7ed3fe153ce95b259133019";
        public static String SALES = "f87d35d9e7ed3fe153ce95b259133019";
    }

    public static class UserType{
            public static String ROLE_ADMIN = "ROLE_ADMIN";
            public static String ROLE_MAN_AFFILIATE = "ROLE_MANAGER_AFFILIATE";
            public static String ROLE_MAN_SALES = "ROLE_MANAGER_SALES";
    }

    public static class UserPermissions{
        public static String ENTITY_AFFILIATE = "entity-affiliate";
        public static String ENTITY_ADVERTISER = "entity-advertiser";
    }

    public static class UserPermissionsLevel{
        public static String WRITE = "write";
        public static String DENY = "deny";
        public static String READ = "read";
    }

    public static class Headers{
        public static String API_KEY = "API-KEY";
    }

    public static class Data{
        public static String email = "email";
        public static String password = "password";
        public static String name = "name";
        public static String login = "login";
        public static String title = "title";
        public static String manager = "manager";
        public static String url = "url";
        public static String skype = "skype";
    }

}
