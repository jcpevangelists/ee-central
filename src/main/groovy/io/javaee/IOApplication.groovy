package io.javaee

import javax.inject.Inject
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application

@ApplicationPath('/api')
class IOApplication extends Application implements ServletContextListener {

    @Inject
    private ServiceApplication application

    @Override
    void contextInitialized(ServletContextEvent ctx) {
        application.init(ctx.servletContext.getResource('/WEB-INF/documentation'))
    }

    @Override
    void contextDestroyed(ServletContextEvent ctx) {
        // no-op
    }
}

