package genecar.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  /**
   * file type 식별 라이브러리. 업로드한 파일의 이름을 바탕으로 타입을 식별. 응답 헤더에 추가한다
   */
  @Bean
  public Tika tika() {
    return new Tika();
  }
}