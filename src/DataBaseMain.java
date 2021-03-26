import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DataBaseMain {
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public void connect(){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraryProject","postgres", "1234");
            System.out.println("Connection to database done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveBooks(int clientId, int numBook, int bookId, int hours){
        try {
            Statement statement = connection.createStatement();
            String query = "select * from librarycard WHERE user_id=" + clientId;
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){

                String query1 = "select * from bookloan WHERE book_id=" + bookId +
                        " and library_card_id=" + resultSet.getInt("library_card_id");
                ResultSet resultSet1 = statement.executeQuery(query1);

                if(resultSet.getInt("number_of_loan") > 0 && resultSet1.next()){

                    while(resultSet1.next()){
                        int num = resultSet1.getInt("number_of_rented_books") + numBook;
                        String query2 = "update bookloan set number_of_rented_books=" + num + " WHERE book_id=" + bookId +
                                " and library_card_id=" + resultSet1.getInt("library_card_id");

                        statement.executeUpdate(query2);
                    }
                } else{
                    String query2 = "insert into bookloan (date_out, due_date, penalty, number_of_rented_books, library_card_id, book_id) " +
                            " values (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query2);
                    Date data = new Date();
                    Time currentTime = new Time(data.getTime());

                    String strTime = currentTime.toString();
                    String[] arrTime = strTime.split(":");
                    int timeInt = Integer.parseInt(arrTime[0]) + hours;
                    if(timeInt > 23){
                        timeInt -= 24;
                    }
                    arrTime[0] = String.valueOf(timeInt);
                    strTime = arrTime[0] + ":" + arrTime[1] + ":" + arrTime[2];

                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                    try{
                        data = dateFormat.parse(strTime);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    Time times = new Time(data.getTime());

                    preparedStatement.setTime(1, currentTime);
                    preparedStatement.setTime(2, times);
                    preparedStatement.setInt(3,0);
                    preparedStatement.setInt(4, numBook);
                    preparedStatement.setInt(5,resultSet.getInt("library_card_id"));
                    preparedStatement.setInt(6,bookId);
                    preparedStatement.executeUpdate();

                    int numLoan = resultSet.getInt("number_of_loan") + 1;
                    query2 = "update librarycard set number_of_loan=" + numLoan + " where user_id=" + clientId;
                    statement.executeUpdate(query2);
                }

                String query3 = "select * from book WHERE book_id=" + bookId;
                ResultSet resultSet2 = statement.executeQuery(query3);

                while(resultSet2.next()){
                    int num = resultSet2.getInt("book_number") - numBook;
                    String query2 = "update book set book_number=" + num + " WHERE book_id=" + bookId;
                    statement.executeUpdate(query2);
                    System.out.println("Book taken");
                }

                query3 = "select * from client where user_id=" + clientId;
                resultSet2 = statement.executeQuery(query3);

                while(resultSet2.next()){
                    int balance = resultSet2.getInt("balance") - (hours*500);
                    String query4 = "update client set balance=" + balance +" where user_id=" + clientId;
                    statement.executeUpdate(query4);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteBook(int bookId){
        try{
            Statement statement = connection.createStatement();
            String query = "delete from book where book_id=" + bookId;
            statement.executeUpdate(query);
            System.out.println("Book deleted");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addBook(int libraryId, String name, String author, int num){
        try {
            String query = "insert into book (name, author_name, library_id, book_number) values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, libraryId);
            preparedStatement.setInt(4, num);
            preparedStatement.executeUpdate();
            System.out.println("New books added");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addNumBook(int bookId, int bookNum){
        try {
            Statement statement = connection.createStatement();
            String query = "select * from book where book_id=" + bookId;
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int num = resultSet.getInt("book_number") + bookNum;

                String query2 = "update book set book_number=" + num + " WHERE book_id=" + bookId;
                statement.executeUpdate(query2);
                System.out.println("Books added");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewDebtors(int libraryId){
        try{
            Statement statement = connection.createStatement();
            String query = "select * from client where library_id=" + libraryId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String query2 = "select * from LibraryCard where user_id=" + userId;
                ResultSet resultSet1 = statement.executeQuery(query2);

                while(resultSet1.next()){
                    if(resultSet1.getInt("number_of_loan") > 0) {
                        int libraryCardId = resultSet1.getInt("library_card_id");
                        String query3 = "select * from BookLoan where library_card_id=" + libraryCardId;
                        ResultSet resultSet2 = statement.executeQuery(query3);

                        while (resultSet2.next()) {
                            Time dueDate = resultSet2.getTime("due_date");
                            Date data = new Date();
                            Time currentTime = new Time(data.getTime());

                            int penalty = currentTime.getHours() - dueDate.getHours();
                            if (penalty <= 0) {
                                penalty = 0;
                            } else {
                                penalty *= 1000;
                                String query4 = "update BookLoan set penalty=" + penalty + " where library_card_id=" + libraryCardId;
                                statement.executeUpdate(query4);

                                query4 = "select * from generalinfo where general_id=" + resultSet.getInt("general_id");
                                ResultSet resultSet3 = statement.executeQuery(query4);
                                while (resultSet3.next()) {
                                    System.out.println("Name " + resultSet3.getString("first_name") +
                                            " " + resultSet3.getString("last_name"));
                                    viewStatistics(userId);
                                }
                            }
                        }
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void viewStatistics(int userId){
        try {
            Statement statement = connection.createStatement();
            String query = "select * from librarycard WHERE user_id=" + userId;
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int numberOfLoan = resultSet.getInt("number_of_loan");

                if(numberOfLoan == 0){
                    System.out.println("You don't have any books ");
                } else {
                    String query2 = "select * from bookloan WHERE library_card_id=" + resultSet.getInt("library_card_id");
                    ResultSet resultSet1 = statement.executeQuery(query2);

                    while(resultSet1.next()){
                        int loanId = resultSet1.getInt("loan_id");
                        Time dateOut = resultSet1.getTime("date_out");
                        Time dueDate = resultSet1.getTime("due_date");
                        int numBook = resultSet1.getInt("number_of_rented_books");
                        int penalty = 0;

                        String query3 = "select * from book WHERE book_id=" + resultSet1.getInt("book_id");
                        ResultSet resultSet2 = statement.executeQuery(query3);

                        while(resultSet2.next()){
                            String name = resultSet2.getString("name");
                            String author = resultSet2.getString("author_name");

                            Date data = new Date();
                            Time currentTime = new Time(data.getTime());
                            penalty = currentTime.getHours() - dueDate.getHours();
                            if(penalty < 0){
                                penalty = 0;
                            } else{
                                penalty *= 1000;
                            }
                            System.out.println("[" + loanId + "], Date out '" + dateOut + "',  due date '" + dueDate +
                                    "',  penalty:" + penalty +",  number of book " + numBook + ",  book's name '" + name +
                                    "',  author '" + author + "'");
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void returnBook(int loanId){
        try {
            Statement statement = connection.createStatement();
            String query2 = "select * from bookloan WHERE loan_id=" + loanId;
            ResultSet resultSet1 = statement.executeQuery(query2);

            while(resultSet1.next()){
                Time dueDate = resultSet1.getTime("due_date");
                Date data = new Date();
                Time currentTime = new Time(data.getTime());
                int bookId = resultSet1.getInt("book_id");
                int bookNum = resultSet1.getInt("number_of_rented_books");
                int libraryCard = resultSet1.getInt("library_card_id");

                int pen = currentTime.getHours() - dueDate.getHours();
                boolean check = false;
                if(pen > 0){
                    System.out.println("Pay the penalty(1000 kzt - 1 hour) first: " + pen * 1000 + " kzt\n" +
                            " Press[1] to pay");
                    while(!(scanner.nextInt() == 1)){
                        System.out.println("Press[1] again");
                    }
                    check = true;
                }

                String query = "delete from bookloan where loan_id=" + loanId;
                statement.executeUpdate(query);

                query = "select * from librarycard WHERE library_card_id=" + libraryCard;
                ResultSet resultSet2 = statement.executeQuery(query);

                while(resultSet2.next()){
                    int num = resultSet2.getInt("number_of_loan") - 1;
                    String query4 = "update librarycard set number_of_loan=" + num + " WHERE library_card_id=" + libraryCard;
                    statement.executeUpdate(query4);

                    int userId = resultSet2.getInt("user_id");

                    String query5 = "select * from client where user_id=" + userId;
                    ResultSet resultSet = statement.executeQuery(query5);

                    while (resultSet.next()){
                        if(check) {
                            int balance = resultSet.getInt("balance") - (pen * 1000);
                            query4 = "update client set balance=" + balance + " where user_id=" + userId;
                            statement.executeUpdate(query4);
                        }
                    }
                }

                String query3 = "select * from book WHERE book_id=" + bookId;
                resultSet2 = statement.executeQuery(query3);

                while(resultSet2.next()){
                    int num = resultSet2.getInt("book_number") + bookNum;
                    String query4 = "update book set book_number=" + num + " WHERE book_id=" + bookId;
                    statement.executeUpdate(query4);
                    System.out.println("Book returned");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editClient(int clientId, String fname, String lname){
        try {
            Statement statement = connection.createStatement();
            String query = "select * from client where user_id=" + clientId;
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int generalId = resultSet.getInt("general_id");
                String query2 = "update generalinfo set first_name=" + "'" + fname + "'" + " where general_id=" + generalId;
                statement.executeUpdate(query2);

                query2 = "update generalinfo set last_name=" + "'" + lname + "'" + " where general_id=" + generalId;
                statement.executeUpdate(query2);
                System.out.println("Client updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addClient(int libraryId, String fname, String lname){
        try {
            Statement statement = connection.createStatement();
            String query = "insert into generalinfo (first_name, last_name) values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,fname);
            preparedStatement.setString(2,lname);
            preparedStatement.executeUpdate();

            query = "select * from generalinfo where first_name='" + fname + "' and last_name='" + lname + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                int generalId = resultSet.getInt("general_id");
                String query2 = "insert into client (general_id, library_id, balance) values(?,?,?)";
                preparedStatement = connection.prepareStatement(query2);
                preparedStatement.setInt(1,generalId);
                preparedStatement.setInt(2,libraryId);
                preparedStatement.setInt(3,100_000);
                preparedStatement.executeUpdate();

                query2 = "select * from client where general_id=" + generalId;
                ResultSet resultSet1 = statement.executeQuery(query2);

                while(resultSet1.next()){
                    int userId = resultSet1.getInt("user_id");

                    String query3 = "insert into LibraryCard (number_of_loan, user_id) values(?,?)";
                    preparedStatement = connection.prepareStatement(query3);
                    preparedStatement.setInt(1,0);
                    preparedStatement.setInt(2,userId);
                    preparedStatement.executeUpdate();

                    System.out.println("Client added");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Books> getBooks(int libraryId){
        ArrayList<Books> books = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String query = "select * from book WHERE library_id=" + libraryId;
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int id = resultSet.getInt("book_id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author_name");
                int library_id = resultSet.getInt("library_id");
                int bookNum = resultSet.getInt("book_number");

                books.add(new Books(id,name,author,library_id,bookNum));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<Library> getLibraries(){
        ArrayList<Library> libraries = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String query = "select * from Building";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int id = resultSet.getInt("library_id");
                String name = resultSet.getString("library_name");

                libraries.add(new Library(id,name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libraries;
    }

    public ArrayList<Users> getClients(int libraryId){
        ArrayList<Users> clients = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String query = "select * from client WHERE library_id=" + libraryId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int general_id = resultSet.getInt("general_id");
                int id = resultSet.getInt("user_id");
                int library_id = resultSet.getInt("library_id");
                int balance = resultSet.getInt("balance");

                String query1 = "select * from GeneralInfo WHERE general_id=" + general_id;
                ResultSet resultSet1 = statement.executeQuery(query1);
                String fName = null;
                String lName = null;
                while(resultSet1.next()) {
                    fName = resultSet1.getString("first_name");
                    lName = resultSet1.getString("last_name");
                }
                clients.add(new Users(id,general_id,library_id,fName,lName,balance));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    public Users getLibrarians(int libraryId){
        Users librarian = new Users();

        try {
            Statement statement = connection.createStatement();
            String query = "select * from librarian WHERE library_id=" + libraryId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int general_id = resultSet.getInt("general_id");
                int id = resultSet.getInt("librarian_id");
                int library_id = resultSet.getInt("library_id");

                String query1 = "select * from GeneralInfo WHERE general_id=" + id;
                ResultSet resultSet1 = statement.executeQuery(query1);
                String fName = null;
                String lName = null;
                while(resultSet1.next()) {
                    fName = resultSet1.getString("first_name");
                    lName = resultSet1.getString("last_name");
                }

                librarian = new Users(id,general_id,library_id,fName,lName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return librarian;
    }

}
