package com.jinnova.smartpad.common.sample;

import java.sql.SQLException;

import com.jinnova.smartpad.partner.ICatalog;
import com.jinnova.smartpad.partner.ICatalogField;
import com.jinnova.smartpad.partner.ICatalogItem;
import com.jinnova.smartpad.partner.IOperation;
import com.jinnova.smartpad.partner.IScheduleSequence;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class Sample2 {

	public static void main(String[] args) throws SQLException {
		
		//initialize
		SmartpadCommon.initialize();
		SmartpadCommon.partnerManager.clearDatabaseForTests();
		
		IUser[] user = new IUser[1];
		IOperation branch = createBranch(user, "lotte", "foods", "Lotteria", "Lotteria Nguyen Thi Thap", "Lotte Ng Van Cu");
		createMenu(user, branch, new String[][] {
				{"Hamburger", "Grilled Chicken Value", "BIG STAR Combo", "Fish Burger", "Bánh Hot Dog", "Cheese Egg Burger"},
				{"Chicken set", "GIFT SET 1", "GIFT SET 2", "Finger Chicken 1000gr", "Finger Chicken 200gr", "Gà rán phần gia đình (9 miếng)"},
				{"Chicken", "Gà HS phần", "Gà HS 1 miếng", "Gà rán phần ", "Gà viên"},
				{"Rice", "Cơm Thịt Heo Chiên", "Cơm gà sốt đậu ", "Cơm thịt bò"},
				{"Dessert", "Gà Nugget", "Tôm viên", "Bánh Hot Pie"},
				{"Drinks", "Float kem", "Kem cây", "Kem ly ", "Tornado", "7Up, Mirinda", "Nước Chanh", "Trà Nestea"},});
		branch = createBranch(user, "kfc", "foods", "KFC", "KFC Bui Bang Doan");
		branch = createBranch(user, "mcdonald", "foods", "Mc Donald", "Mc Donald DBP");
		branch = createBranch(user, "Popeyes", "foods", "Popeyes", "Popeyes PMY");
		branch = createBranch(user, "coffeebean", "foods", "Coffee Beans", "Beans Le Duan", "Beans Hightech");
	}

	private static IOperation createBranch(IUser[] user, String login, String syscatId, String name, String... storeNames) throws SQLException {
		IPartnerManager pm = SmartpadCommon.partnerManager;
		IUser primaryUser;
		primaryUser = pm.createPrimaryUser(login, login);
		pm.getUserPagingList().put(primaryUser, primaryUser);
		IOperation branch = primaryUser.getBranch();
		branch.getRootCatalog().setSystemCatalogId(syscatId);
		branch.getName().setName(name);
		branch.getOpenHours().setText("8AM - 10AM Every Day");
		IScheduleSequence[] schedule = new IScheduleSequence[2];
		schedule[0] = branch.getOpenHours().newScheduleSequenceInstance();
		schedule[0].setHours(new int[] {8, 9, 10});
		branch.getOpenHours().setScheduleSequences(schedule);
		primaryUser.updateBranch();

		primaryUser = pm.login(login, login);
		for (String s : storeNames) {
			IOperation store = primaryUser.getStorePagingList().newEntryInstance(primaryUser);
			store.getName().setName(s);
			primaryUser.getStorePagingList().put(primaryUser, store);
		}
		user[0] = primaryUser;
		return branch;
	}
	
	private static void createMenu(IUser[] user, IOperation branch, String[][] s) throws SQLException {
		ICatalog rootCat = branch.getRootCatalog();
		for (String[] menu : s) {
			ICatalog cat = rootCat.getSubCatalogPagingList().newEntryInstance(user[0]);
			cat.getName().setName(menu[0]);
			rootCat.getSubCatalogPagingList().put(user[0], cat);
			for (int i = 1; i < menu.length; i++) {
				String one = menu [i];
				ICatalogItem item = cat.getCatalogItemPagingList().newEntryInstance(user[0]);
				item.setField(ICatalogField.ID_NAME, one);
				cat.getCatalogItemPagingList().put(user[0], item);
			}
		}
	}

}
