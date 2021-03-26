public class Books {
    private int id;
    private String name;
    private String author;
    private int libraryId;
    private int bookNumber;

    public Books(int id, String name, String author, int libraryId, int bookNumber) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.libraryId = libraryId;
        this.bookNumber = bookNumber;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", libraryId=" + libraryId +
                '}';
    }
}
