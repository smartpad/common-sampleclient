package com.jinnova.smartpad.common.sample;

import java.sql.SQLException;

import com.jinnova.smartpad.db.IBranchDao;
import com.jinnova.smartpad.db.IUserDao;
import com.jinnova.smartpad.partner.IBranch;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.PartnerUtils;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class SampleDataGenerator {

	public static void main(String[] args) throws SQLException {
		SmartpadCommon.initialize();
		IPartnerManager pm = SmartpadCommon.getPartnerManager();
		IUserDao userDao = pm.getUserDao();
		IUser u;
		u = userDao.createPrimaryUser("lotte", "abc123");
		u.setPasshash(PartnerUtils.md5("123abc"));
		userDao.updateUser(u);
		u = userDao.loadUser("lotte");
		System.out.println(u.getLogin());
		
		u = userDao.createUser(u, "lotte2", "x");
		System.out.println(u.getLogin());
		
		IBranchDao bdao = pm.getBranchDao();
		IBranch branch = bdao.loadBranch("lotte");
		branch.setName("Lotteria");
		bdao.updateBranch(branch);
	}

}
