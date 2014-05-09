package com.jinnova.smartpad.common.sample;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class ClientSupport {
	
	static void createDatabase(String dbhost, String dbport, String dbname, String dblogin, String dbpass, boolean withDrilling) {
		try {
			Class<?> c = Class.forName("com.jinnova.smartpad.db.ScriptRunner");
			Method m = c.getMethod("createDatabase", String.class, String.class, String.class, String.class, String.class, boolean.class);
			m.invoke(null, dbhost, dbport, dbname, dblogin, dbpass, withDrilling);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	static void dropDatabaseIfExists(String dbhost, String dbport, String dbname, String dblogin, String dbpass) throws SQLException {

		Connection conn = DriverManager.getConnection(makeDburl(dbhost, dbport, "mysql"), dblogin, dbpass);
		Statement stmt = conn.createStatement();
		String sql = "drop database if exists " + dbname;
		System.out.println("SQL: " + sql);
		stmt.executeUpdate(sql);
		stmt.close();
		conn.close();
	}
	
	public static String makeDburl(String dbhost, String dbport, String dbname) {
		String dburl = "jdbc:mysql://" + dbhost;
		if (dbport != null) {
			dburl = dburl + ":" + dbport;
		}
		return dburl + "/" + dbname + "?useUnicode=true&characterEncoding=UTF-8";
	}
	
	static void generateSystemCatalogs() {
		try {
			Class<?> c = Class.forName("com.jinnova.smartpad.partner.SystemCatalogGenrator");
			Method m = c.getMethod("generate");
			m.invoke(c.newInstance());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
	
	static void loadSyscatsInitially() {
		try {
			Class<?> c = Class.forName("com.jinnova.smartpad.partner.PartnerManager");
			Method m = c.getMethod("loadSyscatsInitially");
			m.invoke(null);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	static void copyDataToDrilling(String drillDbhost, String drillDbport, String drillDbname, String drillDblogin, String drillDbpass, String mainDbname) {
		try {
			Class<?> c = Class.forName("com.jinnova.smartpad.db.ScriptRunner");
			Method m = c.getMethod("copyDataToDrilling", String.class, String.class, String.class, String.class, String.class, String.class);
			m.invoke(null, drillDbhost, drillDbport, drillDbname, drillDblogin, drillDbpass, mainDbname);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	static void createItems() {
		try {
			Class<?> c = Class.forName("com.jinnova.smartpad.partner.SystemCatalogGenrator");
			Method m = c.getMethod("createItems");
			m.invoke(c.newInstance());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

}
