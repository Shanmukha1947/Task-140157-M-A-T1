import java.util.List;

public class Post {
    private long id;
    private String title;
    private User user; // A post belongs to a user
    private List<Comment> comments; // A post can have multiple comments

    public Post(long id, String title, User user, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.comments = comments;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
