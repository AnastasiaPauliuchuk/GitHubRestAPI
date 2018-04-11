import model.PullRequestsData;
import properties.PropertiesResourceManager;

import java.sql.*;
import java.util.Timer;

public class GitHubToJenkinsAgent  {

    private static final String SETTINGS_FILE = "settings.properties";



    public static void main(String args[])  {

        PropertiesResourceManager prop = new PropertiesResourceManager(SETTINGS_FILE);
        Integer period = Integer.parseInt(prop.getProperty("periodMilis","60000"));

        //jdbc:derby:c:/Users/Anastasiya_Pauliuchu/IdeaProjects/restGitHub/database/derby;create=true
        //Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();

        PreparedStatement statement;
        PullRequestsData dataFormDB;
        Timer time;
        GitHubToJenkinsTask gitHubToJenkinsTask;

        try {

                Connection connect = DriverManager.getConnection("jdbc:derby:c:/Users/Anastasiya_Pauliuchu/IdeaProjects/restGitHub/database/derby;create=true");

                 statement = connect.prepareStatement("select * from report_updates");

                ResultSet resultSet = statement.executeQuery();


        }
        catch (SQLException e) {
            e.printStackTrace();
        }



        //read from db
        dataFormDB = null;
        time = new Timer();

        gitHubToJenkinsTask = new GitHubToJenkinsTask();
        gitHubToJenkinsTask.setInitialData(dataFormDB);
        time.schedule(gitHubToJenkinsTask, 0, period);
        gitHubToJenkinsTask.writeDataToDB();
    }

}
