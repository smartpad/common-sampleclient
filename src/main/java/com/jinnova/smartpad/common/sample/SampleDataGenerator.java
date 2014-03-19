package com.jinnova.smartpad.common.sample;

import java.sql.SQLException;

import com.jinnova.smartpad.partner.IOperation;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class SampleDataGenerator {

	public static void main(String[] args) throws SQLException {
		
		//user
		System.out.println("****USER******");
		SmartpadCommon.initialize();
		SmartpadCommon.getPartnerManager().clearDatabaseForTests();
		
		IPartnerManager pm = SmartpadCommon.getPartnerManager();
		IUser primaryUser;
		primaryUser = pm.createPrimaryUser("lotte", "abc123");
		primaryUser.setPassword("123abc");
		pm.updateUser(primaryUser, primaryUser);
		primaryUser = pm.login("lotte", "123abc");
		System.out.println(primaryUser.getLogin());
		
		//branch
		System.out.println("****BRANCH******");
		IOperation branch = primaryUser.loadBranch();
		branch.setName("Lotteria");
		primaryUser.updateBranch();
		branch = primaryUser.loadBranch();
		System.out.println(branch.getName());
		
		pm.createUser(primaryUser, "lotte2", "x");
		IUser u = pm.login("lotte2", "x");
		System.out.println(u.getLogin());
		
		branch = u.loadBranch();
		System.out.println(branch.getName());
		
		//stores
		System.out.println("****STORES******");
		IOperation[] stores = primaryUser.loadStores();
		System.out.println("Stores: " + stores.length);
		IOperation store = primaryUser.newStoreInstance();
		store.setName("Lotteria Nguyen Thi Thap");
		primaryUser.putStore(store);

		u = pm.login("lotte2", "x");
		stores = u.loadStores();
		System.out.println(stores[0].getName());
		
		stores[0].setName("Lotteria Nguyen Luong Bang");
		primaryUser.putStore(stores[0]);

		u = pm.login("lotte2", "x");
		stores = u.loadStores();
		System.out.println(stores[0].getName());
	}

}
