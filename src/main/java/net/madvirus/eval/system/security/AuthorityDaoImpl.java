package net.madvirus.eval.system.security;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AuthorityDaoImpl implements AuthorityDao {
    private JdbcTemplate jdbcTemplate;

    public AuthorityDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Authority> selectByUserId(String id) {
        return jdbcTemplate.query("select * from Authority where user_id = ?",
                new String[] {id},
                (rs, rowNum) -> new Authority(rs.getString("user_id"), rs.getString("role")));
    }
}
