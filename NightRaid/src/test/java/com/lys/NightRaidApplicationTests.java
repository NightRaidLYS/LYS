package com.lys;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lys.sys.entity.CusCustomer;
import com.lys.sys.mapper.CusCustomerMapper;
import com.lys.sys.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class NightRaidApplicationTests {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private CusCustomerMapper customerMapper;
    static String secret = "lys";
    @Test
    void contextLoads() {
        System.out.println(userMapper.selectList(null));
    }
    @Test
    void generatorToken(){
        Map<String,Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,60*60*24);
        String token = JWT.create()
                .withHeader(map) // 设置 header
                .withClaim("userid",1001) // 设置payload
                .withExpiresAt(calendar.getTime()) // 设置过期时间 60秒
                .sign(Algorithm.HMAC256(secret)); // 设置签名 secret 秘钥 需要对外保密
        System.out.println(token);
    }
    /**
     * Token 的校验
     */
    @Test
    void verifier(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTE5MjUyMzcsInVzZXJpZCI6MTAwMX0.wWXbgDn93lSvucShdbGhS9Ohoi8pmhjAe4pR098iwRE";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        System.out.println(decodedJWT.getClaim("userid").asInt());
    }
    @Test
    void test01(){
        List<CusCustomer> myCustomer = customerMapper.queryMyCustomer("lys");
        System.out.println(myCustomer);
    }

}
