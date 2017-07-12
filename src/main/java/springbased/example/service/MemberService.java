package springbased.example.service;

import springbased.example.bean.Member;

import java.util.Collection;

/**
 * Created by wuzonghan on 2017/7/7.
 */
public interface MemberService {
	Collection<Member> findAll();
}
