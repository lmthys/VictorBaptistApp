package e.lmandrew.victorbaptistchurch;

public class Post_Item {
    private String username;

    private String title;

    private String summary;

    public Post_Item(String username, String title, String summary){
        this.username = username;
        this.title = title;
        this.summary = summary;
    }

    public Post_Item(){

    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
