package springbased.example.service.impl;

import springbased.example.bean.Member;
import springbased.example.service.MemberService;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wuzonghan on 2017/7/7.
 */
public class InMemoryMemberService implements MemberService {

	@Override
	public Set<Member> findAll() {
		return Stream.of("member1", "member2", "member3").map(s-> {
			return new Member(s, s + "phone", s+"email");
		}).collect(Collectors.toSet());
	}
}
