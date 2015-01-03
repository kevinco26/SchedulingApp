/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magicianagent;

import java.util.ArrayList;
import java.sql.*;

public class Magician {
    
    private static ArrayList<String> magicians = new ArrayList<String>();
    private static ArrayList<String> customersWithRemovedMagicians = new ArrayList<String>();
    private static ArrayList<String> holidaysWithRemovedMagicians = new ArrayList<String>();
     private static ArrayList<Timestamp> timeStamps = new ArrayList<Timestamp>();
  
    private static boolean isDropped = false; 
    
    //private static DatabaseConnection dbConnection;
    private static String [] mags;
    public Magician() // throws SQLException
    {
           
    }
    
    public static String[] getAllMagicians() //throws SQLException
    {

        try //throws SQLException
        {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT NAME FROM MAGICIAN");
            ResultSet resultSet = DatabaseConnection.getResult();
            
            while(resultSet.next())
                magicians.add(resultSet.getString(1));
            mags = new String[magicians.size()];
            for(int i = 0; i< magicians.size();i++)
            {
                mags[i] = magicians.get(i);
            }
        }
        catch (SQLException ex) 
        {
           ex.printStackTrace();
        }
            return mags;
            //return magicians;     
    }    
    public static void addMagician(String magicianName) 
    {   
        try {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("INSERT INTO MAGICIAN" + "(NAME)" + "VALUES (?)");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();
            
            ps.setString(1, magicianName);
            ps.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        magicians.add(magicianName);
    }
    public static void removeMagician(String magicianName) 
    {
        
         customersWithRemovedMagicians = new ArrayList<String>();
         holidaysWithRemovedMagicians = new ArrayList<String>();
         timeStamps = new ArrayList<Timestamp>();
       
        try {
            
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("DELETE FROM MAGICIAN WHERE NAME = ?");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();
            ps.setString(1, magicianName);
            ps.executeUpdate();
            
            DatabaseConnection.setQuery("SELECT CUSTOMER FROM BOOKINGTABLE WHERE MAGICIAN = ? ORDER BY DATE");
            PreparedStatement ps2 = DatabaseConnection.getPreparedstatement();
            ps2.setString(1, magicianName);
            ResultSet resultSet = ps2.executeQuery();
            
            DatabaseConnection.setQuery("SELECT HOLIDAY FROM BOOKINGTABLE WHERE MAGICIAN = ? ORDER BY DATE");
            PreparedStatement ps3 = DatabaseConnection.getPreparedstatement();
            ps3.setString(1, magicianName);
            ResultSet resultSet2 = ps3.executeQuery();
            
            DatabaseConnection.setQuery("SELECT DATE FROM BOOKINGTABLE WHERE MAGICIAN = ? ORDER BY DATE");
            PreparedStatement ps4 = DatabaseConnection.getPreparedstatement();
            ps4.setString(1, magicianName);
            ResultSet resultSet3 = ps4.executeQuery();

            while(resultSet.next()){
                customersWithRemovedMagicians.add(resultSet.getString(1));
                
            }
            while(resultSet2.next()){
                holidaysWithRemovedMagicians.add(resultSet2.getString(1));
            }
            
            while(resultSet3.next()){
                timeStamps.add(resultSet3.getTimestamp(1));
            }
            
            DatabaseConnection.setQuery("DELETE FROM BOOKINGTABLE WHERE MAGICIAN = ?");
            PreparedStatement ps5 = DatabaseConnection.getPreparedstatement();
            ps5.setString(1, magicianName);
            ps5.executeUpdate();
   
            isDropped = true;
            
            // Iterate through ALL the customers that had a magician dropped.
            // Book them if possible. If not possible, booking method will take care of that.
            for(int i = 0; i< customersWithRemovedMagicians.size();i++)
            {
                BookingClass.bookSpecialCustomer(customersWithRemovedMagicians.get(i), holidaysWithRemovedMagicians.get(i),timeStamps.get(i));
            }
            
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }          
    }
      
    public static boolean isMagDropped()
    {
        return isDropped;
    }
    public static String getFreeMagician(String holiday) 
    {
        String tempMagician = null;
        try {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT NAME FROM MAGICIAN WHERE NAME NOT IN ( SELECT MAGICIAN FROM BOOKINGTABLE WHERE HOLIDAY = ? )");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();
            
            ps.setString(1, holiday);

            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                tempMagician = resultSet.getString(1);
                //return tempMagician;
            }  
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tempMagician;
        
    }
    
    public void setMagician(ArrayList<String> magiciansList)
    {
        magicians = magiciansList;
    }
    
    public void printMag()
    {
        for(int i =0 ; i < magicians.size();i++)
        {
            System.out.println(magicians.get(i));
            System.out.println();
        }
    }
    
}
