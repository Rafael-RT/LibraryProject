-- Database: LibraryProject

-- DROP DATABASE "LibraryProject";

CREATE DATABASE "LibraryProject"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
CREATE TABLE GeneralInfo(
	general_id int PRIMARY KEY,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL
);

CREATE TABLE Building(
	library_id int PRIMARY KEY,
	library_name varchar(50) NOT NULL
);

CREATE TABLE Client(
	user_id int PRIMARY KEY,
	general_id int,
	library_id int,
	balance int,
	FOREIGN KEY (general_id) REFERENCES GeneralInfo(general_id),
	FOREIGN KEY (library_id) REFERENCES Building(library_id)
);
CREATE TABLE Librarian(
	librarian_id int PRIMARY KEY,
	general_id int,
	library_id int,
	FOREIGN KEY (general_id) REFERENCES GeneralInfo(general_id),
	FOREIGN KEY (library_id) REFERENCES Building(library_id)
);

CREATE TABLE Book(
	book_id int PRIMARY KEY,
	name varchar(75) NOT NULL,
	author_name varchar(50) NOT NULL,
	library_id int,
	book_number int,
	FOREIGN KEY (library_id) REFERENCES Building(library_id)
);
CREATE TABLE LibraryCard(
	library_card_id int PRIMARY KEY,
	number_of_loan int,
	user_id int,
	FOREIGN KEY (user_id) REFERENCES Client(user_id)
);
CREATE TABLE BookLoan(
	loan_id serial PRIMARY KEY,
	date_out date,
	due_date date,
	penalty int,
	number_of_rented_books int,
	library_card_id int,
	book_id int,
	FOREIGN KEY (library_card_id) REFERENCES LibraryCard(library_card_id),
	FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

INSERT INTO Building Values
(1, 'Central сity library named after A.P. Chekhov'),
(2, 'City youth library named after Zhambyl')

INSERT INTO Book VALUES
	(1, 'Anna Karenina', 'Leo Tolstoy', 1, 20),
	(2, 'To Kill a Mockingbird', 'Harper Lee', 1, 20),
	(3, 'The Great Gatsby', 'Francies Scott Fitzgerald', 1, 20),
	(4, 'One Hundred Years of Solitude', 'Gabriel García Márquez', 1, 20),
	(5, 'A Passage to India', 'Edward Forster', 1, 20),
	(6, 'Invisible Man', 'Ralph Allison', 2, 20),
	(7, 'Don Quixote', 'Miguel de Cervantes', 2, 20),
	(8, 'Beloved', 'Toni Morrison', 2, 20),
	(9, 'Mrs. Dalloway', 'Virginia Woolf', 2, 20),
	(10, 'Things Fall Apart', 'Chinua Achebe', 2, 20);

INSERT INTO GeneralInfo VALUES
	(1, 'Ramazan', 'Bolat'),
	(2, 'Adina', 'Zhusupova'),
	(3, 'Dastan', 'Makhutov'),
	(4, 'Rafael', 'Toizhanov'),
	(5, 'Madi', 'Askarov'),
    (6, 'Ulan', 'Erikov')

INSERT INTO Librarian Values
	(1,1,1),
	(2,2,2)

INSERT INTO Client VALUES
	(1,3,1,100000),
	(2,4,1,100000),
	(3,5,2,100000),
	(4,6,2,100000)

INSERT INTO LibraryCard VALUES
	(1,0,1),
	(2,0,2),
	(3,0,3),
	(4,0,4)
