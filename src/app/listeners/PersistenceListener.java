package app.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import app.model.PersistenceManager;


public class PersistenceListener implements ServletContextListener
{
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		PersistenceManager.INSTANCE.closeEMF();

		// http://stackoverflow.com/questions/3320400/to-prevent-a-memory-leak-the-jdbc-driver-has-been-forcibly-unregistered
		PersistenceManager.INSTANCE.deregisterDrivers();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		PersistenceManager.INSTANCE.initEMF("RogueFlash");
	}
}
