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
    private static String temp;
    private static String application_id;
    public Database() {
        //usrname=new String();
        init();
    }
    private void init() 
    {
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
    }//Constructor to establish connection
        
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
    }//check database for student ID and password match
    
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
    }//check database for admin ID and password match
    
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
    }//compare the new username with the database to check if its already in use
    
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
    }//register new user(student) 
    
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
    }//get name and details of scholarships for Home page and displaying list in select scholar class
    
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
    }//to logout the user(student)
    
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
    }//to logout the admin
    
    public void createProfile()
    {
        try
        {
            st=conn.createStatement();
            st.executeUpdate("insert into personal_info(username) values('"+usrname+"')");
            st.executeUpdate("insert into caste_details(username) values('"+usrname+"')");
            st.executeUpdate("insert into academic(username) values('"+usrname+"')");
            st.executeUpdate("insert into address_info(username) values('"+usrname+"')");
            System.out.println("done");
        }
        catch(SQLException ex)
        {
            System.out.println("Create Profile Exception :"+ex);
        }
    }//to create a new profile
    
    public void newApplication(String s)
    {
        int app_count=0;
        try{
            st=conn.createStatement();
            ResultSet ps=st.executeQuery("select ID from scholarship_type where scheme_name='"+s+"'");
            ps.next();
            temp=ps.getString(1);
            ResultSet rs=st.executeQuery("select scheme_id from application_info where username='"+usrname+"'");
            while(rs.next())
            {
                if(rs.getString(1).equals(temp))
                {
                    System.out.println("Yes");
                    JOptionPane.showMessageDialog(null,"You have already applied for this scholarship");
                    return;
                }//to avoid applying for same scholarship twice
                app_count++;
            }
            if(app_count<2)
            {
                st.executeUpdate("insert into application_info(scheme_id,username) values((select ID from scholarship_type where scheme_name='"+s+"'),'"+usrname+"')");
                ResultSet as=st.executeQuery("select application_id from application_info where username='"+usrname+"' and scheme_id='"+temp+"'");
                as.next();
                application_id=as.getString(1);
                 st.executeUpdate("insert into account_details(application_id) values('"+application_id+"')");
                System.out.println(application_id);
            }//not allowing more than two application per user
            else
                JOptionPane.showMessageDialog(null,"Only 2 applications allowed per user...");
        }
        catch(SQLException ex){
            System.out.println("New Application Exception :"+ex);
        }
    }//to create new applications 
    
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
    }//get the name of the user currently logged in
    
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
    }//to apply income constraints for economically backward classes
    
    public void add_academic(String[] a)
    {
        try{
            st=conn.createStatement();
            st.executeUpdate("update academic set Class='"+a[0]+"',College='"+a[1]+"',Course='"+a[2]+"',Result='"+a[3]+"',Passing_year='"+a[4]+"',Percentage='"+a[5]+"'where username='"+usrname+"'");
        }
        catch(SQLException ex){
            System.out.println("Add Academic values Exception :"+ex);
        }
    }//to add academic info for the user
    
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
    }//to delete academic info for user
    
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
    }//get the academic table for a user
    
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
    }//to check if already applied for scholarship or not and redirect accordingly to the appropriate window
    
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
    }//to auto fill Name, Email, Contact No. 
    
    public void updateReligion(String religion,String category)
    {
        try
        {
            st=conn.createStatement();
            System.out.println("update caste_details set religion='"+religion+"' and category='"+category+"' where username='"+usrname+"'");
            st.executeUpdate("update caste_details set religion='"+religion+"', category='"+category+"' where username='"+usrname+"'");
        }
        catch(SQLException ex)
        {
            System.out.println("Update Religion exception :"+ex);
        }
        
    }
    public void updateaccount(String accno,String IFSC,String bName,String bankName)
    {
        try
        {
            st=conn.createStatement();
            //System.out.println("update caste_details set religion='"+religion+"' and category='"+category+"' where username='"+usrname+"'");
             ResultSet rs=st.executeQuery("select * from bank_details where IFSC_code='"+IFSC+"'");
             if(rs.next()==false)
             {
            st.executeUpdate("insert into bank_details(IFSC_code,branch_name,bank_name) values('"+IFSC+"','"+bName+"','"+bankName+"')");
             }
            st.executeUpdate("update account_details set account_no='"+accno+"' ,IFSC_code='"+IFSC+"' where application_id= (select application_id from application_info where username='"+usrname+"')");
            
        }
        catch(SQLException ex)
        {
            System.out.println("Update account exception :"+ex);
        }
        
    }
     public void updatePersonal(String aadharno,String DOB,String gender,String marital_status,String par_contact,String address,String state,String district,String taluka,String village,String pincode )
    {
        try
        {
            st=conn.createStatement();
            //System.out.println("update caste_details set religion='"+religion+"' and category='"+category+"' where username='"+usrname+"'");
            st.executeUpdate("update personal_info set aadhar_no='"+aadharno+"', DOB='"+DOB+"',gender='"+gender+"',marital_status='"+marital_status+"',par_contact='"+par_contact+"' where username='"+usrname+"'");
            st.executeUpdate("update address_info set address='"+address+"', state='"+state+"',district='"+district+"',taluka='"+taluka+"',village='"+village+"',pincode='"+pincode+"' where username='"+usrname+"'");
        }
        catch(SQLException ex)
        {
            System.out.println("Update Religion exception :"+ex);
        }
        
    }
}
