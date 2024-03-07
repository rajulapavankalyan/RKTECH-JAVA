CREATE DATABASE student_seat;

USE student_seat;

CREATE TABLE student(
  id INT AUTO_INCREMENT PRIMARY KEY,
  roll_no INT,
  name VARCHAR(50),
  branch VARCHAR(50),
  semester INT,
  year INT
  );

INSERT INTO student (roll_no, name, branch, semester, year) 
VALUES 
(1, 'John Doe', 'Computer Science', 6, 2023),
(2, 'Jane Smith', 'Electronics', 7, 2022),
(3, 'Mike Johnson', 'Mechanical', 5, 2024),
(4, 'Alice Davis', 'Civil', 8, 2021),
(5, 'Bob Brown', 'Chemical', 4, 2025),
(6, 'Charlie Miller', 'Aerospace', 5, 2023),
(7, 'David Wilson', 'Biomedical', 6, 2022),
(8, 'Eva Johnson', 'Ceramics', 7, 2021),
(9, 'Frank Davis', 'Civil', 8, 2024),
(10, 'Grace Brown', 'Computer Science', 4, 2025),
(11, 'Harry Davis', 'Electronics', 5, 2023),
(12, 'Irene Johnson', 'Mechanical', 6, 2022),
(13, 'Jack Wilson', 'Metallurgy', 7, 2021),
(14, 'Karen Davis', 'Mining', 8, 2024),
(15, 'Leonard Brown', 'Petroleum', 4, 2025);
