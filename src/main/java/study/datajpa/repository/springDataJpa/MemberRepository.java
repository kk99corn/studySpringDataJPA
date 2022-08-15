package study.datajpa.repository.springDataJpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

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

	// 반환타입
	List<Member> findListByUsername(String username); // 컬렉션

	Member findMemberByUsername(String username); // 단건

	Optional<Member> findOptionalByUsername(String username); // 단건 optional

	// countQuery 분리
	@Query(value = "select m from Member m left join m.team t",
			countQuery = "select count(m.username) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);

	Slice<Member> findSliceByAge(int age, Pageable pageable);
}
