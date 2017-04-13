package app.daos;

import javax.persistence.EntityManager;


public class AbstractDao
{
	private EntityManager em = null;

	public EntityManager getEM()
	{
		return em;
	}


	public AbstractDao(EntityManager em)
	{
		this.em = em;
	}
}
