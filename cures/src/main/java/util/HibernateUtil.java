package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@SuppressWarnings(Constant.UNUSED)
public class HibernateUtil {
	// private static StandardServiceRegistry registry;
	// private static SessionFactory sessionFactory;

//	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory factory;

	static {
		factory = new Configuration().configure().buildSessionFactory();
	}

	public static Session buildSessionFactory() {
		return factory.getCurrentSession();
//		return factory.openSession();
	}

	public void doWork() {
		Session session = buildSessionFactory(); // do work.
		session.close();
	}

	// Call this during shutdown
	public static void shutdown() {
		factory.close();
	}

	/*
	 * public static SessionFactory buildSessionFactory() { try { // Create the
	 * SessionFactory from hibernate.cfg.xml return new
	 * Configuration().configure().buildSessionFactory(); } catch (Throwable ex) {
	 * // Make sure you log the exception, as it might be swallowed
	 * System.err.println("//////////Initial SessionFactory creation failed." + ex);
	 * throw new ExceptionInInitializerError(ex); } }
	 */

	/*
	 * public static SessionFactory getSessionFactory() { if(sessionFactory== null){
	 * try{ registry= new StandardServiceRegistryBuilder().configure().build();
	 * 
	 * MetadataSources source= new MetadataSources(registry);
	 * 
	 * Metadata metadata= source.getMetadataBuilder().build();
	 * 
	 * sessionFactory = metadata.getSessionFactoryBuilder().build(); }catch
	 * (Exception e) { e.printStackTrace(); if(registry!=null){
	 * StandardServiceRegistryBuilder.destroy(registry); } } } return
	 * sessionFactory; }
	 */

	/*
	 * public static void shutdown() { // Close caches and connection pools if
	 * (registry != null) { StandardServiceRegistryBuilder.destroy(registry); }
	 * 
	 * }
	 */

}