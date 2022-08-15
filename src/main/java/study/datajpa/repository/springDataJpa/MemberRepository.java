package study.datajpa.repository.springDataJpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
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

	@Query("select m.username from Member m")
	List<String> findUsernameList();

	// JPQL dto 반환
	@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();

	// 컬렉션 파라미터 바인딩
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") List<String> names);
}
