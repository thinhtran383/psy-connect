package online.thinhtran.psyconnect.dto.certificate;

import lombok.*;
import org.aspectj.apache.bcel.classfile.InnerClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateUploadDto {
    private List<MultipartFile> images;
    private String name;
}
