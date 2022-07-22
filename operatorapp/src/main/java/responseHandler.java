
import java.io.IOException;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/response")
public class responseHandler extends HttpServlet {

    static Dotenv dotenv = Dotenv.load();
    
    static String db_url = dotenv.get("DB_URL");
    static String user = dotenv.get("DB_USER"); 
    static String password = dotenv.get("DB_PASSWORD");
    static Connection conn;
    static Statement statement;

    static Instant now = Instant.now();
    static String secret = dotenv.get("SECRET_2");

    static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                            SignatureAlgorithm.HS256.getJcaName()); 


    //doPost handles the POST requests from the client
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //mapping the request body to get parameters
        String body = IOUtils.toString(request.getReader());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(body, new TypeReference<Map<String, String>>(){});
        String token = map.get("operatorToken");
        //decoding token recieved from middleman
        String phone = parseJwt(token).getBody().get("phone").toString();
        System.out.println("🟢operator recieved and decoded JWT from middleman signed with key2: "+ token +"\n for phone number: " +phone);
        resultObject obj = doesUserExists(phone);
        String jwt;
        if(obj != null){
            jwt = createJWT(phone, obj.getBalance(), obj.getOperator(), 0);
        }
        else{
            jwt = createJWT(phone, 0, "", 1);
        }
        
        response.setHeader("token", jwt);
        response.getWriter().println(response.getHeader("token"));

        
    }
    //returns the resultObject of the user if exists, otherwise returns null
    public static resultObject doesUserExists(String phone){
        resultObject result = new resultObject();
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(db_url, user, password);
            String sql = "SELECT * FROM users WHERE phone = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                    System.out.println("🟢User exists with phone "+ phone);
                    result.setBalance(rs.getInt("balance"));
                    result.setOperator(rs.getString("operator"));
                    result.setPhone(rs.getString("phone"));
                    result.setStatus(0);
                    conn.close();
                    ps.close();
                    return result;
            }
            conn.close();
            ps.close();
            System.out.println("🔴User does not exist! with phone "+ phone);
            return null;
        } catch (SQLException e) {
            System.out.println("🔴Failed to check user!");
            e.printStackTrace();
            return null;
        }
    }

    public static String createJWT(String phone, int balance, String operator, int status){
        String jwt = Jwts.builder()
            .claim("phone", phone)
            .claim("balance", balance)
            .claim("operator", operator)
            .claim("status", status)
            .setSubject("User Data")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
            .signWith(hmacKey)
            .compact();
        return jwt;
    }

    public static Jws<Claims> parseJwt(String jwtString) {
        
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }

}


