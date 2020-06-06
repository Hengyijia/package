import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * 创建jwt测试
 */
public class CreateJwtTest {
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("111")
                .setSubject("张三")
                .claim("role", "admin") //自定义的claim
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"lijian");//前面是hs256算法，后面是"盐"

        System.out.println(jwtBuilder.compact());
    }
}

