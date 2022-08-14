package study.datajpa.repository.springDataJpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// 쿼리 메소드 기능
	List<Member> findByUsername(String username);
	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	// @NamedQuery
	@Query(name = "Member.findByUsername")
	List<Member> findByUsername2(@Param("username") String username);

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
