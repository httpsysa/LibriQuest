CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    published_date DATE,
    genre VARCHAR(100),
    status VARCHAR(50)
);

CREATE TABLE requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL
);

CREATE TABLE borrowed (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Auto-incrementing ID for borrowed records
    book_id INT,                         -- Foreign key referencing the catalog ID
    title VARCHAR(255),
    author VARCHAR(255),
    publisher VARCHAR(255),
    borrow_time DATETIME,
    return_time DATETIME,
    student_name VARCHAR(255),
    contact_number VARCHAR(255),
    email VARCHAR(255),
    FOREIGN KEY (book_id) REFERENCES books(id) -- Reference to the books table
);
