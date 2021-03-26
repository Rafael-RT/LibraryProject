public class Users {
    private int id;
    private int generalId;
    private int libraryId;
    private String firstName;
    private String lastName;
    private int balance;

    public Users() {
    }

    public Users(int id, int generalId, int libraryId, String firstName, String lastName, int balance) {
        this.id = id;
        this.generalId = generalId;
        this.libraryId = libraryId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public Users(int id, int generalId, int libraryId, String firstName, String lastName) {
        this.id = id;
        this.generalId = generalId;
        this.libraryId = libraryId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGeneralId() {
        return generalId;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", generalId=" + generalId +
                ", libraryId=" + libraryId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
