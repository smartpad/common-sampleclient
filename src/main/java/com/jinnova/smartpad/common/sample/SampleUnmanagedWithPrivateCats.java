package com.jinnova.smartpad.common.sample;

import static com.jinnova.smartpad.common.sample.Sample2.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.jinnova.smartpad.partner.IOperation;
import com.jinnova.smartpad.partner.IUser;

public class SampleUnmanagedWithPrivateCats {

	public static void createSampleBranches() throws SQLException, FileNotFoundException, IOException {	

		IUser[] user = new IUser[1];
		IOperation branch = createBranch(user, "b1", "z_entertain", "B1", "B1 SaiGon");
		IOperation branch1 = branch;
		IUser user1 = user[0];
		createMenu(user1, branch1, "branch1", new String[][] {
				{ "B1-Cat1", "B1-Cat1-Item1", "B1-Cat1-Item2", "B1-Cat1-Item3", "B1-Cat1-Item4" },
				{ "B1-Cat2", "B1-Cat2-Item1", "B1-Cat2-Item2", "B1-Cat2-Item3",
						"B1-Cat2-Item4", "B1-Cat2-Item5", "B1-Cat2-Item6" }, });

		branch = createBranch(user, "b2", "z_entertain_foods", "B2", "B2 SaiGon");
		IOperation branch2 = branch;
		IUser user2 = user[0];
		createMenu(user2, branch2, "branch2", new String[][] {
				{ "B2-Cat1", "B2-Cat1-Item1", "B2-Cat1-Item2", "B2-Cat1-Item3", "B2-Cat1-Item4" },
				{ "B2-Cat2", "B2-Cat2-Item1", "B2-Cat2-Item2", "B2-Cat2-Item3",
						"B2-Cat2-Item4", "B2-Cat2-Item5", "B2-Cat2-Item6" }, });

		branch = createBranch(user, "b3", "z_entertain_foods_fastfoods", "B3",
				"B3 SaiGon");
		IOperation branch3 = branch;
		IUser user3 = user[0];
		createMenu(user3, branch3, "branch3", new String[][] {
				{ "B3-Cat1", "B3-Cat1-Item1", "B3-Cat1-Item2", "B3-Cat1-Item3", "B3-Cat1-Item4" },
				{ "B3-Cat2", "B3-Cat2-Item1", "B3-Cat2-Item2", "B3-Cat2-Item3",
						"B3-Cat2-Item4", "B3-Cat2-Item5", "B3-Cat2-Item6" }, });
	}

}
