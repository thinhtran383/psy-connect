package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.certificate.CertificateUploadDto;
import online.thinhtran.psyconnect.entities.Certificate;
import online.thinhtran.psyconnect.repositories.CertificateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Transactional
    public void uploadCertificate(CertificateUploadDto certificateUploadDto) {
        List<Certificate> certificates = certificateUploadDto.getImages().stream().map(image -> {
            Certificate certificate = new Certificate();
            certificate.setUserId(certificateUploadDto.getUserId());
            certificate.setCertificateName(certificateUploadDto.getName());
            certificate.setCertificateImage(cloudinaryService.upload(image));
            return certificate;
        }).collect(Collectors.toList());

        certificateRepository.saveAll(certificates);
    }

    @Transactional(readOnly = true)
    public List<String> getCertificateImages(Integer userId) {
        return certificateRepository.findAllCertificateByUserId(userId);
    }
}
