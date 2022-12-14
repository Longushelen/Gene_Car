package genecar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;

@SpringBootApplication
public class GeneCarApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GeneCarApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(GeneCarApplication.class, args);
	}

//	@Configuration("leaveaTrace")
//	class leaveaTrace extends LeaveaTrace {};
}

// 로컬용
//public class BackendApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(BackendApplication.class, args);
//	}
//
//	@Configuration("leaveaTrace")
//	class leaveaTrace extends LeaveaTrace {};
//}