package com.jinnova.smartpad.common.sample;

import static com.jinnova.smartpad.partner.ICatalogField.F_NAME;
import static com.jinnova.smartpad.partner.SmartpadCommon.*;

import java.sql.SQLException;

import com.jinnova.smartpad.partner.ICatalog;
import com.jinnova.smartpad.partner.ICatalogItem;
import com.jinnova.smartpad.partner.IOperation;
import com.jinnova.smartpad.partner.IPartnerManager;
import com.jinnova.smartpad.partner.IUser;
import com.jinnova.smartpad.partner.SmartpadCommon;

public class SampleUnmanaged {
	
	private static IUser user;

	private static IUser createNormalSeller(String login, String syscatId) throws SQLException {
		IPartnerManager pm = SmartpadCommon.partnerManager;
		IUser primaryUser;
		primaryUser = pm.createPrimaryUser(login, login);
		//pm.getUserPagingList().put(primaryUser, primaryUser);
		//primaryUser.createBranch();
		IOperation branch = primaryUser.getBranch();
		branch.setSystemCatalogId(syscatId);
		primaryUser.updateBranch();
		return primaryUser;
	}

	public static void createItems() throws SQLException {
		
		//systemUser = SmartpadCommon.partnerManager.systemUser;
		user = (IUser) createNormalSeller("ngoc", "z_household");
		//"wash_type", "wash_load", "max_rpm", "capacity", "water", "power", "sizes", "weight", "madein"
		
		ICatalog cat = SmartpadCommon.partnerManager.getSystemCatalog("z_elec_appliance_washer");
		createItem(cat, washerFields, "Sanyo ASW-D90VT", "SANYO", "Máy giặt lồng đứng", 9,   850, 65, 122, 160, "590 x 564 x 988", 41, "Việt Nam");
		createItem(cat, washerFields, "LG WFD8525DD",    "LG",    "Máy giặt lồng đứng", 8.5, 850, 79, 410, 0,   "540 x 910 x 540", 39, null);
		createItem(cat, washerFields, "Sanyo ASW-U850HT", "Sanyo","Máy giặt lồng nghiêng",8.5,840,62, 410, 0,   "589 x 620 x 988", 43, null);
		
		//"material", "length", "width", "thick", "madein"
		cat = SmartpadCommon.partnerManager.getSystemCatalog("z_household_mattress");
		createItem(cat, mattressFields, "Nệm cao su Vạn Thành", "Vạn Thành", "Cao su", 160, 200, 5, "Việt Nam");
		createItem(cat, mattressFields, "Nệm cao su Liên Á Classic 180x200x10cm", "Liên Á", "Cao su", 180, 200, 10, "Việt Nam");
		createItem(cat, mattressFields, "Nệm cao su Venus Vạn Thành 100x200x10cm", "Vạn Thành", "Cao su", 100, 200, 10, "Việt Nam");
		createItem(cat, mattressFields, "Nệm bông ép Hàn Quốc Cuscino 140x200x5cm", "Cuscino", "Bông tấm PE ép", 140, 200, 5, "Hàn Quốc");
		
		//resort
		cat = SmartpadCommon.partnerManager.getSystemCatalog("z_entertain_resort");
		createItem(cat, null, "Thiên Ý Resort", "Thiên Ý Resort");
		createItem(cat, null, "Vinpearl Resort", "Vinpearl Resort");
		
		//áo quần
		//"trademark", "material", "color", "style", "size", "madein", "sex"
		cat = SmartpadCommon.partnerManager.getSystemCatalog("z_fashion_clothes");
		createItem(cat, clothFields, "Váy đầm dạ hội trẻ trung quyến rũ,đa dạng cho phụ nữ Việt Nam DV144",
				"DV", "Thun", "Đen/trắng", "Đầm liền", "Freesize", "Việt Nam", "Nữ");
		createItem(cat, clothFields, "Đầm body Ngọc Trinh 2 dây đơn giản sang trọng DV145", 
				"DV", "Thun", "Đỏ/trắng", "Đầm liền", "Freesize", "Việt Nam", "Nữ");
	}
	
	private static void createItem(ICatalog cat, String[] fieldNames, Object... data) throws SQLException {
		ICatalogItem item = cat.getCatalogItemPagingList().newEntryInstance(user);
		item.setField(F_NAME, String.valueOf(data[0]));
		item.setBranchName((String) data[1]);
		
		if (fieldNames == null) {
			return;
		}
		int offset = 2;
		for (int i = 0; i < fieldNames.length; i++) {
			item.setField(fieldNames[i], data[offset + i] == null ? null : String.valueOf(data[offset + i]));
		}
		cat.getCatalogItemPagingList().put(user, item);
	}
}
