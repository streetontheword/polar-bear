package vttp2023.batch3.ssf.frontcontroller.respositories;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
public class AuthenticationRepository {
	@Autowired
	private RedisTemplate<String, Object> template;

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis
	public void logFailedLoginAttempt(String username) {
		int loginAttempts = findFailedLoginAttempts(username);
		if (loginAttempts > 0) {
			template.opsForValue().set(username, ((Integer) loginAttempts) + 1, Duration.ofMinutes(30));
		} else {
			template.opsForValue().set(username, 1, Duration.ofMinutes(30));
		}
	}

	public int findFailedLoginAttempts(String username) {
		Object loginAttempts = template.opsForValue().get(username);
		if (loginAttempts == null) {
			return 0;
		}
		return (Integer) loginAttempts;
	}

}
