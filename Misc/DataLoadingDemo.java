import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataLoadingDemo {
	
	// populate tree_desc and tree_main table with description text from T5.txt;
	public static void main(String[] args) {
		BufferedReader br=null;
		Connection conn=null;
		PreparedStatement prepTree_desc=null;
		PreparedStatement prepTree_main=null;
		 try {
			 
			Class.forName("org.sqlite.JDBC");
			//fill in the path where your .db file located;
			conn = DriverManager.getConnection("jdbc:sqlite:E:\\ga-trees.db");
			// prepare SQL commands;
			prepTree_desc=conn.prepareStatement("insert into tree_desc (desc_id, full) values (?, ?);");
			prepTree_main=conn.prepareStatement("insert into tree_main (tree_id, cName, KEY, wood, uses, dist) values(?,?,?,?,?,?);");
			File file=new File("E://T5.txt");
			br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String temp="";
			// initiate collections for storing data extracted from txt file;
			ArrayList<String> cName=new ArrayList<>();
			ArrayList<String> Distribution=new ArrayList<>();
			ArrayList<String> Key=new ArrayList<>();
			ArrayList<String> Use=new ArrayList<>();
			ArrayList<String> Wood=new ArrayList<>();
			ArrayList<String> Description=new ArrayList<>();
			// read data from txt, and populate collections;
			while ((temp=br.readLine())!=null) 
			{
				if (temp.startsWith("DESCRIPTION")) 
				{
					
					Description.add(temp);
				}
				else if(temp.startsWith("USES"))
				{
					Use.add(temp);
				}
				else if(temp.startsWith("WOOD"))
				{
					Wood.add(temp);
				}
				else if(temp.startsWith("DISTRIBUTION"))
				{
					Distribution.add(temp);
				}
				else if(temp.startsWith("KEY"))
				{
					Key.add(temp);
				}
				else {
					
					cName.add(temp);
				}
			}
			// execute SQL commands;  
			for (int i = 0; i < Description.size(); i++) 
			{
				prepTree_desc.setInt(1, i+1);
				prepTree_desc.setString(2, Description.get(i));
				prepTree_desc.execute();

			}
			for (int i = 0; i < Description.size(); i++) {
				prepTree_main.setInt(1, i+1);
				prepTree_main.setString(2,cName.get(i));
				prepTree_main.setString(3, Key.get(i));
				prepTree_main.setString(4, Wood.get(i));
				prepTree_main.setString(5, Use.get(i));
				prepTree_main.setString(6, Distribution.get(i));
				prepTree_main.execute();
				
			}
			
			 br.close();
			 prepTree_desc.close();
			 prepTree_main.close();
			 conn.close();
		}
		 
		 catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	    
	}

}
