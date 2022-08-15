package study.datajpa.repository.springDataJpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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

	// bulk
	@Modifying(clearAutomatically = true)
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	// fetch join을 하게되면 member 조회시 연관된 team 다 조회
	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();

	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(String username);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String username);

	List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
	List<UsernameOnlyDto> findProjectionsDtoByUsername(@Param("username") String username);

	// NativeQuery
	@Query(value = "select * from member where username = ?", nativeQuery = true)
	Member findByNativeQuery(String username);
}
