package com.hiretrack.web;
import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/admin") public class AdminController {final JdbcTemplate db;AdminController(JdbcTemplate d){db=d;}
 @GetMapping("/summary") Map<String,Object> summary(){Map<String,Object> m=new LinkedHashMap<>();for(String t:List.of("users","companies","recruiters","candidates","jobs","applications","interviews"))m.put(t,db.queryForObject("select count(*) from "+t,Integer.class));return m;}
}
