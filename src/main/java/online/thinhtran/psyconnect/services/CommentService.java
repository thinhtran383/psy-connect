package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.repositories.CommentRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public long countByPostId(Integer postId) {
        return commentRepository.countByPost_Id(postId);
    }


    @Transactional(readOnly = true)
    public PageableResponse<UserCommentResponse> getAllCommentsByPostId(Integer postId, int page, int size) {
        Page<UserCommentResponse> comments = commentRepository.findAllByPostId(postId, PageRequest.of(page, size));
        return PageableResponse.<UserCommentResponse>builder()
                .elements(comments.getContent())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .build();
    }
}
