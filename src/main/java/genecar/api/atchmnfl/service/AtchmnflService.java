package genecar.api.atchmnfl.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import genecar.api.atchmnfl.entity.Atchmnfl;
import genecar.api.atchmnfl.repository.AtchmnflRepository;
import genecar.api.atchmnfl.vo.AtchmnflVO;
import genecar.root.common.exception.CommonException;

/** 파일 첨부 서비스 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AtchmnflService {

  private final AtchmnflRepository atchmnflRepository;

  /**
   * / 부터 시작하는 경로 시작 부분. 환경에 따라 다를 수 있음
   *
   * <p>예> /mnt/nas/dev/ /mng/nas/prod/
   */
  @Value("${atchmnfl.rootPath}")
  private String rootPath;

  /** 파일 저장. 로컬에 저장 후 저장 정보 DB에 저장. DB 정보 리턴 */
  @Transactional
  public Atchmnfl saveAtchmnfl(AtchmnflVO atchmnflVO, MultipartFile file) throws CommonException {

    // 로컬에 파일 저장
    if (file.isEmpty()) {
      // 저장할 파일이 없으면 오류
      throw new CommonException(String.format("파일이 없습니다 >>> %s", atchmnflVO.toString()));
    }

    String realFileName = file.getOriginalFilename();
    String fileExtWithDot = "";
    int index = realFileName.lastIndexOf('.');
    if (index > 0) {
      fileExtWithDot = realFileName.substring(index);
    }
    String atchmnflNm = UUID.randomUUID() + fileExtWithDot;
    String atchmnflPath = this.rootPath + "atchmnfl/";
    String fileLocation = atchmnflPath + atchmnflNm;
    Path filePath = Paths.get(fileLocation);
    int fileCpcty;

    try {
      try (InputStream inputStream = file.getInputStream()) {
        fileCpcty = file.getBytes().length;
        Files.createDirectories(filePath.getParent());
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new CommonException(String.format("저장 중 오류가 발생했습니다. >>>  ", atchmnflVO.toString()));
    }

    // 저장 경로. 이름 포함하여 DB 에 저장
    Atchmnfl atchmnfl = new Atchmnfl();
    atchmnfl.setAtchmnflNm(atchmnflNm);
    atchmnfl.setAtchmnflPath(atchmnflPath);
    atchmnfl.setRealAtchmnflNm(realFileName);
    atchmnfl.setFileCpcty(fileCpcty);
    return atchmnflRepository.save(atchmnfl);
  }

  /**
   * @see AtchmnflService#moveAtchmnfl(Atchmnfl, String, String)
   */
  @Transactional
  public Atchmnfl moveAtchmnfl(Integer atchmnflNo, String pathToMove) throws IOException {
    return this.moveAtchmnfl(atchmnflNo, pathToMove, null);
  }

  /**
   * @see AtchmnflService#moveAtchmnfl(Atchmnfl, String, String)
   */
  @Transactional
  public Atchmnfl moveAtchmnfl(Integer atchmnflNo, String pathToMove, String nameToChange)
      throws IOException {
    Atchmnfl atchmnfl = atchmnflRepository.findById(atchmnflNo).get();
    return this.moveAtchmnfl(atchmnfl, pathToMove, nameToChange);
  }

  /**
   * @see AtchmnflService#moveAtchmnfl(Atchmnfl, String, String)
   */
  @Transactional
  public Atchmnfl moveAtchmnfl(Atchmnfl atchmnfl, String pathToMove) throws IOException {
    return this.moveAtchmnfl(atchmnfl, pathToMove, null);
  }

  /**
   * 파일 경로 업데이트. 다른 데이터 추가하며 실제 사용하게 되었을 때 소속된 경로로 이동.
   *
   * <p>사용절차 예>
   *
   * <p>1. Atchmnfl api 를 통해 파일 업로드. 응답값을 화면에 표시한다
   *
   * <p>2. 공지사항 등 등록 api 에 1번의 응답값을 같이 보낸다
   *
   * <p>3. 2번 데이터를 저장할 때, 파일 정보가 같이 있다면 파일을 이동시키기 위해 <bold>이 메서드를 실행한다</bold>
   *
   * <p>4. 변경된 정보를 가지고 있는 return 값의 경로 등을 공지사항 데이터 등에 반영하여 저장한다
   *
   * @param atchmnfl 첨부파일
   * @param pathToMove 이동할 경로. storage root(~~/dev, ~~/prod) 를 제외한 경로를 입력한다(dev 등은 환경 따라서 알아서 붙임)
   * @param nameToChange 변경할 이름. null 이면 기존 이름 유지
   */
  @Transactional
  public Atchmnfl moveAtchmnfl(Atchmnfl atchmnfl, String pathToMove, String nameToChange)
      throws IOException {
    // persist 상태 확인
    if (atchmnfl.getRealAtchmnflNm() == null) {
      atchmnfl = atchmnflRepository.findById(atchmnfl.getAtchmnflNo()).get();
    }
    Path oldPath = Paths.get(atchmnfl.getAtchmnflPath() + atchmnfl.getAtchmnflNm());
    String newName = nameToChange != null ? nameToChange : atchmnfl.getAtchmnflNm();
    Path newPath = Paths.get(this.rootPath + pathToMove + newName);

    // 파일이 이미 있는지 체크. 있으면 이름 앞에 숫자를 붙여서 중복검사 다시
    int fileNameRetryCnt = 0;
    while (Files.exists(newPath)) {
      fileNameRetryCnt++;
      newPath = Paths.get(this.rootPath + pathToMove + "/" + fileNameRetryCnt + "_" + newName);
    }
    if (fileNameRetryCnt > 0) {
      newName = fileNameRetryCnt + "_" + newName;
      atchmnfl.setRealAtchmnflNm(fileNameRetryCnt + "_" + atchmnfl.getRealAtchmnflNm());
    }

    //    if (Files.exists(newPath)) {
    //      throw new AtchmnflException("이동하려는 곳에 파일 이미 존재");
    //    }

    // 파일 이동
    Files.createDirectories(newPath.getParent());
    Files.move(oldPath, newPath);

    // 파일 이동 후, 정보 저장
    atchmnfl.setAtchmnflPath(newPath.getParent().toString() + "/");
    atchmnfl.setAtchmnflNm(newName);
    atchmnflRepository.save(atchmnfl);

    // 저장 경로 중 /origin/ 이 포함되어있고, 파일명이 이미지라면 리사이징을 실행 후 각 리사이징 폴더에 추가로 저장한다
    if (newPath.toString().indexOf("/origin/") >= 0) {
      int[] widthList = {200, 400, 600, 800, 1200, 1800};
      BufferedImage bufferedImage = null;
      try {
        bufferedImage = ImageIO.read(newPath.toFile());
        for (int width : widthList) {
          Path resizeFilePath = Paths.get(newPath.toString().replace("/origin/", "/w" + width + "/"));
          Files.createDirectories(resizeFilePath.getParent());
          Thumbnails.of(bufferedImage).size(width, 6000).toFile(resizeFilePath.toFile());
        }
      } catch (IOException e) {
        log.error("썸네일 생성 중 오류, {}", e.getMessage());
      }
    }

    return atchmnfl;
  }
}
