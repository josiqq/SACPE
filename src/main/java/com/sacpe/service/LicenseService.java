package com.sacpe.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class LicenseService {

    // ESTA ES TU "LLAVE SECRETA". DEBE SER LARGA Y COMPLEJA.
    // ¡NUNCA LA COMPARTAS! Es como la llave privada.
    private static final String SECRET_KEY_STRING = "v6C|\"5|F>Onm5[2?IV*^n~gQ&R4sve</<;RDpn(^*u*d8x>ja4";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    
    private boolean licenseValid = false;
    private String licenseMessage = "Licencia no verificada.";

    public LicenseService() {
        verifyLicense();
    }

    private void verifyLicense() {
        try {
            // Lee el archivo de licencia desde la misma carpeta donde se ejecuta el .jar
            String token = new String(Files.readAllBytes(Paths.get("license.dat")));

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            Date expiration = body.getExpiration();

            if (expiration.before(new Date())) {
                this.licenseValid = false;
                this.licenseMessage = "Su licencia ha expirado el " + expiration + ".";
            } else {
                this.licenseValid = true;
                this.licenseMessage = "Licencia válida hasta " + expiration + ".";
            }

        } catch (Exception e) {
            this.licenseValid = false;
            this.licenseMessage = "Error de licencia: No se pudo encontrar o validar el archivo license.dat.";
        }
    }

    public boolean isLicenseValid() {
        return licenseValid;
    }

    public String getLicenseMessage() {
        return licenseMessage;
    }
}