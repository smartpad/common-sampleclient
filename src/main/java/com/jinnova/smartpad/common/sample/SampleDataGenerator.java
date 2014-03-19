package com.jinnova.smartpad.common.sample;

import java.sql.SQLException;

import com.jinnova.smartpad.partner.IOperation;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class SampleDataGenerator {

	public static void main(String[] args) throws SQLException {
		
		//user
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
	}

}
