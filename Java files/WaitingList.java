

package magicianagent;

import java.sql.*;
import java.util.ArrayList;


public class WaitingList {
    public static ArrayList<String> customerWaitList = new ArrayList<String>();
    public static ArrayList<String> holidayWaitList = new ArrayList<String>();
    
    public WaitingList()
    {
        
    }
    
    public static void insertWaitList(String customer, String holiday, Timestamp date)
    {
        try {
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("INSERT INTO WAITLIST" + "(CUSTOMER,HOLIDAY,DATE)" + "VALUES (?,?,?)");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();

            ps.setString(1, customer); 
            ps.setString(2, holiday); 
            ps.setTimestamp(3, date); 
            
            ps.executeUpdate();
            
            customerWaitList.add(customer);
            holidayWaitList.add(holiday);
        
            
        }
        
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public static void deleteWaitList(String customer, String holiday)
    {
        
        try {
           
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("DELETE FROM WAITLIST WHERE CUSTOMER = ? AND HOLIDAY = ?");
            PreparedStatement ps = DatabaseConnection.getPreparedstatement();

            ps.setString(1, customer); 
            ps.setString(2, holiday); 
        
            ps.executeUpdate();
            
            BookingClass.bookCustomer(customer, holiday);
            //customerWaitList = new ArrayList<String>();
            //holidayWaitList = new ArrayList<String>();
           
              
                
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
     public static boolean isWaitListEmpty()
    {
        try {
           
            DatabaseConnection.connect();
            DatabaseConnection.setQuery("SELECT CUSTOMER FROM WAITLIST");
            ResultSet resultSet = DatabaseConnection.getResult();
            
            while(resultSet.next())
                return false;

           
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return true;
    }
}
