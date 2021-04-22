package model;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.support.GenericXmlApplicationContext;

public class DBHandler implements DataBase{
	
	Connection con = null;

    public DBHandler() {
    }

    // DB 연결
    public Connection connect() {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
            DataSource ds = (DataSource) ctx.getBean("dataSource");
            con = ds.getConnection();
            System.out.println("정상적으로 연결되었습니다.");

        } catch(SQLException e) {
            System.err.println("con 오류:" + e.getMessage());
            e.printStackTrace();
        }

        return con;
    }

    // DB 연결 해제
    public int disconnect() {
        try {
            if(con != null) {
                con.close();
            }
        } catch (SQLException e) {
            return -1;
        }

        return 0;
    }
	

}
