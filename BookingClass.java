/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magicianagent;


import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;


public class BookingClass {
    
   
    private static boolean booked;
    public BookingClass() 
    {
        
    }
  
    public static void bookCustomer(String customerName,String holidayName)
    {
        String tempMagician = Magician.getFreeMagician(holidayName);
        Timestamp currentTimeStamp = new Timestamp(Calendar.getInstance().getTime().getTime() );
        try {
            DatabaseConnection.connect();
            
            if(tempMagician == null)
            {
                booked = false;
                
                    WaitingList.insertWaitList(customerName,holidayName,currentTimeStamp); 
            }
            else
            {
                DatabaseConnection.setQuery("INSERT INTO BOOKINGTABLE" + "(CUSTOMER,HOLIDAY,MAGICIAN,DATE)" + "VALUES (?,?,?,?)");
                PreparedStatement ps = DatabaseConnection.getPreparedstatement();
                
                
                ps.setString(1, customerName);
                ps.setString(2, holidayName);
                ps.setString(3, tempMagician);
                ps.setTimestamp(4, currentTimeStamp);
                
                ps.executeUpdate();
                
                booked = true;
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
   
    
    public static void cancelCustomerBooking(String customerName, String holidayName)
    {
        DatabaseConnection.connect();
        PreparedStatement ps;
        ResultSet result;
        try {
              DatabaseConnection.setQuery("SELECT CUSTOMER FROM BOOKINGTABLE WHERE CUSTOMER = ? AND HOLIDAY = ?");
              ps = DatabaseConnection.getPreparedstatement();
              ps.setString(1, customerName);
              ps.setString(2, holidayName);
              result = ps.executeQuery();
              
              // The customer is in bookingTable
              if(result.next())
              {
                DatabaseConnection.setQuery("DELETE FROM BOOKINGTABLE WHERE CUSTOMER = ? AND HOLIDAY = ?");
                ps = DatabaseConnection.getPreparedstatement();
                ps.setString(1, customerName);
                ps.setString(2, holidayName);
   
                ps.executeUpdate();
               
                // CHECK WAITLIST
                DatabaseConnection.setQuery("SELECT CUSTOMER FROM WAITLIST WHERE HOLIDAY = ? ORDER BY DATE");
                ps = DatabaseConnection.getPreparedstatement();
                ps.setString(1, holidayName);
             
                result = ps.executeQuery();
                
                while(result.next()){
                   
                    WaitingList.deleteWaitList(result.getString(1), holidayName);
                }
              }
              else
              {
                  
                DatabaseConnection.setQuery("SELECT FROM WAITLIST WHERE CUSTOMER = ? AND HOLIDAY = ?");
                ps = DatabaseConnection.getPreparedstatement();
                ps.setString(1, customerName);
                ps.setString(2, holidayName);
                result = ps.executeQuery();
                if(result.next())
                {
                
                    DatabaseConnection.setQuery("DELETE FROM WAITLIST WHERE CUSTOMER = ? AND HOLIDAY = ?");
                    ps = DatabaseConnection.getPreparedstatement();
                    ps.setString(1, customerName);
                    ps.setString(2, holidayName);

                    ps.executeUpdate();
                
                }
                else
                    JOptionPane.showMessageDialog(null,"Customer with specified holiday does not exist");
              }
              //Customer doe not exist
              
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
   
    
    public static void bookSpecialCustomer(String customerName,String holidayName,Timestamp time)
    {
        String tempMagician = Magician.getFreeMagician(holidayName);
       
        try {
            DatabaseConnection.connect();
            
            if(tempMagician == null)
            {
                booked = false;
                
                    WaitingList.insertWaitList(customerName,holidayName,time); 
            }
            else
            {
                DatabaseConnection.setQuery("INSERT INTO BOOKINGTABLE" + "(CUSTOMER,HOLIDAY,MAGICIAN,DATE)" + "VALUES (?,?,?,?)");
                PreparedStatement ps = DatabaseConnection.getPreparedstatement();
                
                
                ps.setString(1, customerName);
                ps.setString(2, holidayName);
                ps.setString(3, tempMagician);
                ps.setTimestamp(4, time);
                
                ps.executeUpdate();
                
                booked = true;
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    public static boolean succesFullyBooked()
    {
        return booked;
    }
    
    public static ArrayList<String> getWaitlistStatus()
    {
        ArrayList<String> waitArray = new ArrayList<String>();
        try {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT CUSTOMER,HOLIDAY FROM WAITLIST ORDER BY HOLIDAY,DATE");
            ResultSet resultSet = DatabaseConnection.getResult();
   

            while(resultSet.next()){
           
                waitArray.add(resultSet.getString("CUSTOMER"));
                waitArray.add(resultSet.getString("HOLIDAY"));     
            }

        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return waitArray;
    }
    
    public static ArrayList<String> getMagicianStatus(String magician)
    {
         ArrayList<String> magicianArray = new ArrayList<String>();
        try {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT CUSTOMER,HOLIDAY FROM BOOKINGTABLE WHERE MAGICIAN = ?");
            
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();  
            ps.setString(1, magician);
            
           // ps.executeUpdate();
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                magicianArray.add(resultSet.getString("CUSTOMER"));
                magicianArray.add(resultSet.getString("HOLIDAY"));
                
            }
            
            
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        return magicianArray;
    }
    
    public static ArrayList<String> getHolidayStatus(String holiday)
    {
        ArrayList<String> holidayArray = new ArrayList<String>();
        try {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT CUSTOMER,MAGICIAN FROM BOOKINGTABLE WHERE HOLIDAY = ?");
            
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();  
            ps.setString(1, holiday);
            
           
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                
                holidayArray.add(resultSet.getString("CUSTOMER"));
                holidayArray.add(resultSet.getString("MAGICIAN"));
           
            }         
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return holidayArray;
    }
    public static void removeCustomer(String customerName)
    {
           
        try {
            DatabaseConnection.connect();
         
            DatabaseConnection.setQuery("DELETE FROM BOOKINGTABLE WHERE CUSTOMER = ?");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();

            ps.setString(1, customerName);
            
            ps.executeUpdate();
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
}
