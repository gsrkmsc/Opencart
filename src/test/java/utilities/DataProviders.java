package utilities;

import java.io.IOException;
import org.testng.annotations.DataProvider;

public class DataProviders {

    // DataProvider 1: Fetches all data from the specified Excel sheet
    @DataProvider(name = "LoginData")
    public String[][] getData() throws IOException {
    	String path = ".\\testData\\Opencart_LoginData.xlsx"; // Path to your excel file
    	Excelutility xlutil = new Excelutility(path);  // Creating an object of ExcelUtility

        int totalrows = xlutil.getRowCount("Sheet1");
        int totalcols = xlutil.getCellCount("Sheet1", 1);

        String logindata[][] = new String[totalrows][totalcols]; // Created a 2D array

        for (int i = 1; i <= totalrows; i++) { // Read data from row 1 (skipping header)
            for (int j = 0; j < totalcols; j++) { // 0 is the first column
                logindata[i - 1][j] = xlutil.getCellData("Sheet1", i, j);
            }
        }
        return logindata; // Returning 2D array
    }
    
    // You can add more DataProviders here for Registration, Checkout, etc.
}
