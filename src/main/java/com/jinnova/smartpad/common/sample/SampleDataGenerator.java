package com.jinnova.smartpad.common.sample;

import java.sql.SQLException;

import com.jinnova.smartpad.IPage;
import com.jinnova.smartpad.IPagingList;
import com.jinnova.smartpad.partner.ICatalog;
import com.jinnova.smartpad.partner.ICatalogFieldType;
import com.jinnova.smartpad.partner.ICatalogItem;
import com.jinnova.smartpad.partner.ICatalogItemSort;
import com.jinnova.smartpad.partner.ICatalogSort;
import com.jinnova.smartpad.partner.ICatalogField;
import com.jinnova.smartpad.partner.IOperation;
import com.jinnova.smartpad.partner.IOperationSort;
import com.jinnova.smartpad.partner.IPromotion;
import com.jinnova.smartpad.partner.IPromotionSort;
import com.jinnova.smartpad.partner.IScheduleSequence;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.IUserSort;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class SampleDataGenerator {

	public static void main(String[] args) throws SQLException {
		generate();
		//test();
		//testSorters();
	}
	
	static void test() throws SQLException {
		SmartpadCommon.initialize();
		IPartnerManager pm = SmartpadCommon.getPartnerManager();
		IUser primaryUser = pm.login("lotte", "123abc");
		IOperation branch = primaryUser.getBranch();
		ICatalog rootCat = branch.getRootCatalog();
		System.out.println("sub catalog count: " + rootCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getMembers().length);
	}

	@SuppressWarnings({ "rawtypes" })
	static void testSorters() throws SQLException {
		SmartpadCommon.initialize();
		IPartnerManager pm = SmartpadCommon.getPartnerManager();
		IUser u = pm.login("lotte", "123abc");
		
		IPagingList pl = pm.getUserPagingList();
		testSorters(u, pl, IUserSort.class);
		
		pl = u.getStorePagingList();
		testSorters(u, pl, IOperationSort.class);
		
		pl = u.getStorePagingList().loadPage(u, 1).getMembers()[0].getPromotionPagingList();
		testSorters(u, pl, IPromotionSort.class);
		//testSorters(u, pl, IUserSort.class);
		
		ICatalog cat = u.getStorePagingList().loadPage(u, 1).getMembers()[0].getRootCatalog();
		pl = cat.getSubCatalogPagingList();
		testSorters(u, pl, ICatalogSort.class);

		pl = cat.getCatalogItemPagingList();
		testSorters(u, pl, ICatalogItemSort.class);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void testSorters(IUser u, IPagingList list, Class<? extends Enum> e) throws SQLException {
		Enum[] values = e.getEnumConstants();
		for (Enum v : values) {
			list.setSortField(v);
			list.loadPage(u, 1);
		}
	}
	
	static void generate() throws SQLException {
		
		//initialize
		SmartpadCommon.initialize();
		SmartpadCommon.getPartnerManager().clearDatabaseForTests();
		IPartnerManager pm = SmartpadCommon.getPartnerManager();
		
		//system catalog
		ICatalog sysCatFoods = pm.getSystemRootCatalog().getSubCatalogPagingList().newMemberInstance(pm.getSystemUser());
		sysCatFoods.getName().setName("Foods");
		sysCatFoods.getCatalogSpec().setSpecId("foods"); //table name
		ICatalogField field = sysCatFoods.getCatalogSpec().createField();
		field.setId("name"); //column name
		field.setFieldType(ICatalogFieldType.Text_Name);
		field.setName("Name");
		pm.getSystemRootCatalog().getSubCatalogPagingList().put(pm.getSystemUser(), sysCatFoods);
		
		//user
		System.out.println("****USER******");
		
		IUser primaryUser;
		primaryUser = pm.createPrimaryUser("lotte", "abc123");
		primaryUser.setPassword("123abc");
		pm.getUserPagingList().put(primaryUser, primaryUser);
		primaryUser = pm.login("lotte", "123abc");
		System.out.println(primaryUser.getLogin());
		
		//branch
		System.out.println("****BRANCH******");
		IOperation branch = primaryUser.getBranch();
		branch.getRootCatalog().setSystemCatalogId(sysCatFoods.getId());
		branch.getName().setName("Lotteria");
		primaryUser.updateBranch();
		branch = primaryUser.getBranch();
		System.out.println(branch.getName().getName());
		
		//create secondary user
		primaryUser = pm.login("lotte", "123abc");
		IUser u = pm.getUserPagingList().newMemberInstance(primaryUser);
		u.setLogin("lotte2");
		u.setPassword("x");
		pm.getUserPagingList().put(primaryUser, u);
		u = pm.login("lotte2", "x");
		System.out.println(u.getLogin());
		
		branch = u.getBranch();
		System.out.println(branch.getName().getName());
		
		//branch open hours
		branch = primaryUser.getBranch();
		branch.getOpenHours().setText("8AM - 10AM Every Day");
		IScheduleSequence[] schedule = new IScheduleSequence[2];
		schedule[0] = branch.getOpenHours().newScheduleSequenceInstance();
		schedule[0].setHours(new int[] {8, 9, 10});
		branch.getOpenHours().setScheduleSequences(schedule);
		primaryUser.updateBranch();
		
		//branch member levels
		String[] memberLevels = new String[] {"Bronze", "Gold", "Diamond"};
		branch.setMemberLevels(memberLevels);
		primaryUser.updateBranch();
		
		//stores
		System.out.println("****STORES******");
		IOperation[] stores = primaryUser.getStorePagingList().loadPage(primaryUser, 1).getMembers();
		System.out.println("Stores: " + stores.length);
		IOperation store = primaryUser.getStorePagingList().newMemberInstance(primaryUser);
		store.getName().setName("Lotteria Nguyen Thi Thap");
		primaryUser.getStorePagingList().put(primaryUser, store);

		u = pm.login("lotte2", "x");
		stores = u.getStorePagingList().loadPage(u, 1).getMembers();
		System.out.println(stores[0].getName().getName());
		
		stores[0].getName().setName("Lotteria Nguyen Luong Bang");
		primaryUser.getStorePagingList().put(primaryUser, stores[0]);

		u = pm.login("lotte2", "x");
		stores = u.getStorePagingList().loadPage(u, 1).getMembers();
		System.out.println(stores[0].getName().getName());
		
		//catalog
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		ICatalog rootCat = branch.getRootCatalog();
		ICatalog cat = rootCat.getSubCatalogPagingList().newMemberInstance(primaryUser);
		cat.getName().setName("Món điểm tâm");
		rootCat.getSubCatalogPagingList().put(primaryUser, cat);
		
		ICatalog subCat = cat.getSubCatalogPagingList().newMemberInstance(primaryUser);
		subCat.getName().setName("Quick breakfast");
		cat.getSubCatalogPagingList().put(primaryUser, subCat);

		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		System.out.println("catalog count: " + rootCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getMembers().length);
		
		subCat = rootCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getMembers()[0];
		System.out.println("catalog count: " + subCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getMembers().length);
		
		//catalog update
		subCat.getName().setDescription("Both warm and cold");
		rootCat.getSubCatalogPagingList().put(primaryUser, subCat);
		
		//top level catalog item
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getMembers().length);
		ICatalogItem item = rootCat.getCatalogItemPagingList().newMemberInstance(primaryUser);
		item.setField(ICatalogField.ID_NAME, "Mi goi");
		rootCat.getCatalogItemPagingList().put(primaryUser, item);
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1));

		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getMembers().length);
		item = rootCat.getCatalogItemPagingList().newMemberInstance(primaryUser);
		item.setField(ICatalogField.ID_NAME, "Mi goi 2");
		rootCat.getCatalogItemPagingList().put(primaryUser, item);
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getMembers().length);
		
		//promotions
		IPromotion promo = branch.getPromotionPagingList().newMemberInstance(primaryUser);
		promo.getName().setName("Buy one get one free");
		branch.getPromotionPagingList().put(primaryUser, promo);
		
		//load promotions
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		IPage<IPromotion> promoPage = branch.getPromotionPagingList().loadPage(primaryUser, 1);
		System.out.println(promoPage.getMembers()[0].getName().getName() + 
				" (pageCount/totalCount: " + promoPage.getPageCount() + "/" + promoPage.getTotalCount() + ")");
	}

}
