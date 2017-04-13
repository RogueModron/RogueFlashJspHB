package app.model;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public enum PersistenceManager
{
	INSTANCE;


	private static EntityManagerFactory emf = null;


	public synchronized void initEMF(String persistenceUnitName)
	{
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
	}

	public synchronized void closeEMF()
	{
		if (emf != null && emf.isOpen())
		{
			emf.close();
		}
	}

	public EntityManagerFactory getEMF()
	{
		return emf;
	}


	public EntityManager getEM()
	{
		return emf.createEntityManager();
	}


	public synchronized void deregisterDrivers()
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements())
		{
			Driver driver = drivers.nextElement();
			if (driver.getClass().getClassLoader() != cl)
			{
				continue;
			}

			try
			{
				DriverManager.deregisterDriver(driver);
			}
			catch (SQLException ex)
			{
				Log log = LogFactory.getLog(PersistenceManager.class);
				log.debug(ex.getMessage());
			}
		}
	}
}
