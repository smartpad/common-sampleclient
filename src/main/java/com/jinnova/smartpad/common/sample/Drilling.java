package com.jinnova.smartpad.common.sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.jinnova.smartpad.partner.SmartpadCommon;

public class Drilling {

	public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
		
		//initialize
		ClientSupport.dropDatabaseIfExists("localhost", null, "smartpad_drill", "root", "");
		ClientSupport.createDatabase("localhost", null, "smartpad_drill", "root", "", true);
		SmartpadCommon.initialize("localhost", null, "smartpad_drill", "root", "");
		ClientSupport.generateSystemCatalogs();
	}

}
