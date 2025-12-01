package ee.news.app.controller.posts;

import ee.news.app.service.posts.PostsService;
import ee.news.app.service.posts.dto.AddPostDto;
import ee.news.app.service.posts.dto.PostsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/posts")
    @Operation(summary = "Returns list of all posts", description = "If there are no posts, returns an empty array")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public List<PostsDto> getPosts() {

        return postsService.allPosts();
    }

    @PostMapping("/posts/add")
    @Operation(summary = "Returns list of all posts", description = "If there are no posts, returns an empty array")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public String newPosts(@RequestBody AddPostDto addPostDto) {
        return postsService.newPost(addPostDto);
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "Deletes post", description = "Deletes the post by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public void deletePosts(@PathVariable("id") Integer id) {
        postsService.deletePost(id);
    }
}
