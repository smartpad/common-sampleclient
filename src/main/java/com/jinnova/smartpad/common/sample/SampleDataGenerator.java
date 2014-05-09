package com.jinnova.smartpad.common.sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import com.jinnova.smartpad.IPage;
import com.jinnova.smartpad.IPagingList;
import com.jinnova.smartpad.partner.ICatalog;
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

	public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
		generate();
		//test();
		//testSorters();
	}
	
	static void test() throws SQLException {
		SmartpadCommon.initialize("localhost", null, "smartpad", "root", "root");
		IPartnerManager pm = SmartpadCommon.partnerManager;
		IUser primaryUser = pm.login("lotte", "123abc");
		IOperation branch = primaryUser.getBranch();
		ICatalog rootCat = branch.getRootCatalog();
		System.out.println("sub catalog count: " + rootCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getPageEntries().length);
	}

	@SuppressWarnings({ "rawtypes" })
	static void testSorters() throws SQLException {
		SmartpadCommon.initialize("localhost", null, "smartpad", "root", "root");
		IPartnerManager pm = SmartpadCommon.partnerManager;
		IUser u = pm.login("lotte", "123abc");
		
		IPagingList pl = pm.getUserPagingList();
		testSorters(u, pl, IUserSort.class);
		
		pl = u.getStorePagingList();
		testSorters(u, pl, IOperationSort.class);
		
		pl = u.getStorePagingList().loadPage(u, 1).getPageEntries()[0].getPromotionPagingList();
		testSorters(u, pl, IPromotionSort.class);
		//testSorters(u, pl, IUserSort.class);
		
		ICatalog cat = u.getStorePagingList().loadPage(u, 1).getPageEntries()[0].getRootCatalog();
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
	
	static void generate() throws SQLException, FileNotFoundException, IOException {
		
		//initialize
		ClientSupport.dropDatabaseIfExists("localhost", null, "smartpad", "root", "root");
		ClientSupport.createDatabase("localhost", null, "smartpad", "root", "root", false);
		SmartpadCommon.initialize("localhost", null, "smartpad", "root", "root");
		ClientSupport.generateSystemCatalogs();
		
		//user
		System.out.println("****USER******");
		IPartnerManager pm = SmartpadCommon.partnerManager;
		IUser primaryUser;
		primaryUser = pm.createPrimaryUser("lotte", "abc123");
		primaryUser.setPassword("123abc");
		pm.getUserPagingList().put(primaryUser, primaryUser);
		primaryUser = pm.login("lotte", "123abc");
		System.out.println(primaryUser.getLogin());
		
		//branch
		System.out.println("****BRANCH******");
		ICatalog sysCatFoods = pm.getSystemCatalog("z_entertain_foods");
		IOperation branch = primaryUser.getBranch();
		branch.getRootCatalog().setSystemCatalogId(sysCatFoods.getId());
		branch.setName("Lotteria");
		primaryUser.updateBranch();
		branch = primaryUser.getBranch();
		System.out.println(branch.getName());
		
		//branch gps
		branch.getGps().setLongitude(BigDecimal.ONE);
		branch.getGps().setLatitude(BigDecimal.ONE);
		primaryUser.updateBranch();
		
		//create secondary user
		primaryUser = pm.login("lotte", "123abc");
		IUser u = pm.getUserPagingList().newEntryInstance(primaryUser);
		u.setLogin("lotte2");
		u.setPassword("x");
		pm.getUserPagingList().put(primaryUser, u);
		u = pm.login("lotte2", "x");
		System.out.println(u.getLogin());
		
		branch = u.getBranch();
		System.out.println(branch.getName());
		
		//branch open hours
		branch = primaryUser.getBranch();
		branch.getOpenHours().setDesc("8AM - 10AM Every Day");
		IScheduleSequence[] schedule = new IScheduleSequence[2];
		schedule[0] = branch.getOpenHours().newScheduleSequenceInstance();
		schedule[0].setHours(new int[] {8, 9, 10});
		branch.getOpenHours().setScheduleSequences(schedule);
		primaryUser.updateBranch();
		sure(branch.getGps().getLongitude().intValue() == 1);
		
		//branch items
		/*ICatalogItem item = branch.getRootCatalog().getCatalogItemPagingList().newMemberInstance(primaryUser);
		item.setField(ICatalogField.ID_NAME, "Big burger");
		branch.getRootCatalog().getCatalogItemPagingList().put(primaryUser, item);
		System.out.println("branch item count: " + branch.getRootCatalog().getCatalogItemPagingList().loadPage(primaryUser, 1));*/
		
		//branch member levels
		String[] memberLevels = new String[] {"Bronze", "Gold", "Diamond"};
		branch.setMemberLevels(memberLevels);
		primaryUser.updateBranch();
		
		//stores
		System.out.println("****STORES******");
		IOperation[] stores = primaryUser.getStorePagingList().loadPage(primaryUser, 1).getPageEntries();
		System.out.println("Stores: " + stores.length);
		IOperation store = primaryUser.getStorePagingList().newEntryInstance(primaryUser);
		store.setName("Lotteria Nguyen Thi Thap");
		primaryUser.getStorePagingList().put(primaryUser, store);
		sure(store.getGps().getLongitude().intValue() == 1);

		u = pm.login("lotte2", "x");
		stores = u.getStorePagingList().loadPage(u, 1).getPageEntries();
		System.out.println(stores[0].getName());
		
		stores[0].setName("Lotteria Nguyen Luong Bang");
		primaryUser.getStorePagingList().put(primaryUser, stores[0]);

		u = pm.login("lotte2", "x");
		stores = u.getStorePagingList().loadPage(u, 1).getPageEntries();
		System.out.println(stores[0].getName());
		
		//catalog
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		ICatalog rootCat = branch.getRootCatalog();
		ICatalog cat = rootCat.getSubCatalogPagingList().newEntryInstance(primaryUser);
		cat.setName("Món điểm tâm");
		rootCat.getSubCatalogPagingList().put(primaryUser, cat);
		
		ICatalog subCat = cat.getSubCatalogPagingList().newEntryInstance(primaryUser);
		subCat.setName("Quick breakfast");
		cat.getSubCatalogPagingList().put(primaryUser, subCat);

		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		System.out.println("catalog count: " + rootCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getPageEntries().length);
		
		subCat = rootCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getPageEntries()[0];
		System.out.println("catalog count: " + subCat.getSubCatalogPagingList().loadPage(primaryUser, 1).getPageEntries().length);
		
		//catalog update
		subCat.getDesc().setDescription("Both warm and cold");
		rootCat.getSubCatalogPagingList().put(primaryUser, subCat);
		
		//top level catalog item
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getPageEntries().length);
		ICatalogItem item = rootCat.getCatalogItemPagingList().newEntryInstance(primaryUser);
		item.setField(ICatalogField.F_NAME, "Mi goi");
		rootCat.getCatalogItemPagingList().put(primaryUser, item);
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1));
		sure(item.getGps().getLongitude().intValue() == 1);

		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getPageEntries().length);
		item = rootCat.getCatalogItemPagingList().newEntryInstance(primaryUser);
		item.setField(ICatalogField.F_NAME, "Mi goi 2");
		rootCat.getCatalogItemPagingList().put(primaryUser, item);
		System.out.println("top level item count: " + rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getPageEntries().length);
		
		//promotions
		IPromotion promo = branch.getPromotionPagingList().newEntryInstance(primaryUser);
		promo.setName("Buy one get one free");
		branch.getPromotionPagingList().put(primaryUser, promo);
		sure(promo.getGps().getLongitude().intValue() == 1);
		
		//load promotions
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		IPage<IPromotion> promoPage = branch.getPromotionPagingList().loadPage(primaryUser, 1);
		System.out.println(promoPage.getPageEntries()[0].getName() + 
				" (pageCount/totalCount: " + promoPage.getPageCount() + "/" + promoPage.getTotalCount() + ")");
		
		//change gps
		branch.getGps().setLongitude(new BigDecimal(2));
		primaryUser.updateBranch();
		sure(promo.getGps().getLongitude().intValue() == 1);
		primaryUser = pm.login("lotte", "123abc");
		branch = primaryUser.getBranch();
		rootCat = branch.getRootCatalog();
		sure(rootCat.getCatalogItemPagingList().loadPage(primaryUser, 1).getPageEntries()[0].getGps().getLongitude().intValue() == 2);
		promo = branch.getPromotionPagingList().loadPage(primaryUser, 1).getPageEntries()[0];
		sure(promo.getGps().getLongitude().intValue() == 2);
	}
	
	private static void sure(boolean b) {
		if (!b) {
			throw new RuntimeException();
		}
	}

}
