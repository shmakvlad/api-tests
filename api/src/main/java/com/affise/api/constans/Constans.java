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

    public static class Headers{
        public static String API_KEY = "API-KEY";
    }

    public static class UserRoles{
        public String token;
        public String translation;

        public UserRoles(String token, String translation){
            this.token = token;
            this.translation = translation;
        }

        public static UserRoles ROOT = new UserRoles("5be436c9c29e7936dc78d8a8","ROOT");
        public static UserRoles ADMIN = new UserRoles("f87d35d9e7ed3fe153ce95b259133019","ADMINISTRATOR");
        public static UserRoles AFFILIATE = new UserRoles("4bb6760900aaca4969d2f47c5ff0a5c3","AFFILIATE MANAGER");
        public static UserRoles ACCOUNT = new UserRoles("d194921e05d7662d729c340eeaedc405","ACCOUNT MANAGER");
    }

}
