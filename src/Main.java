import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static DataBaseMain dataBaseMain = new DataBaseMain();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        dataBaseMain.connect();
        System.out.println("------------------Toizhanov Rafael-------------------");
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------Library-----------------------");
        System.out.println("-----------------------------------------------------");
        selectLibrary();
        System.out.println("The program ended");
    }

    public static void selectLibrary() {
        while (true) {
            ArrayList<Library> libraries = dataBaseMain.getLibraries();
            System.out.println("SELECT LIBRARY:");

            for (Library i : libraries) {
                System.out.println("[" + i.getId() + "] " + i.getName());
            }
            System.out.println("ENTER[3] to exit");
            int choose = scanner.nextInt();

            System.out.println("-----------------------------------------------------");
            if (choose == 3)
                break;

            for (Library i : libraries) {
                if (choose == i.getId()) {
                    System.out.println("Welcome to " + i.getName());
                    System.out.println("-----------------------------------------------------");
                    while (true) {
                        System.out.println("ENTER[1] to enter in librarian account\n" +
                                "ENTER[2] to enter in client's account\n" +
                                "ENTER[3] to exit");
                        int chooseAcc = scanner.nextInt();

                        System.out.println("-----------------------------------------------------");
                        if (chooseAcc == 1) {
                            selectLibrarian(choose);
                        } else if (chooseAcc == 2) {
                            selectClient(choose);
                        } else if (chooseAcc == 3) {
                            break;
                        } else {
                            System.out.println("Incorrect choice. Try again");
                        }
                    }
                }
            }
        }
    }

    public static void showBooks(int libraryId) {
        ArrayList<Books> books = dataBaseMain.getBooks(libraryId);

        System.out.println("Available books");
        for (Books i : books) {
            if (i.getBookNumber() > 0) {
                System.out.println("[" + i.getId() + "] Name '" + i.getName() + "',  author '"
                        + i.getAuthor() + "',  number " + i.getBookNumber());
            }
        }
        System.out.println("-----------------------------------------------------");
    }

    public static void selectLibrarian(int libraryId) {
        Users librarian = dataBaseMain.getLibrarians(libraryId);
        System.out.println("Welcome, my name is " + librarian.getFirstName() + " " + librarian.getLastName() +
                " and I am librarian");
        System.out.println("-----------------------------------------------------");
        while (true) {
            System.out.println("ENTER[1] to edit books\n" +
                    "ENTER[2] to edit clients\n" +
                    "ENTER[3] to create list of debtors\n" +
                    "ENTER[4] to create list of available books\n" +
                    "ENTER[5] to exit");

            int choose = scanner.nextInt();
            System.out.println("-----------------------------------------------------");
            if (choose == 1) {
                editBooks(libraryId);
            } else if (choose == 2) {
                editClient(libraryId);
            } else if (choose == 3) {
                Date data = new Date();
                Time currentTime = new Time(data.getTime());
                System.out.println("Current time " + currentTime);
                dataBaseMain.viewDebtors(libraryId);
                System.out.println("-----------------------------------------------------");
            } else if (choose == 4) {
                showBooks(libraryId);
            } else if (choose == 5) {
                break;
            } else {
                System.out.println("Incorrect choice. Try again");
            }
        }

    }

    public static void editBooks(int libraryId){
        while(true){
            System.out.println("Enter[1] to add number of books\n" +
                    "Enter[2] to add new book\n" +
                    "Enter[3] to delete book\n" +
                    "Enter[4] to exit");

            int chose = scanner.nextInt();

            if(chose == 1){
                System.out.println("Select books");
                ArrayList<Books> books = dataBaseMain.getBooks(libraryId);
                for (Books i : books) {
                    System.out.println("[" + i.getId() + "] Name '" + i.getName() + "',  author '"
                            + i.getAuthor() + "',  number " + i.getBookNumber());
                }
                int bookId = scanner.nextInt();

                System.out.println("How many books do you want to add?");
                int num = scanner.nextInt();

                dataBaseMain.addNumBook(bookId,num);
                System.out.println("-----------------------------------------------------");
            } else if(chose == 2){
                scanner.nextLine();
                System.out.println("Enter the title of the book");
                String name = scanner.nextLine();

                System.out.println("Enter author");
                String author = scanner.nextLine();

                System.out.println("Enter number of books");
                int num = scanner.nextInt();

                dataBaseMain.addBook(libraryId,name,author,num);
                System.out.println("-----------------------------------------------------");
            } else if(chose == 3){
                System.out.println("Select books");
                ArrayList<Books> books = dataBaseMain.getBooks(libraryId);
                for (Books i : books) {
                    System.out.println("[" + i.getId() + "] Name '" + i.getName() + "',  author '"
                            + i.getAuthor() + "',  number " + i.getBookNumber());
                }
                int bookId = scanner.nextInt();

                dataBaseMain.deleteBook(bookId);
                System.out.println("-----------------------------------------------------");
            } else if(chose == 4){
                break;
            } else {
                System.out.println("Incorrect choice. Try again");
            }

        }
    }

    public static void editClient(int libraryId){

        while(true){
            System.out.println("Enter[1] to change first name and last name of clients\n" +
                    "Enter[2] to add clients\n" +
                    "Enter[3] to exit");

            int choose = scanner.nextInt();
            System.out.println("-----------------------------------------------------");

            if(choose == 1){
                try {
                    System.out.println("Select client");
                    ArrayList<Users> clients = dataBaseMain.getClients(libraryId);
                    for (Users i : clients) {
                        System.out.println("[" + clients.indexOf(i) + "] Name " + i.getFirstName() + " " + i.getLastName());
                    }

                    int chooseClient = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Enter new first name and last name for client");
                    String fname = scanner.nextLine();
                    String lname = scanner.nextLine();
                    dataBaseMain.editClient(clients.get(chooseClient).getId(), fname, lname);

                } catch (Exception e) {
                    System.out.println("Enter correct client");
                }
            } else if(choose == 2){
                scanner.nextLine();
                System.out.println("Enter new first name and last name for new client");
                String fname = scanner.nextLine();
                String lname = scanner.nextLine();
                dataBaseMain.addClient(libraryId,fname,lname);
            } else if(choose == 3){
                break;
            } else {
                System.out.println("Incorrect choice. Try again");
            }

        }

    }

    public static void selectClient(int libraryId) {
        ArrayList<Users> clients = dataBaseMain.getClients(libraryId);
        boolean check = true;

        System.out.println("Select client");

        while (check) {
            try {
                for (Users i : clients) {
                    System.out.println("[" + clients.indexOf(i) + "] Name " + i.getFirstName() + " " + i.getLastName() +
                            " balance = " + i.getBalance() + " kzt");
                }

                int chooseClient = scanner.nextInt();
                System.out.println("-----------------------------------------------------");
                System.out.println("Welcome " + clients.get(chooseClient).getFirstName() + " " + clients.get(chooseClient).getLastName() +
                        " your balance = " + clients.get(chooseClient).getBalance() + " kzt");
                while (true) {
                    System.out.println("-----------------------------------------------------");
                    System.out.println("ENTER[1] to view available books\n" +
                            "ENTER[2] to take book\n" +
                            "ENTER[3] to return book\n" +
                            "ENTER[4] to view statistics\n" +
                            "ENTER[5] to exit");

                    int choice = scanner.nextInt();
                    System.out.println("-----------------------------------------------------");
                    if (choice == 1) {
                        showBooks(libraryId);
                    } else if (choice == 2) {
                        Date data = new Date();
                        Time currentTime = new Time(data.getTime());
                        System.out.println("Current time " + currentTime);
                        takeBook(libraryId, clients.get(chooseClient).getId());
                    } else if (choice == 3) {
                        Date data = new Date();
                        Time currentTime = new Time(data.getTime());
                        System.out.println("Current time " + currentTime);
                        returnBook(clients.get(chooseClient).getId());
                    } else if (choice == 4) {
                        Date data = new Date();
                        Time currentTime = new Time(data.getTime());
                        System.out.println("Current time " + currentTime);
                        dataBaseMain.viewStatistics(clients.get(chooseClient).getId());
                    } else if (choice == 5) {
                        check = false;
                        break;
                    } else {
                        System.out.println("Incorrect choice. Try again");
                    }
                }
            } catch (Exception e) {
                System.out.println("Enter correct client");
            }
        }
    }

    public static void returnBook(int clientId){

        while(true){
            System.out.println("Select the book you want to return");

            dataBaseMain.viewStatistics(clientId);
            System.out.println("Enter[0] to exit");
            int num = scanner.nextInt();
            System.out.println("-----------------------------------------------------");
            if(num == 0){
                break;
            }
            dataBaseMain.returnBook(num);
        }

    }

    public static void takeBook(int libraryId, int clientId) {

        while (true) {
            System.out.println("1 hour - 500kzt, penalty: 1 hour - 1000kzt");
            System.out.println("Select books");

            showBooks(libraryId);
            System.out.println("Or press[0] to exit");
            int choose = 0;
            ArrayList<Books> books = null;
            try {
                choose = scanner.nextInt();
                System.out.println("-----------------------------------------------------");
                if(choose == 0){
                    break;
                }
                books = dataBaseMain.getBooks(libraryId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Books i : books) {
                if (i.getBookNumber() > 0) {
                    if (choose == i.getId()) {
                        System.out.println("How many books do you need?");
                        int num = scanner.nextInt();

                        if (i.getBookNumber() < num) {
                            System.out.println("We don't have so many books");
                        } else {
                            System.out.println("Enter how long you need the book\n" +
                                    "Hours:");
                            int hours = 0;

                            while (true) {
                                try {
                                    hours = scanner.nextInt();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (hours < 0 || hours > 23) {
                                    System.out.println("Enter hours again");
                                } else {
                                    break;
                                }
                            }
                            dataBaseMain.giveBooks(clientId, num, i.getId(), hours);
                        }

                    }
                }
            }
        }
    }

}