package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.comments.CommentDto;
import online.thinhtran.psyconnect.entities.Comment;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.CommentRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public long countByPostId(Integer postId) {
        return commentRepository.countByPostId(postId);
    }


    @Transactional(readOnly = true)
    public PageableResponse<UserCommentResponse> getAllCommentsByPostId(Integer postId, int page, int size) {
        Page<UserCommentResponse> comments = commentRepository.findAllByPostId(postId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return PageableResponse.<UserCommentResponse>builder()
                .elements(comments.getContent())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .build();
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public void commentPost(CommentDto commentDto, User user) {
        Comment comment = Comment.builder()
                .userId(user.getId())
                .postId(commentDto.getPostId())
                .content(commentDto.getContent())
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public void deleteCommentsByPostId(Integer postId) {
        commentRepository.deleteCommentsByPostId(postId);
    }
}
