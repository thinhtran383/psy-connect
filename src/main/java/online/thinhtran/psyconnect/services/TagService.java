package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Tag;
import online.thinhtran.psyconnect.repositories.TagRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.tag.TagResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public String getTagById(Integer tagId) {
        return tagRepository.findById(tagId).orElseThrow().getTagName();
    }

    @Transactional(readOnly = true)
    public PageableResponse<TagResponse> getAllTag(int page, int size) {
        Page<Tag> tags = tagRepository.findAll(PageRequest.of(page, size));

        return PageableResponse.<TagResponse>builder()
                .totalPages(tags.getTotalPages())
                .totalElements(tags.getTotalElements())
                .elements(tags.map(tag -> modelMapper.map(tag, TagResponse.class)).toList())
                .build();
    }

}
