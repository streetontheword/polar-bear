package vttp2023.batch3.ssf.frontcontroller.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch3.ssf.frontcontroller.respositories.AuthenticationRepository;

@Service
public class AuthenticationService {
	@Autowired
	private AuthenticationRepository authRepo;

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public void authenticate(String username, String password) throws Exception {
		String url = "http://localhost:8081/api/authenticate";

		JsonObject obj = Json.createObjectBuilder()
			.add("username", username)
			.add("password", password)
			.build();

		RequestEntity<String> req = RequestEntity.post(url)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.body(obj.toString());

		RestTemplate template = new RestTemplate();

		ResponseEntity<String> res = template.exchange(req, String.class);

		if (!res.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
			JsonReader jr = Json.createReader(new StringReader(res.getBody()));
			String authMessage = jr.readObject().getString("message");
			throw new Exception(authMessage);
		}
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return findFailedLoginAttempts(username) >= 3;
	}

	public void logFailedLoginAttempt(String username) {
		authRepo.logFailedLoginAttempt(username);
	}

	public int findFailedLoginAttempts(String username) {
		return authRepo.findFailedLoginAttempts(username);
	}
}
