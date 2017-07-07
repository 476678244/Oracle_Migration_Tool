package springbased.example.bean;

/**
 * Created by wuzonghan on 2017/7/7.
 */

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Member {
	public Member() {
	}

	public Member(String name, String phone, String email) {
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	private String name;
	private String phone;
	private String email;

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}