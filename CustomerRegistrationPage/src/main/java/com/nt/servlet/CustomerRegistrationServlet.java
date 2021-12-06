package com.nt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;



public class CustomerRegistrationServlet extends HttpServlet{
	private static final String CUSTOMER_INSERT_QUERY="INSERT INTO SERVLET_CUSTOMER VALUES(CUST_CNO.NEXTVAL,?,?,?,?)";
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		// get printwriter
		PrintWriter pw=res.getWriter();
		// set response content type
		res.setContentType("text/html");
		//read from data(req paramvalues)
		String name =req.getParameter("cname");
		String addrs =req.getParameter("cadd");
		float billAmt =Float.parseFloat(req.getParameter("billAmt"));
		long mobileNo=Long.parseLong(req.getParameter("mobileNo"));
		//get pooled JDBC con object
		try(Connection con=getPooledConnection();
				  PreparedStatement ps=con.prepareStatement(CUSTOMER_INSERT_QUERY);
				  ){
			//SET VALUES TO QUEREY PARAMS
			ps.setString(1, name);
			ps.setString(2,addrs);
			ps.setFloat(3, billAmt);
			ps.setLong(4,mobileNo);
			//execute the Query
			int result=ps.executeUpdate();
			if(result==1)
				pw.println("<h1 style='çolor:green'>Customer is registered </h1>");
			else
				pw.println("<h1 style='çolor:green'>Customer is not registered </h1>");

			//add hyperlink
			pw.println("<br><br><a href='customer_register.html'>home</a>");
			//close Stream
			pw.close();

		}//try
		catch(Exception se) {
			se.printStackTrace();
			pw.println("<h1 style='çolor:green'>Internal problem Try agian::"+se.getMessage()+"</h1>");
			//add hyperlink
			pw.println("<br><br><a href='customer_register.html'>home</a>");
		}//doGet()
	}
		
		@Override
		public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
			doGet(req,res);
		}
		
		
		public Connection getPooledConnection()throws Exception{
			// create intial context object
			InitialContext ic= new InitialContext();
			// get datasource object ref through lookup operations
			//DataSource ds=(DataSource)ic.lookup("DsJndi");
			//
			String jndiName=getServletConfig().getInitParameter("jndi");
			DataSource ds=(DataSource)ic.lookup(jndiName);
			// get pooled JDBC connection
			Connection con=ds.getConnection();
			return con;
		}

	

	}


