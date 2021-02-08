package com.trecapps.userservice.services;

import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.util.Base64;
import java.util.Calendar;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.trecapps.userservice.models.primary.TrecOauthClient;
import com.trecapps.userservice.models.primary.TrecOauthClientOne;
import com.trecapps.userservice.models.secondary.InvalidSession;
import com.trecapps.userservice.repositories.secondary.InvalidSessionRepo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.repositories.primary.TrecAccountRepo;

@Service
public class JwtTokenService {
	
	@Value("${trec.key.public}")
	String publicKeyStr;
	
	@Value("${trec.key.private}")
	String privateKeyStr;
	
	RSAPublicKey publicKey;
	
	RSAPrivateKey privateKey;
	
	@Autowired
	TrecAccountService accountService;
	
	@Autowired
	TrecAccountRepo accountRepo;

	@Autowired
	InvalidSessionRepo invalidSessionRepo;

	@Autowired
	TrecOauthClientService clientService;

	// chose a Character random from this String
	final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "0123456789"
			+ "abcdefghijklmnopqrstuvxyz";
	final int RANDOM_STRING_LENGTH = 10;

	private String generateRandomString()
	{
		StringBuilder sb = new StringBuilder();
		for(int c = 0; c < RANDOM_STRING_LENGTH; c++)
		{
			int ch = (int) (Math.random() * AlphaNumericString.length());
			sb.append(AlphaNumericString.charAt(ch));
		}
		return sb.toString();
	}
	
	private static final long TEN_MINUTES = 600_000;
	
	private boolean setKeys()
	{
		if(publicKey == null)
		{
			System.out.println("Key location is " + publicKeyStr);
			File publicFile = new File(publicKeyStr);

			Scanner keyfis;
			try {
				String encKey = "";
				
				keyfis = new Scanner(publicFile);
				
				while(keyfis.hasNext())
				{
					encKey += keyfis.next();
				}
				
				keyfis.close();
				
				System.out.println("Private Key is " + encKey);
				
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(encKey));
				
				publicKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
				
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		
		if(privateKey == null)
		{
			File privateFile = new File(privateKeyStr);
			
			try (FileReader keyReader = new FileReader(privateFile);
				      PemReader pemReader = new PemReader(keyReader)) {
				 
				        PemObject pemObject = pemReader.readPemObject();
				        byte[] content = pemObject.getContent();
				        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
				        privateKey =  (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privKeySpec);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		return privateKey != null && publicKey != null;
	}

	public String generateSession(TrecAccount account, TrecOauthClient clientId, String currentSessionId)
	{
		if(currentSessionId != null)
		{
			if(currentSessionId.length() != 10)
				throw new IllegalArgumentException("CurrentSessionId must be null or contain 10 characters");
		}

		if(!setKeys())
			return null;

		if(!verifyUnlocked(account))
			return null;

		Date now = new Date(Calendar.getInstance().getTime().getTime());
		Date exp = new Date(now.getTime() + TEN_MINUTES);

		String sessionId;
		InvalidSession iSession;
		if(invalidSessionRepo.existsById(account.getAccountId()))
		{
			iSession = invalidSessionRepo.getOne(account.getAccountId());
		}
		else
		{
			iSession = invalidSessionRepo.save(new InvalidSession(account.getAccountId(), null, null));
		}

		if(currentSessionId == null)
		{
			do {

				sessionId = generateRandomString();
			} while(iSession.hasSession(sessionId));
		}
		else
		{
			if(iSession.hasSession(currentSessionId))
				throw new IllegalArgumentException("Current Session is Expired!");
			sessionId = currentSessionId;
		}

		return JWT.create().withIssuer("Trec-Apps-User-Service")
				.withClaim("ID", account.getAccountId())
				.withClaim("sessionId", sessionId)
				.withClaim("Client", (clientId == null ? "Trec-Apps-User-Service" : clientId.getName()))
				.withExpiresAt(exp)
				.withIssuedAt(now)
				.sign(Algorithm.RSA512(publicKey, privateKey));
	}

	public String generateToken(TrecAccount account)
	{
		if(account == null)
			return null;
		
		if(!setKeys())
			return null;
		
		if(!verifyUnlocked(account))
			return null;
		
		privateKey.getAlgorithm();
		
		Date now = new Date(Calendar.getInstance().getTime().getTime());
		
		
		if(account.getTimeForValidToken() > 0)
		{
			
			Date exp = new Date(now.getTime() + (account.getTimeForValidToken() * TEN_MINUTES));
			
			return JWT.create().withIssuer("Trec-Apps-User-Service")
			.withClaim("Username", account.getUsername())
			.withClaim("FirstName", account.getFirstName())
			.withClaim("LastName", account.getLastName())
			.withClaim("ID", account.getAccountId())
			.withClaim("Validated", account.getIsValidated() != 0)
			.withClaim("Color", account.getColor())
			.withClaim("MainEmail", account.getMainEmail())
			.withClaim("TrecEmail", account.getTrecEmail())
			.withClaim("BackupEmail", account.getBackupEmail())
			.withClaim("Birthday", account.getBirthday())
			.withClaim("UpdateByActivity", account.getValidTimeFromActivity() > 0)
			.withIssuedAt(now)
			.withExpiresAt(exp)
			.sign(Algorithm.RSA512(publicKey, privateKey));
		}
		else
		return JWT.create().withIssuer("Trec-Apps-User-Service")
		.withClaim("Username", account.getUsername())
		.withClaim("FirstName", account.getFirstName())
		.withClaim("LastName", account.getLastName())
		.withClaim("ID", account.getAccountId())
		.withClaim("Validated", account.getIsValidated() != 0)
		.withClaim("Color", account.getColor())
		.withClaim("MainEmail", account.getMainEmail())
		.withClaim("TrecEmail", account.getTrecEmail())
		.withClaim("BackupEmail", account.getBackupEmail())
		.withClaim("Birthday", account.getBirthday())
		.withClaim("UpdateByActivity", account.getValidTimeFromActivity() > 0)
		.withIssuedAt(now)
		.sign(Algorithm.RSA512(publicKey, privateKey));
		

	}

	public String getSessionId(String token)
	{
		if(!setKeys())
			return null;

		DecodedJWT decodedJwt = null;
		try
		{
			decodedJwt = JWT.require(Algorithm.RSA512(publicKey,privateKey))
					.build()
					.verify(token);
		}
		catch(JWTVerificationException e)
		{
			e.printStackTrace();
			return null;
		}

		Claim idClaim = decodedJwt.getClaim("sessionId");

		return idClaim.asString();
	}

	public TrecAccount verifyToken(String token)
	{
		
		if(!setKeys())
			return null;
		
		DecodedJWT decodedJwt = null;
		try
		{
			decodedJwt = JWT.require(Algorithm.RSA512(publicKey,privateKey))
					.build()
					.verify(token);
		}
		catch(JWTVerificationException e)
		{
			e.printStackTrace();
			return null;
		}
		
		Claim idClaim = decodedJwt.getClaim("ID");
		
		Long idLong = idClaim.asLong();
		
		if(idLong == null)
			return null;
		
		
		
		return accountService.getAccountById(idLong);
	}

	String generateAuthToken(TrecAccount account, TrecOauthClient client)
	{
		return JWT.create()
				.withIssuedAt(Calendar.getInstance().getTime())
				.withClaim("account", account.getAccountId())
				.withClaim("client", client.getClientId())
				.withClaim("clients", client.getClientSecret())
				.withIssuer("Trec-Apps-User-Service")
				.sign(Algorithm.RSA512(publicKey, privateKey));
	}

	String verifyAuthToken(String token)
	{
		DecodedJWT decodedJwt = null;
		try
		{
			decodedJwt = JWT.require(Algorithm.RSA512(publicKey,privateKey))
					.build()
					.verify(token);
		}
		catch(JWTVerificationException e)
		{
			e.printStackTrace();
			return null;
		}

		// Make sure it references a proper account
		Claim claim = decodedJwt.getClaim("account");
		Long idLong = claim.asLong();

		TrecAccount account =null;
		if(idLong == null && (account = accountService.getAccountById(idLong))== null)
			return null;

		if(!account.isCredentialsNonExpired() || !account.isAccountNonLocked())
			return null;

		claim = decodedJwt.getClaim("client");
		String clientStr = claim.asString();

		TrecOauthClient client = clientService.loadClientByClientId(clientStr);

		claim = decodedJwt.getClaim("clients");
		clientStr = claim.asString();
		if(client == null || !client.getClientSecret().equals(clientStr))
			return null;

		// All Checks have passed.
		return generateToken(account);
	}

	boolean verifyUnlocked(TrecAccount account)
	{
		Date locked = account.getLockInit();
		
		if(locked == null)
			return true;
		// The current time
		Date now = new Date(Calendar.getInstance().getTime().getTime());
		// The time to unlock
		Date unlockTime = new Date(locked.getTime() + (account.getLockTime() * TEN_MINUTES));
		
		if(now.getTime() > unlockTime.getTime())
		{
			account.setLockInit(null);
			account.setFailedLoginAttempts((byte)0);
			
			account = accountRepo.save(account);
			
			return true;
		}
		return false;
	}
}
