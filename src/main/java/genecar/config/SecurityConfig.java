package genecar.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import genecar.common.consts.EnumMapper;
import genecar.security.filter.FilterSkipMatcher;
import genecar.security.filter.FormLoginFilter;
import genecar.security.filter.JwtAuthenticationFilter;
import genecar.security.provider.JWTAuthenticationProvider;
import genecar.security.provider.LoginAuthenticationProvider;
import genecar.security.vo.UserRole;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// spring security는 필터임 
	// 필터는 서블릿에 도달하기 전에 호출되고 스프링 인터셉터는 서블릿에서 컨트롤러를 호출하기 전에 호출됨

    private final LoginAuthenticationProvider loginProvider;
    private final JWTAuthenticationProvider jwtProvider;

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
    	authenticationManagerBuilder
			.authenticationProvider(this.loginProvider)
			.authenticationProvider(this.jwtProvider);
		
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http
			.headers()
			.frameOptions()
			.disable();

        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증
         */
		http
			.addFilterBefore(this.formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(this.jwtFilter(),UsernamePasswordAuthenticationFilter.class);

        // 접근 설정
        http.authorizeRequests()
            .mvcMatchers("/static", "/login", "/vue", "/attboxau").permitAll()
            .mvcMatchers("/api","/usr", "/odr").hasAnyRole(UserRole.USER.name())
            .mvcMatchers("/api/v2","/api/v3").hasAnyRole(UserRole.ADMIN.name(), UserRole.SUPER.name())
        	.mvcMatchers("/adm/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.SUPER.name());
    }

    protected FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter filter = new FormLoginFilter(
		/* new AntPathRequestMatcher("/api/account/login", HttpMethod.POST.name()) */
        		new AntPathRequestMatcher("/web/member/login", HttpMethod.POST.name())
        );
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }

    
    protected JwtAuthenticationFilter jwtFilter() throws Exception {
        List<AntPathRequestMatcher> skipPath = new ArrayList<>();

        // Static 정보 접근 허용
        skipPath.add(new AntPathRequestMatcher("/error", HttpMethod.GET.name()));
        skipPath.add(new AntPathRequestMatcher("/favicon.ico", HttpMethod.GET.name()));
        skipPath.add(new AntPathRequestMatcher("/static", HttpMethod.GET.name()));
        skipPath.add(new AntPathRequestMatcher("/static/**", HttpMethod.GET.name()));
        skipPath.add(new AntPathRequestMatcher("/attboxau", HttpMethod.GET.name()));
        
        skipPath.add(new AntPathRequestMatcher("/api/account", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/account/login", HttpMethod.POST.name()));
        // skipPath.add(new AntPathRequestMatcher("/api/order/**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/art/**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/main/**", HttpMethod.POST.name()));

        // 파일 업로드
        skipPath.add(new AntPathRequestMatcher("/api/atchmnfl/**"));
        skipPath.add(new AntPathRequestMatcher("/attboxal/**"));

        // 신규 토큰 발급
        skipPath.add(new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/auth/login/admin", HttpMethod.POST.name()));
        // 통합회원 여부 확인
        skipPath.add(new AntPathRequestMatcher("/api/auth/select/gbn", HttpMethod.POST.name()));

        // 회원 가입할 때 중복 체크 등을 위하여 인증없이 조회하는 하는 API
        skipPath.add(new AntPathRequestMatcher("/api/member/check**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/search**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/updateMemberPw", HttpMethod.POST.name()));
        // 회원가입할 때는 인증 x
        skipPath.add(new AntPathRequestMatcher("/api/member/joinMember", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/insertMemberAccount", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/selectVrtlAccount", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/authMemberCheck", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/updateMemberPw", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/reUpdateMemberPw", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/insertMemberAccountPop", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/updateIntegMember", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/joinMemberCheck", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/member/selectPolicyPopup", HttpMethod.POST.name()));
        
        //공모전
        skipPath.add(new AntPathRequestMatcher("/api/pssrp/info/**", HttpMethod.POST.name()));
        // 베너 조회는 인증 x
        skipPath.add(new AntPathRequestMatcher("/api/banner/**", HttpMethod.POST.name()));
        // 공지사항 조회는 인증 x
        skipPath.add(new AntPathRequestMatcher("/api/notice/**", HttpMethod.POST.name()));
        // 아티피디아 조회는 인증 x
        skipPath.add(new AntPathRequestMatcher("/api/artpedia/**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/promotion/**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/popup/**", HttpMethod.POST.name()));
        // 고객지원 FAQ 조회는 인증 x
        skipPath.add(new AntPathRequestMatcher("/api/inquiry/selectFAQList", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/api/event/**", HttpMethod.POST.name()));
        skipPath.add(new AntPathRequestMatcher("/view/danal/**"));
        skipPath.add(new AntPathRequestMatcher("/was_js/**"));
        skipPath.add(new AntPathRequestMatcher("/css/**"));

        // 아이디,암호로 로그인(accessToken 취득)
        skipPath.add(new AntPathRequestMatcher("/api/member/login"));

        // skipPath.add(new AntPathRequestMatcher("/api/**"));
        skipPath.add(new AntPathRequestMatcher("/danal/**"));

        FilterSkipMatcher matcher = new FilterSkipMatcher(skipPath, "/**");

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher);
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }
    
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("UserRole", UserRole.class);

        return enumMapper;
    }


}