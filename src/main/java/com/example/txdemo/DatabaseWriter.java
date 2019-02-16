package com.example.txdemo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class DatabaseWriter {

	@Autowired
	DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void init() {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		initializeTables();
	}
	
	public void initializeTables() {
		jdbcTemplate.update("CREATE TABLE vals (val VARCHAR(32))");
	}
	
	public boolean writeToDatabase(String value) {
		System.out.println(String.format("Writing to database: %s", value));
		return jdbcTemplate.update("INSERT INTO vals(val) VALUES(?)", value) > 0;
	}
	
	public long numberofRecords() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM vals", Long.class);
	}

	public List<String> readRecords() {
		return jdbcTemplate.query("SELECT val FROM vals", new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> result = new ArrayList<String>();
				while (rs.next()) {
					System.out.println("...");
					result.add(rs.getString(1));
				}
				return result;
			}
		});
	}
	
}
