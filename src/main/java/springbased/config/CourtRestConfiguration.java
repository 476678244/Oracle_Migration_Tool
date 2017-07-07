package springbased.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;
import springbased.example.bean.Member;
import springbased.example.bean.Members;
import springbased.example.service.MemberService;
import springbased.example.service.impl.InMemoryMemberService;

/**
 * Created by wuzonghan on 2017/7/7.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "springbased.example.controller")
public class CourtRestConfiguration {

	@Bean
	public MemberService memberService() {
		return new InMemoryMemberService();
	}

}