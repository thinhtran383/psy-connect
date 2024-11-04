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

    public String upload(MultipartFile file)  {
        try{
            Map<Objects, String> data = cloudinary.uploader().upload(file.getBytes(), Map.of());
            String url = data.get("secure_url");
            return url;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
}
