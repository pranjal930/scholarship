/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dbms.Scholarship;

import java.sql.*;
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
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Scholarship","root","mysql");
        } 
        catch (SQLException ex) {
           System.out.println("Connection Exception : "+ex);
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
            System.out.println("Login Exception : "+ex);
        }
        return false;
    }
    
    public Boolean adminIDLogin(String adminid,String passwd)
    {
        //System.out.println(""+ adminid);
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("Select * from admin where username='"+adminid+"' and password=MD5('"+passwd+"')");
            rs.next();
            rs.getString(2);
            JOptionPane.showMessageDialog(null,"Logged in Successfully....");
            st.executeUpdate("update admin set status='in' where username='"+adminid+"'");
            usrname=String.valueOf(adminid);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"No match Found...!!!!");
            System.out.println("Admin Exception : "+ex);
        }
        return false;
    }
    public Boolean getUserName(String userid)
    {
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from login where username='"+userid+"'");
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Username Exception : "+ex);
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
            System.out.println("Registration Exception :"+ex);
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
            System.out.println("Scholarship Type Exception :"+ex);
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
            System.out.println("Update Login Status Exception :"+ex);
        }   
    }
    
    public void updateAdminlogin_status()
    {
        try
        {
            //System.out.println("user : "+usrname);
            st=conn.createStatement();
            st.executeUpdate("update admin set status='out' where username='"+usrname+"'");
        }
        catch(SQLException ex){
            System.out.println("Update Admin Status Exception :"+ex);
        }   
    }
    
    public void createProfile()
    {
        try
        {
            st=conn.createStatement();
            st.executeUpdate("insert into personal_info(username) values('"+usrname+"')");
        }
        catch(SQLException ex)
        {
            System.out.println("Create Profile Exception :"+ex);
        }
    }
    
    public void newApplication(String s)
    {
        int app_count=0;
        try{
            st=conn.createStatement();
            ResultSet ps=st.executeQuery("select ID from scholarship_type where scheme_name='"+s+"'");
            ps.next();
            String temp=ps.getString(1);
            ResultSet rs=st.executeQuery("select scheme_id from application_info where username='"+usrname+"'");
            while(rs.next())
            {
                if(rs.getString(1).equals(temp))
                {
                    System.out.println("Yes");
                    JOptionPane.showMessageDialog(null,"You have already applied for this scholarship");
                    return;
                }
                app_count++;
            }
            if(app_count<2)
            {
                st.executeUpdate("insert into application_info(scheme_id,username) values((select ID from scholarship_type where scheme_name='"+s+"'),'"+usrname+"')");
            }
            else
                JOptionPane.showMessageDialog(null,"Only 2 applications allowed per user...");
        }
        catch(SQLException ex){
            System.out.println("New Application Exception :"+ex);
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
            System.out.println("Name Exception : "+ex);
        }
        return null;
    }
    
    public String getSchemeID()
    {
        try
        {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select scheme_id from application_info where username='"+usrname+"'");
            rs.next();
            return rs.getString(1);
        } catch (SQLException ex) {
            System.out.println("Scheme ID Exception : "+ex);
        }
        return null;
    }
    
    public void add_academic(String[] a)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("insert into academic values ('"+usrname+"','"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+a[4]+"','"+a[5]+"')");
        }
        catch(SQLException ex){
            System.out.println("Add Academic values Exception :"+ex);
        }
    }
    
    public void del_academic(String[] a)
    {
        try
        {
            st=conn.createStatement();
            st.executeUpdate("delete from academic where College='"+a[1]+"' && username='"+usrname+"' && Class='"+a[0]+"'");
        }
        catch(SQLException ex){
            System.out.println("Delete Academic values Exception :"+ex);
        }
    }
    public ResultSet getTable()
    {
        try{
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select Class,College,Course,Result,Passing_year,Percentage from academic where username='"+usrname+"'");
            return rs;
        }
        catch(SQLException ex){
            System.out.println("Get Academic table Excepyion :"+ex);
        }
        return null;
    }
    
    public Boolean checkWindow(String userid)
    {
        try{
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from personal_info where username='"+userid+"'");
            return rs.next();
        }
        catch(SQLException ex){
            System.out.println("Check Window : "+ex);
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
            System.out.println("Get Values from login Exception :"+ex);
        }
        return null;
    }
}
