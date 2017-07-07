package springbased.example.controller;

/**
 * Created by wuzonghan on 2017/7/7.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springbased.example.bean.Members;
import springbased.example.service.MemberService;

@Controller
public class RestMemberController {

	private final MemberService memberService;

	@Autowired
	public RestMemberController(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@RequestMapping(value = "/members", method = RequestMethod.GET)
	@ResponseBody
	public Members getRestMembers(Model model) {
		Members members = new Members();
		members.addMembers(memberService.findAll());
		return members;
	}

}
