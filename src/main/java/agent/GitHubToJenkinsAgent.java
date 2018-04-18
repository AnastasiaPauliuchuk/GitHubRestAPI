package agent;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import properties.PropertiesResourceManager;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;


public class GitHubToJenkinsAgent {

    private static final String SETTINGS_FILE = "settings.properties";
    private static final String PERIOD_PROP = "periodMilis";

    private static boolean isResend;
    private static boolean isUseDb;


    public static void main(String args[]) {

        List<String> argsList = Arrays.asList(args);
        isResend = argsList.contains("-r");
        isUseDb = argsList.contains("-d");

        if(isUseDb)  System.setProperty("spring.profiles.active", "withDB");
        else   System.setProperty("spring.profiles.active", "withoutDB");

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        GitHubToJenkinsTask gitHubToJenkinsTask  = (GitHubToJenkinsTask) context.getBean("gitHubTask");


        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty(PERIOD_PROP, "36000"));

        gitHubToJenkinsTask.processInitialDataFromDb(isResend);


        Timer time = new Timer();
        time.schedule(gitHubToJenkinsTask, 0, period);

    }


}
