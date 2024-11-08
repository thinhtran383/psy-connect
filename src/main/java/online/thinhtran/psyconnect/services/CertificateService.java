package online.thinhtran.psyconnect.services;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.certificate.CertificateUploadDto;
import online.thinhtran.psyconnect.entities.Certificate;
import online.thinhtran.psyconnect.repositories.CertificateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    @Async
    public void uploadCertificate(CertificateUploadDto certificateUploadDto, Integer userId) {
        certificateUploadDto.getImages().forEach(file -> {
            try {
                byte[] fileData = file.getBytes();
                String imageUrl = cloudinaryService.upload(fileData);

                Certificate certificate = new Certificate();
                certificate.setUserId(userId);
                certificate.setCertificateImage(imageUrl);
                certificate.setCertificateName(certificateUploadDto.getName());
                certificateRepository.save(certificate);
            } catch (Exception e) {
                System.err.println("Failed to upload image for user " + userId);
            }
        });
    }


    @Transactional(readOnly = true)
    public List<String> getCertificateImages(Integer userId) {
        return certificateRepository.findAllCertificateByUserId(userId);
    }


}
