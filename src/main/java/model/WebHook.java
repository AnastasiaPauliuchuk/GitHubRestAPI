package model;

public class WebHook {


    private static final String CREATE_HOOK_JSON_TEMPLATE = "{\n" +
            "  \"name\": \"%1$s\",\n" +
            "  \"active\": true,\n" +
            "  \"events\": [%2$s],\n" +
            "  \"config\": {\n" +
            "    \"url\": \"%3$s\",\n" +
            "    \"content_type\": \"json\"\n" +
            "  }\n" +
            "}";

    public static String getCreateJsonTemplate() {
        return CREATE_HOOK_JSON_TEMPLATE;
    }
}
