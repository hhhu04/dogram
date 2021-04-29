package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import configuration.DateNow;
import model.DBHandler;
import model.DataBase;
import model.dao.daol.StroeDaoI;
import model.dto.CommunityDto;
import model.dto.StoreDto;
import model.dto.UserDto;

@Component
public class StoreDao implements StroeDaoI{
	
	private final DataBase db;
	
	public StoreDao(DBHandler db) {
		this.db = db;
		
	}
	
	public Long getNum() throws SQLException {
		Long num = 1L;
		Connection c = db.connect();
		Statement stat = c.createStatement();
		
		PreparedStatement preparedStatement = 
				c.prepareStatement("select num from store order by num desc limit 1");
		 ResultSet rs = preparedStatement.executeQuery();
	        if(rs.next()) {
	            return rs.getLong("num");
	        }
	        stat.close();

	        db.disconnect();
		
		return num;
	}
	
		public Long checkCookie(String cookie) throws ClassNotFoundException, SQLException {
		
		Connection c = db.connect();
		Statement stat = c.createStatement();
		
		PreparedStatement preparedStatement = 
				c.prepareStatement("select num from user where user.id = ?");
		
		preparedStatement.setString(1, cookie);
		
		 ResultSet rs = preparedStatement.executeQuery();
	        if(rs.next()) {
	        	
	            return rs.getLong("num");
	        }
	        stat.close();

	        db.disconnect();
		
		return -1L;
		
	}
	
	
	 public int create(StoreDto dto) throws ClassNotFoundException, SQLException{
		 	Long num = getNum();
		 	num++;
		 
		 	GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
	    	DateNow date = ctx.getBean("datenow",DateNow.class);
		 
		 
		 
	        Connection c = db.connect();

	        PreparedStatement preparedStatement = c.prepareStatement(
	                "insert into store (num,user_num,product,price,created_at,category,want_count,title) value(?,?,?,?,?,?,?,?)");

	        preparedStatement.setLong(1, num);
	        preparedStatement.setLong(2, dto.getUserNum());
	        preparedStatement.setString(3, dto.getProduct());
	        preparedStatement.setString(4, dto.getPrice());
	        preparedStatement.setString(5, date.date());
	        preparedStatement.setString(6, dto.getCategory());
	        preparedStatement.setInt(7, 0);
	        preparedStatement.setString(8, dto.getTitle());
	        preparedStatement.executeUpdate();
	        preparedStatement.close();

	        db.disconnect();
	        return 0;
	    }

	public List<StoreDto> read(StoreDto dto) throws SQLException {
		// TODO Auto-generated method stub
		List<StoreDto> list = new ArrayList<StoreDto>();
		 Connection c = db.connect();
			Statement stat = c.createStatement();
			
			PreparedStatement preparedStatement = 
					c.prepareStatement("select * from store");
			
			
			 ResultSet rs = preparedStatement.executeQuery();
			
		        while(rs.next()) {
		
		        	StoreDto dto2 = new StoreDto();
		        	
		        	Long num = rs.getLong("num");
		        	Long userNum = rs.getLong("user_num");
		        	String product = rs.getString("product");
		        	String price = rs.getString("price");
		        	String createdAt = rs.getString("created_at");
		        	String updatedAt = rs.getString("updated_at");
		        	String category = rs.getString("category");
		        	int wantCount = rs.getInt("want_count");
		        	String buyer = rs.getString("buyer");
		        	
		        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        	LocalDateTime dateTime = LocalDateTime.parse(createdAt, formatter);
		        	
		        	dto2.setNum(num);
		        	dto2.setUserNum(userNum);
		        	dto2.setProduct(product);
		        	dto2.setPrice(price);
		        	dto2.setCreatedAt(dateTime);
		        	dto2.setUpdatedAt(dateTime);
		        	dto2.setCategory(category);
		        	dto2.setWantCount(wantCount);
		        	dto2.setBuyer(userNum);
		        	
		        	list.add(dto2);
		        	
		        }
		
		return list;
	}
	

}
