package main;

import account.AccountService;
import account.AccountServiceImpl;
import level_1.ShuffleWordService;
import level_1.ShuffleWord;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.handler.HandlerList;
import org.glassfish.jersey.servlet.ServletContainer;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import rest.FirstLevel;
import rest.Session;
import account.UserProfile;
import rest.User;
import org.hibernate.HibernateException;


/**
 * created by snach
 */
public class Main {
    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        @SuppressWarnings("ConstantNamingConvention")
        final Logger logger = new Logger(AccountServiceImpl.class);
        int port = -1;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
        } else {
            logger.log("Specify port");
            System.exit(1);
        }

        logger.log("Starting at port: " + String.valueOf(port) + '\n');

        try {
            final Server server = new Server(port);

            final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);

            final SessionFactory sessionFactory;

            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(UserProfile.class);
            configuration.addAnnotatedClass(ShuffleWord.class);

            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_Poliglot");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "rootPassword");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "create");

            sessionFactory = configuration.buildSessionFactory();

            final Context context = new Context();
            context.put(AccountService.class, new AccountServiceImpl(sessionFactory));
            context.put(ShuffleWordService.class, new ShuffleWordService(sessionFactory));

            final ResourceConfig config = new ResourceConfig(User.class, Session.class, FirstLevel.class);
            config.register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(context);
                }
            });
            final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setWelcomeFiles(new String[]{"index.html"});
            resourceHandler.setResourceBase("public_html");

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});
            server.setHandler(handlers);

            contextHandler.addServlet(servletHolder, "/*");
            server.start();
            server.join();
        } catch (HibernateException e) {
            logger.log("Fail to connect to db_Poliglot");
            System.exit(1);
        }
    }
}