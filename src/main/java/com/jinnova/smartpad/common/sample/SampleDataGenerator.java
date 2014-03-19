package com.jinnova.smartpad.common.sample;

import java.sql.SQLException;

import com.jinnova.smartpad.partner.IBranch;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.PartnerUtils;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class SampleDataGenerator {

	public static void main(String[] args) throws SQLException {
		SmartpadCommon.initialize();
		IPartnerManager pm = SmartpadCommon.getPartnerManager();
		IUser primaryUser;
		primaryUser = pm.createPrimaryUser("lotte", "abc123");
		primaryUser.setPasshash(PartnerUtils.md5("123abc"));
		pm.updateUser(primaryUser, primaryUser);
		primaryUser = pm.login("lotte", "123abc");
		System.out.println(primaryUser.getLogin());
		
		IUser u = pm.createUser(primaryUser, "lotte2", "x");
		System.out.println(u.getLogin());
		
		IBranch branch = pm.loadBranch("lotte");
		branch.setName("Lotteria");
		pm.updateBranch(primaryUser, branch);
	}

}
