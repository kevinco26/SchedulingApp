/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magicianagent;

import java.sql.*;

public abstract class DatabaseConnection {
    
    private static String query;
    private static PreparedStatement preparedStatement;
    private static final String DATABASE_URL = "jdbc:derby://localhost:1527/sample";
    private static ResultSet resultSet;
   
    private static Connection connection;
    
    
    public DatabaseConnection() 
    {
        
       
    }
    public static void connect()
    {
        try{
         connection = DriverManager.getConnection(DATABASE_URL,"app","password"); 
        }
         catch(SQLException ex){
             ex.printStackTrace();
         }
    }
    public static void setQuery(String q) //throws SQLException
    {
        // Check to see if its a preparedStatement or not(checking to see if there is a placeholder '?')
        try{
        if(q.indexOf('?') == -1)    // If not, then regular query
            resultSet = connection.createStatement().executeQuery(q);
        else                        // PreparedStatement.
            preparedStatement =  connection.prepareStatement(q);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
   
    public static ResultSet getResult()
    {     
        return resultSet;
    }
    public static PreparedStatement getPreparedstatement()
    {
        return preparedStatement;
    }        
       
    
   
         
       
}
