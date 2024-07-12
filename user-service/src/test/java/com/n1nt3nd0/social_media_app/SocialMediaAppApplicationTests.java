package com.n1nt3nd0.social_media_app;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SocialMediaAppApplicationTests {

	@Test
	void contextLoads() {
		boolean b = true;
		Assertions.assertThat(b).isTrue();
	}
	@Test
	public void testUsingSimpleRegex() {
		String emailAddress = "username@domain.com";
		String regexPattern = "^(.+)@(\\S+)$";
		Assertions.assertThat(emailAddress.matches(regexPattern)).isTrue();
	}
	@Test
	public void testUsingSimpleRegex2() {
		String emailAddress = "username@domain.com";
		String regexPattern =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		Assertions.assertThat(emailAddress.matches(regexPattern)).isTrue();
	}

}
