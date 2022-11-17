package genecar.api.atchmnfl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 첨부파일. 파일을 먼저 올리고, 필요한 곳(공지사항, 작품)에서는 생성된 아이디만 배열로 업로드
 */
@Data
@Entity
@Table(name = "ATCHMNFL", indexes = {
    @Index(name = "ATCHMNFL_IDX1", columnList = "ATCHMNFL_NM"),
})
@DynamicUpdate
@Accessors(chain = true)
public class Atchmnfl implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ATCHMNFL_NO")
  private Integer atchmnflNo;

  /**
   * 실제 파일 명(업로드한 클라이언트에서 갖고 있던 이름)
   */
  @Column(name = "REAL_ATCHMNFL_NM")
  @NotNull
  private String realAtchmnflNm;

  /**
   * 저장 파일명. /atchmnfl api 를 사용하여 액세스 할 때 사용가능
   *
   * 도메인 + /api/atchmnfl/ + atchmnflNm 으로 액세스
   */
  @Column(name = "ATCHMNFL_NM")
  @NotNull
  private String atchmnflNm;

  /**
   * 파일 경로(서버 저장소에 파일이 저장된 경로). 웹서버에서 직접 호스팅한다면 경로를 이동시킬 수 있다
   */
  @Column(name = "ATCHMNFL_PATH")
  @JsonIgnore
  @NotNull
  private String atchmnflPath;

  /**
   * 파일 용량
   */
  @Column(name = "FILE_CPCTY")
  @NotNull
  private Integer fileCpcty;

  /**
   * 등록 일
   */
  @Column(name = "REG_DT")
  @CreationTimestamp
  private LocalDateTime regDt;
}
