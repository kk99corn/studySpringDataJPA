package study.datajpa.repository.springDataJpa;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
	List<Member> findMemberCustom();
}
