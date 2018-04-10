import model.PullRequestsData;
import properties.PropertiesResourceManager;

import java.util.Timer;

public class GitHubToJenkinsAgent  {

    private static final String SETTINGS_FILE = "settings.properties";



    public static void main(String args[])  {

        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty("periodMilis","60000"));

        //read from db
        PullRequestsData dataFormDB = null;
        Timer time = new Timer();

        GitHubToJenkinsTask gitHubToJenkinsTask = new GitHubToJenkinsTask();
        gitHubToJenkinsTask.setInitialData(dataFormDB);
        time.schedule(gitHubToJenkinsTask, 0, period);
        gitHubToJenkinsTask.writeDataToDB();
    }

}
