package genecar.root.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBean {
	
	// PasswordEncoder 순함참조 문제
	// SecurityConfig > LoginAuthenticationProvider > AccountService 순환참조로 인해 분리해줌(더 좋은방법 생각중)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
