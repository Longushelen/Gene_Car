package genecar.api.atchmnfl.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.BooleanBuilder;
import genecar.root.common.controller.BaseController;
import genecar.root.common.exception.CommonException;
import genecar.root.common.util.StringUtil;
import genecar.root.common.vo.PackingVO;
import genecar.api.atchmnfl.entity.Atchmnfl;
import genecar.api.atchmnfl.repository.AtchmnflRepository;
import genecar.api.atchmnfl.service.AtchmnflService;
import genecar.api.atchmnfl.vo.AtchmnflVO;
import genecar.api.atchmnfl.entity.QAtchmnfl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@Controller
@Slf4j
public class AtchmnflController extends BaseController {

  private final AtchmnflRepository atchmnflRepository;

  private final AtchmnflService atchmnflService;

  private final Tika tika;

  /**
   * 파일 업로드
   */
  @PostMapping("/api/atchmnfl")
  public @ResponseBody PackingVO postAtchmnfl(
      AtchmnflVO atchmnflVO,
      @RequestParam("file") MultipartFile file
  ) throws CommonException {
    log.info("[ 파일 업로드 ] {}", atchmnflVO);

    return getPack("0000", "저장완료", atchmnflVO, atchmnflService.saveAtchmnfl(atchmnflVO, file));
  }

  /**
   * 에디터 이미지 업로드
   */
  @PostMapping("/api/atchmnfl/editor")
  public @ResponseBody PackingVO postEditorImage(
      AtchmnflVO atchmnflVO,
      @RequestParam("file") MultipartFile file
  ) throws CommonException, IOException {
    log.info("[ 파일 업로드 ] {}", atchmnflVO);
    Atchmnfl atchmnfl = atchmnflService.saveAtchmnfl(atchmnflVO, file);
    atchmnfl = atchmnflService.moveAtchmnfl(atchmnfl, "al/editor/");
    // 에디터 본문 내 파일 이름은 없애버림. 화면에 노출되지 않고 같은 폴더를 사용하기 때문에 중복으로 저장하고 관리하지 않음
    atchmnfl.setRealAtchmnflNm(atchmnfl.getAtchmnflNm());
    atchmnflRepository.save(atchmnfl);

    return getPack("0000", "저장완료", atchmnflVO, atchmnfl);
  }

  /**
   * 개발환경에서 웹 서버 없을 때 테스트 목적으로 사용하는 api
   *
   * 운영환경에서는 웹서버가 /attboxal 을 redirect 하므로 사용되지 않는다.
   */
  @GetMapping({
      "/attboxal/{entityName}/{realAtchmnflNm}",
      "/attboxal/{entityName}/{path2}/{realAtchmnflNm}",
      "/attboxal/{entityName}/{path2}/{path3}/{realAtchmnflNm}",
      "/attboxal/{entityName}/{path2}/{path3}/{path4}/{realAtchmnflNm}"
  })
  public ResponseEntity<Resource> getAtchmnfl(
      @PathVariable(required = false) String entityName, //  notice, symbol
      @PathVariable(required = false) String path2,
      @PathVariable(required = false) String path3,
      @PathVariable(required = false) String path4,
      @PathVariable String realAtchmnflNm
  )
      throws UnsupportedEncodingException {
    log.info("[ 파일 조회 ] realAtchmnflNm : {}", realAtchmnflNm);

    String atchmnflPath;
    String realNm;
    String vrtlNm;

    String realAtchmnflPath = "/al";
    if (!StringUtil.isEmpty(entityName)) {
      realAtchmnflPath += "/" + entityName;
    }
    if (!StringUtil.isEmpty(path2)) {
      realAtchmnflPath += "/" + path2;
    }
    if (!StringUtil.isEmpty(path3)) {
      realAtchmnflPath += "/" + path3;
    }
    if (!StringUtil.isEmpty(path4)) {
      realAtchmnflPath += "/" + path4;
    }
    realAtchmnflPath += "/";

    BooleanBuilder bb = new BooleanBuilder();
    QAtchmnfl qAtchmnfl = QAtchmnfl.atchmnfl;
    bb.and(qAtchmnfl.realAtchmnflNm.eq(realAtchmnflNm)
        .and(qAtchmnfl.atchmnflPath.endsWith(realAtchmnflPath))
    );
    Atchmnfl atchmnfl = atchmnflRepository.findOne(bb).orElse(null);

    if (atchmnfl == null) {
      return ResponseEntity.notFound().build();
    }

    atchmnflPath = atchmnfl.getAtchmnflPath();
    vrtlNm = atchmnfl.getAtchmnflNm();
    realNm = atchmnfl.getRealAtchmnflNm();

    try {
      Path filePath = Paths.get(atchmnflPath + vrtlNm);
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists() || resource.isReadable()) {
        String encodedFilename =
            URLEncoder.encode(realNm, "UTF-8").replace("+", "%20");

        String contentType = tika.detect(resource.getFilename());
        boolean isImage = contentType.startsWith("image");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType(contentType));

        if (!isImage) {
          responseHeaders.setContentDisposition(
              ContentDisposition.attachment()
                  .filename(realNm, Charset.forName("UTF-8"))
                  .build());
        }

        return ResponseEntity.ok().headers(responseHeaders)
            .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping({
      "/api/atchmnfl/{atchmnflNm}"
  })
  public ResponseEntity<Resource> getAtchmnfl(@PathVariable String atchmnflNm)
      throws UnsupportedEncodingException {
    log.info("[ 파일 조회 ] atchmnflNm : {}", atchmnflNm);

    Atchmnfl atchmnfl = atchmnflRepository.findByAtchmnflNm(atchmnflNm).get();

    try {
      Path filePath = Paths.get(atchmnfl.getAtchmnflPath() + atchmnfl.getAtchmnflNm());
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists() || resource.isReadable()) {
        String encodedFilename =
            URLEncoder.encode(atchmnfl.getRealAtchmnflNm(), "UTF-8").replace("+", "%20");

        String contentType = tika.detect(resource.getFilename());
        boolean isImage = contentType.startsWith("image");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType(contentType));

        if (!isImage) {
          responseHeaders.setContentDisposition(
              ContentDisposition.attachment()
                  .filename(atchmnfl.getRealAtchmnflNm(), Charset.forName("UTF-8"))
                  .build());
        }

        return ResponseEntity.ok().headers(responseHeaders)
            .body(resource);
      } else {
        return ResponseEntity.badRequest().body(null);
      }
    } catch (MalformedURLException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }
}
