public class Comment {
    private long id;
    private String text;
    private Post post; // A comment belongs to a post

    public Comment(long id, String text, Post post) {
        this.id = id;
        this.text = text;
        this.post = post;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
