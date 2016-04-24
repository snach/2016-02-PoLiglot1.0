package main.cnf;

import account.UserProfile;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.*;

/**
 * Created by Snach on 15.04.16.
 */
public class Config {
    private static final String SERVER_CONFIG_FILE = "configuration/cfg/server.properties";
    private static final String DB_CONFIG_FILE = "configuration/cfg/db.properties";
    private static final String WORDS_XML_FILE = "configuration/data/words.xml";

    private static final Logger LOGGER = LogManager.getLogger(Config.class);

    private static int port;
    //private static String host;
    private static long maxWordId;

    public static void loadConfig() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(SERVER_CONFIG_FILE)) {
            properties.load(fileInputStream);
            fileInputStream.close();
            port = Integer.valueOf(properties.getProperty("port"));
            /* host = properties.getProperty("host"); */
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found: " + SERVER_CONFIG_FILE);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static SessionFactory connectToDB(){

        final Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(DB_CONFIG_FILE)) {
            properties.load(fileInputStream);
            fileInputStream.close();

            configuration.setProperty("hibernate.dialect", properties.getProperty("dialect"));
            configuration.setProperty("hibernate.connection.driver_class", properties.getProperty("connection.driver_class"));
            configuration.setProperty("hibernate.connection.url", properties.getProperty("connection.url"));
            configuration.setProperty("hibernate.connection.username", properties.getProperty("connection.username"));
            configuration.setProperty("hibernate.connection.password", properties.getProperty("connection.password"));
            configuration.setProperty("hibernate.show_sql", properties.getProperty("show_sql"));
            configuration.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hbm2ddl.auto"));

        } catch (FileNotFoundException e) {
            LOGGER.error("File not found: " + DB_CONFIG_FILE);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return configuration.buildSessionFactory();
    }
    @Nullable
    public static Map<Long, String> readXML() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SAXHendler handler = new SAXHendler();
            saxParser.parse(WORDS_XML_FILE, handler);
            maxWordId = handler.getPos();
            return handler.getWords();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getPort() {
        return port;
    }

    public static long getMaxWordid() {
        return maxWordId;
    }
}
