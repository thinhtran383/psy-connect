package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.certificate.CertificateUploadDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.services.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Response<?>> uploadCertificate(
            @ModelAttribute CertificateUploadDto certificateUploadDto,
            @AuthenticationPrincipal User user
    ) {

        certificateService.uploadCertificate(certificateUploadDto, user.getId());
        return ResponseEntity.ok(Response.builder()
                .message("Certificate uploaded successfully")
                .build());
    }
}
