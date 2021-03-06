package com.gatlingbook.dao.impl;

import com.gatlingbook.dao.IUser;
import com.gatlingbook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserImpl implements IUser {
	
	private NamedParameterJdbcTemplate template;

	@Autowired
	public UserImpl(DataSource ds) {
		template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public User getUserbyUsername(String username) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", username);
		String sql = "SELECT * FROM user WHERE username=:name";
		
        List<User> list = template.query(
                    sql,
                    params,
                    userMapper);
		User result = null;
        if(list != null && !list.isEmpty()) {
        	result = list.get(0);
        }
		return result;
	}

	@Override
	public void registerUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", user.getUsername());
        params.put("email", user.getEmail());
        params.put("pw", user.getPassword());
		String sql = "insert into user (username, email, pw) values (:username, :email, :pw)";
        template.update(sql,  params);
	}

	private RowMapper<User> userMapper = (rs, rowNum) -> {
		User u = new User();
		u.setId(rs.getInt("user_id"));
		u.setEmail(rs.getString("email"));
		u.setUsername(rs.getString("username"));
		u.setPassword(rs.getString("pw"));
		return u;
	};
}
