/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magicianagent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author kevincohen
 */
public class Holiday {
    private static ArrayList<String> holidays = new ArrayList<String>();
    //private static DatabaseConnection dbConnection;
    private static String [] holArray;
    public Holiday() // throws SQLException
    {
       
    }
    
    public static String[] getAllHolidays() //throws SQLException
    {

        try //throws SQLException
        {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT NAME FROM HOLIDAY");
            ResultSet resultSet = DatabaseConnection.getResult();
            
            while(resultSet.next())
                holidays.add(resultSet.getString(1));
            holArray = new String[holidays.size()];
            for(int i = 0; i< holidays.size();i++)
            {
                holArray[i] = holidays.get(i);
            }
        }
        catch (SQLException ex) 
        {
           ex.printStackTrace();
        }
            return holArray;
             
    }    
    public static void addHoliday(String holidayName) 
    {   
        try {
            DatabaseConnection.setQuery("INSERT INTO HOLIDAY" + "(NAME)" + "VALUES (?)");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();
            
            ps.setString(1, holidayName);
            ps.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    public void removeHoliday(String holidayName) 
    {
       
        try {
            DatabaseConnection.setQuery("DELETE FROM HOLIDAY WHERE NAME = ?");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();
            
            ps.setString(1,holidayName);
            ps.executeUpdate();
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }          
    }
    
    public void setHoliday(ArrayList<String> holidayList)
    {
        holidays = holidayList;
    }
    
}
