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
    public Database() {
        init();
    }
    private void init() 
    {
       // String sql="Create table ";
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Scholarship","root","");
        } catch (Exception ex) {
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
                st.executeUpdate("update login set status='out'");
                st.executeUpdate("update login set status='in' where username='"+userid+"'");
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
            st.executeUpdate("Insert into login(Name,username,password,email,contact_no) values('"+name+"','"+userid+"',MD5('"+passwd+"'),'"+email+"','"+mob+"')");
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
            st=conn.createStatement();
            st.executeUpdate("update login set status='out' where status='in'");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }   
    }
    
    public void updateScheme(String s)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("insert into personal_info(scheme_id,user) values((select ID from scholarship_type where scheme_name='"+s+"'),(select username from login where status= 'in'))");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
    public String getName()
    {
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where status='in'");
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
            st.executeUpdate("insert into academic values ((select application_id from personal_info where user=(select username from login where status='in')),'"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+a[4]+"','"+a[5]+"')");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
    
    public void del_academic(String[] a)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("delete from academic where College='"+a[1]+"' && app_ID=(select application_id from personal_info where user=(select username from login where status='in')) && Class='"+a[0]+"'");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
    public ResultSet getTable()
    {
        try{
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from academic where app_id=(select application_id from personal_info where user=(select username from login where status='in'))");
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
    
    public void resetPass(String oldpasswd,String passwd)
    {
        try{
            st=conn.createStatement();
            System.out.println(oldpasswd+" "+passwd);
            if(st.executeUpdate("update login set password=MD5('"+passwd+"') where status='in' and password=MD5('"+oldpasswd+"')")==0)
                JOptionPane.showMessageDialog(null,"Old Password doesn't Match");
            else
                JOptionPane.showMessageDialog(null,"Password changed successfully");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }
}
