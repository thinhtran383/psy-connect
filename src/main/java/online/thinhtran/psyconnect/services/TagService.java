package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public String getTagById(Integer tagId) {
        return tagRepository.findById(tagId).orElseThrow().getTagName();
    }
}
