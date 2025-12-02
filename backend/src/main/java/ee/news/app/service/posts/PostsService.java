package ee.news.app.service.posts;

import ee.news.app.infrastructure.exception.ResourceNotFoundException;
import ee.news.app.persistence.posts.Posts;
import ee.news.app.persistence.posts.PostsRepository;
import ee.news.app.persistence.user.User;
import ee.news.app.persistence.user.UserRepository;
import ee.news.app.service.posts.dto.AddPostDto;
import ee.news.app.service.posts.dto.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsMapper postsMapper;
    private final UserRepository userRepository;

    public String newPost(AddPostDto addPostDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Posts newPost = postsMapper.addFromDto(addPostDto);
        newPost.setUser(user);
        postsRepository.save(newPost);
        return "New post saved";
    }

    public List<PostsDto> allPosts() {
        List<Posts> allPosts = postsRepository.findAll();
        List<PostsDto> allPostsDto = new ArrayList<>();
        for (Posts post : allPosts) {
            allPostsDto.add(postsMapper.toDto(post));
        }
        return allPostsDto;
    }

    public void deletePost(Integer id) {
        postsRepository.deleteById(id);
    }

    public PostsDto updatePost(Integer id, AddPostDto addPostDto) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post does not exist!"));
        post.setTitle(addPostDto.getTitle());
        post.setPost(addPostDto.getPost());

        postsRepository.save(post);

        return postsMapper.toDto(post);
    }
}
