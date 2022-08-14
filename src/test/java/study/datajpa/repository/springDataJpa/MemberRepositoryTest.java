package study.datajpa.repository.springDataJpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.repository.springDataJpa.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Test
	public void testMember() {

		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);

		Optional<Member> byId = memberRepository.findById(savedMember.getId());
		Member findMember = byId.get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

		/*
		 * findMember == member
		 * 동일한 Transaction 안에서는 영속성 컨텐츠들의 동일성을 보장함
		 */
		assertThat(findMember).isEqualTo(member);
	}
}