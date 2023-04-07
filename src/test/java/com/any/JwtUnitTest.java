package com.any;

import com.any.common.vo.JwtUtil;
import com.any.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUnitTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt(){
        User user = new User();
        user.setUsername("caocao");
        user.setPhone("1234567890");
        String token = jwtUtil.createToken((user));
        System.out.println(token);
    }

    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0YzFkYTI2OS03Y2E3LTRmNGQtYmNiMi1mMzAwOTU4M2NmOTYiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDU2Nzg5MFwiLFwidXNlcm5hbWVcIjpcImNhb2Nhb1wifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY4MDc2MTI1NCwiZXhwIjoxNjgwNzYzMDU0fQ.TELPaqyr3P6Fe1Rjr5d13fYFHJlW6Mbt_liF4jtOZ_w";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }

    @Test
    public void testParseJwt2(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0YzFkYTI2OS03Y2E3LTRmNGQtYmNiMi1mMzAwOTU4M2NmOTYiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDU2Nzg5MFwiLFwidXNlcm5hbWVcIjpcImNhb2Nhb1wifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY4MDc2MTI1NCwiZXhwIjoxNjgwNzYzMDU0fQ.TELPaqyr3P6Fe1Rjr5d13fYFHJlW6Mbt_liF4jtOZ_w";
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }


}
