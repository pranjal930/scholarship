/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dbms.Scholarship;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author gupta
 */
public class Database {

    Connection conn;
    Statement st;

    private static String usrname;
    public Database() {
        //usrname=new String();
        init();
    }
    private void init() 
    {
       // String sql="Create table ";
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Scholarship","root","");
        } 
        catch (SQLException ex) {
           System.out.println("Exception : "+ex);
        }
        catch (ClassNotFoundException ex) {
           System.out.println("Exception : "+ex);
        }
      
    }
        
    public Boolean userLogin(String userid,String passwd)
    {
        //System.out.println(""+ userid);
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("Select * from login where username='"+userid+"' and password=MD5('"+passwd+"')");
            rs.next();
            rs.getString(2);
                JOptionPane.showMessageDialog(null,"Logged in Successfully....");
                st.executeUpdate("update login set status='in' where username='"+userid+"'");
                usrname=String.valueOf(userid);
                //System.out.println(usrname);
                return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"No match found...!!!!");
            System.out.println("Exception : "+ex);
        }
        return false;
    }
    
    public void adminIDLogin(String adminid,String passwd)
    {
        //System.out.println(""+ adminid);
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("Select * from admin where username='"+adminid+"' and password=MD5('"+passwd+"')");
            rs.next();
            rs.getString(2);
            JOptionPane.showMessageDialog(null,"Logged in Successfully....");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"No match Found...!!!!");
            System.out.println("Exception : "+ex);
        }
    }
    public Boolean getUserName(String userid)
    {
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where username='"+userid+"'");
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Exception : "+ex);
        }
        return false;
    }
    public Boolean addUser(String name,String userid,String passwd,String email,String mob)
    {   
        try {
            st=conn.createStatement();
            st.executeUpdate("Insert into login(Name,username,password,email,contact_no,status) values('"+name+"','"+userid+"',MD5('"+passwd+"'),'"+email+"','"+mob+"','in')");
            usrname=String.valueOf(userid);
            return true;
        }
        catch(SQLException ex)
        {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null,ex.getMessage()+"\nEntered email is already present in the database");
            return false;
        }
    }
    public ResultSet scholarType()
    {
        ResultSet rs=null;
        try {
            st=conn.createStatement();
            rs=st.executeQuery("select * from scholarship_type");
            rs.next();
        } catch (SQLException ex) {
            System.out.println(""+ex);
        }
        return rs;
    }
    
    public void updateLogin_status()
    {
        try
        {
            //System.out.println("user : "+usrname);
            st=conn.createStatement();
            st.executeUpdate("update login set status='out' where username='"+usrname+"'");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }   
    }
    
    public void newApplication(String s)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("insert into personal_info(scheme_id,user) values((select ID from scholarship_type where scheme_name='"+s+"'),'"+usrname+"')");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
    public String getName()
    {
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where username='"+usrname+"'");
            rs.next();
            return rs.getString(1);
        } catch (SQLException ex) {
            System.out.println("Exception : "+ex);
        }
        return null;
    }
    
    public String getSchemeID()
    {
        try
        {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select scheme_id from personal_info where user='"+usrname+"'");
            rs.next();
            return rs.getString(1);
        } catch (SQLException ex) {
            System.out.println("Exception : "+ex);
        }
        return null;
    }
    
    public void add_academic(String[] a)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("insert into academic values ((select application_id from personal_info where user='"+usrname+"'),'"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+a[4]+"','"+a[5]+"')");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
    
    public void del_academic(String[] a)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("delete from academic where College='"+a[1]+"' && app_ID=(select application_id from personal_info where user='"+usrname+"') && Class='"+a[0]+"'");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
    public ResultSet getTable()
    {
        try{
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from academic where app_id=(select application_id from personal_info where user='"+usrname+"')");
            return rs;
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        return null;
    }
    
    public Boolean checkWindow(String userid)
    {
        try{
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from personal_info where user='"+userid+"'");
            return rs.next();
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        return false;
    }
    public ResultSet getValues( )
    {
             try{
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select Name,email,contact_no from login where username='"+usrname+"'");
            rs.next();
            return rs;
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        return null;
    }
}
