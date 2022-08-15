package study.datajpa.repository.springDataJpa;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

	// @Value("#{target.username + ' ' + target.age}")
	String getUsername();
}
