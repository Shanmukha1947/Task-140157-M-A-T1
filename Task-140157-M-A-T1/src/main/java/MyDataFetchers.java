import graphql.schema.DataFetcher;
import org.dataloader.DataLoader;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MyDataFetchers {

    // DataLoader for Users
    private final DataLoader<Long, User> userLoader;

    public MyDataFetchers(DataLoader<Long, User> userLoader) {
        this.userLoader = userLoader;
    }

    // DataFetcher to retrieve a user by ID
    public DataFetcher<CompletableFuture<User>> getUserDataFetcher() {
        return dataFetchingEnvironment -> {
            long userId = dataFetchingEnvironment.getArgument("id");
            return userLoader.load(userId); // Return CompletableFuture<User>
        };
    }

    // DataFetcher for Posts of a User
    public DataFetcher<List<Post>> getPostsDataFetcher() {
        return dataFetchingEnvironment -> {
            User user = dataFetchingEnvironment.getSource();
            return fetchPostsForUser(user.getId()); // Return a list of posts
        };
    }

    // DataFetcher for Comments of a Post
    public DataFetcher<List<Comment>> getCommentsDataFetcher() {
        return dataFetchingEnvironment -> {
            Post post = dataFetchingEnvironment.getSource();
            return fetchCommentsForPost(post.getId()); // Return a list of comments
        };
    }

    // Simulate fetching posts for a user (replace with actual database call)
    private List<Post> fetchPostsForUser(long userId) {
        return List.of();
    }

    // Simulate fetching comments for a post (replace with actual database call)
    private List<Comment> fetchCommentsForPost(long postId) {
        return List.of();
    }
}
