package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.certificate.CertificateUploadDto;
import online.thinhtran.psyconnect.entities.Certificate;
import online.thinhtran.psyconnect.repositories.CertificateRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final CloudinaryService cloudinaryService;


    @Async
    public void uploadCertificate(CertificateUploadDto certificateUploadDto, Integer userId) {
        log.info("certificateUploadDto: {}", certificateUploadDto.getImages().size());

        List<CompletableFuture<Void>> futures = certificateUploadDto.getImages().stream()
                .map(file -> CompletableFuture.runAsync(() -> {
                    try {
                        log.info("Uploading image: {}", file.getOriginalFilename());

                        byte[] fileData = file.getBytes();
                        String imageUrl = cloudinaryService.upload(fileData);

                        Certificate certificate = new Certificate();
                        certificate.setUserId(userId);
                        certificate.setCertificateImage(imageUrl);
                        certificate.setCertificateName(certificateUploadDto.getName());

                        certificateRepository.save(certificate);

                        log.info("Uploaded image successfully: {}", file.getOriginalFilename());
                    } catch (Exception e) {
                        log.error("Failed to upload image for user {}: {}", userId, file.getOriginalFilename(), e);
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        CompletableFuture.completedFuture(null);
    }



    @Transactional(readOnly = true)
    public List<String> getCertificateImages(Integer userId) {
        return certificateRepository.findAllCertificateByUserId(userId);
    }


}
