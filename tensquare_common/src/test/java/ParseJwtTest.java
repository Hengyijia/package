import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

/**
 * 解析jwt测试
 */
public class ParseJwtTest {
    public static void main(String[] args) {
        Claims claims = Jwts.parser()
                .setSigningKey("lijian")    //指定加密的"盐"
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMTEiLCJzdWIiOiLlvKDkuIkiLCJyb2xlIjoiYWRtaW4iLCJpYXQiOjE1Nzk1MTI2ODh9.be-n3hLlhh_LsNxY-V5fkQDxo4ODxfZzzvekN2zRgEU")
                .getBody();
        System.out.println("用户id：" + claims.getId());
        System.out.println("用户名：" + claims.getSubject());
        System.out.println("用户类型：" + claims.get("role"));
        System.out.println("登陆时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
    }
}
