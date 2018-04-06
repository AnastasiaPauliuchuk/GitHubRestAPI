import org.springframework.scheduling.config.ScheduledTask;
import utils.PropertiesResourceManager;

import java.util.Timer;
import java.util.TimerTask;

public class GitHubToJenkinsAgent  {
    private static final String SETTINGS_FILE = "settings.properties";

    public static void main(String args[])  {

        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty("periodMilis","60000"));

        Timer time = new Timer();
        GitHubManager gitHubManager = new GitHubManager();
        time.schedule(gitHubManager, 0, period);

    }

}
