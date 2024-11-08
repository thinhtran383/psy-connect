package online.thinhtran.psyconnect.services;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String upload(byte[] fileData) {
        try {
            System.out.println("Uploading file...");
            Map<String, Object> data = cloudinary.uploader().upload(fileData, Map.of());
            String url = (String) data.get("secure_url");
            System.out.println("Upload successful. URL: " + url);
            return url;
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException("Image upload failed: " + io.getMessage());
        }
    }
}
