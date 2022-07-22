
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tokenHandler")
public class tokenHandler extends HttpServlet {

    static Dotenv dotenv = Dotenv.load();

    static Instant now = Instant.now();
    //key between landing and middleman
    static String secret1 = dotenv.get("SECRET_1");
    //key between middleman and operator
    static String secret2 = dotenv.get("SECRET_2");


    static Key hmacKey1 = new SecretKeySpec(Base64.getDecoder().decode(secret1), 
                            SignatureAlgorithm.HS256.getJcaName());

    static Key hmacKey2 = new SecretKeySpec(Base64.getDecoder().decode(secret2), 
                            SignatureAlgorithm.HS256.getJcaName()); 
    
    static int serviceCost = 200;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //mapping the request body to get parameters
        System.out.println("🟣entered tokenHandler");
        String body = IOUtils.toString(request.getReader());    
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(body, new TypeReference<Map<String, String>>(){});
        String tokenFromLanding = map.get("token");
        System.out.println(tokenFromLanding);
        String phone = parseJwtBetweenLM(tokenFromLanding).getBody().get("phone").toString();
    
        System.out.println("🔵middleman recieved and decoded JWT from landing signed with key1: " +tokenFromLanding +"\n for phone number: " +phone);
        
        //create a post request to the operator server with new JWT
        
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            //create new token that is shared between middleman and operator
            String jwt2 = Jwts.builder()
                        .claim("phone", phone)
                        .setSubject("Phone Number")
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                        .signWith(hmacKey2)
                        .compact();

            HttpPost req = new HttpPost("http://localhost:8080/operatorapp/response");
            StringEntity params =new StringEntity("{\"operatorToken\":\""+jwt2+"\"}");
            req.addHeader("content-type", "application/json");
            req.addHeader("Accept","application/json");
            req.setEntity(params);
            System.out.println("🔵middleman sent post request to operator server with new JWT signed with key2");
            HttpResponse res = httpClient.execute(req);
            HttpEntity entity = res.getEntity();
            if(entity != null) {
                String responseString = EntityUtils.toString(entity, "UTF-8");
                //decode responseString to get the status
                String status = parseJwtBetweenMO(responseString).getBody().get("status").toString();
                int balance = (int) parseJwtBetweenMO(responseString).getBody().get("balance");
                if(status.equals("0")) {
                    System.out.println("🔵middleman recieved status 0 and new JWT signed with key2 from operator: "+responseString 
                        +"\n phone number: " +parseJwtBetweenMO(responseString).getBody().get("phone")
                        +"\n balance: " +parseJwtBetweenMO(responseString).getBody().get("balance")
                        +"\n operator: " +parseJwtBetweenMO(responseString).getBody().get("operator"));
                        //got the new JWT from operator, send it to landing signed with key 1
                    System.out.println("🔵middleman sent response JWT to landing server signed with key1");
                    //send status 3 if balance is less than service cost
                    if(balance < serviceCost){
                        System.out.println("🔴middleman sent status 3 to landing");
                        middleManResponse(3, response);
                    }
                    else{
                        middleManResponse(0, response);
                    }
                }
                else if (status.equals("1")){ //handling status 1, its 1 when user does not exists in the operator db
                    System.out.println("🔴middleman recieved status 1 from operator with JWT signed with key2: \n" + responseString);
                    middleManResponse(1, response);
                }

            }

        }catch (Exception ex) {
            System.out.println(ex);
        } finally {
            httpClient.close();
        }

    }
    public static Jws<Claims> parseJwtBetweenLM(String jwtString) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
        
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(secret1.getBytes("UTF-8"))
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }

    public static Jws<Claims> parseJwtBetweenMO(String jwtString) {
        
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey2)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }
    //creates a new a JWT with key1 to send it to landing server as a response 
    public static void middleManResponse(int status, HttpServletResponse response) throws InvalidKeyException, UnsupportedEncodingException{
        String jwt3 = Jwts.builder()
                        .claim("status", status)
                        .setSubject("Operator Response")
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS256, secret1.getBytes("UTF-8"))
                        .compact();
        
        response.setHeader("token", jwt3);
    }


}


