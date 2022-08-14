package study.datajpa.repository.springDataJpa;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// 쿼리 메소드 기능
	List<Member> findByUsername(String username);

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
