package online.thinhtran.psyconnect.services;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.certificate.CertificateUploadDto;
import online.thinhtran.psyconnect.entities.Certificate;
import online.thinhtran.psyconnect.repositories.CertificateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
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

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Transactional
    public void uploadCertificate(CertificateUploadDto certificateUploadDto, Integer userId) {
        List<Pair<Certificate, MultipartFile>> certificatePairs = certificateUploadDto.getImages().stream()
                .map(image -> {
                    Certificate certificate = new Certificate();
                    certificate.setUserId(userId);
                    certificate.setCertificateName(certificateUploadDto.getName());
                    certificateRepository.save(certificate);
                    return Pair.of(certificate, image);
                })
                .toList();

        System.out.println("Submitting background task to upload images.");

        executorService.submit(() -> {
            certificatePairs.forEach(pair -> {
                try {
                    System.out.println("Uploading image for certificate ID: " + pair.getFirst().getId());
                    String imageUrl = cloudinaryService.upload(pair.getSecond()); // Upload từng ảnh
                    pair.getFirst().setCertificateImage(imageUrl);
                    certificateRepository.save(pair.getFirst()); // Lưu từng ảnh vào CSDL
                    System.out.println("Upload successful for certificate ID: " + pair.getFirst().getId());
                } catch (Exception e) {
                    System.err.println("Error uploading image for certificate ID: " + pair.getFirst().getId() + ": " + e.getMessage());
                }
            });
            System.out.println("All images uploaded and certificates updated.");
        });


        System.out.println("Background task submitted successfully.");
    }

    @PreDestroy
    public void shutDownExecutorService() {
        executorService.shutdown();
        System.out.println("Executor service shut down.");
    }

    @Transactional(readOnly = true)
    public List<String> getCertificateImages(Integer userId) {
        return certificateRepository.findAllCertificateByUserId(userId);
    }


}
