package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author Anastasia Pauliuchuk
 *         created:  1/3/2018.
 */
public class PropertiesResourceManager  {

    private Properties properties = new Properties();
    private Logger logger = Logger.getLogger(PropertiesResourceManager.class);


    public PropertiesResourceManager() {
        properties = new Properties();
    }

    public PropertiesResourceManager(final String resourceName) {
        properties = appendFromResource(properties, resourceName);

    }


    private Properties appendFromResource(final Properties objProperties, final String resourceName) {
        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);

        if (inStream != null) {
            try {
                objProperties.load(inStream);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.fatal(String.format("Resource \"%1$s\" could not be found", resourceName));
        }
        return objProperties;
    }

    public String getProperty(final String key) {
        return properties.getProperty(key);
    }


    public String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    public Set<Object> getAllKeys() {
        return properties.keySet();
    }

    public Map<String,String> getAllProperties() {
        HashMap<String,String> map = new HashMap<>();
        for(Object key: getAllKeys()) {
            map.put((String)key,properties.getProperty((String) key));
        }
        return map;
    }

}
