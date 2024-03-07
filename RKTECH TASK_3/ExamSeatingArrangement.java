// Task 5 - Exam Seating Arrangement System
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class ExamSeatingArrangement extends JFrame implements ActionListener {
	
	// create label and fields for no of students and seats available
	// create a button
	private static final long serialVersionUID = 1L;
	private JLabel totalNoOfStudentsLabel, totalSeatsAvailableLabel;
    private JTextField totalNoOfStudentsField, totalSeatsAvailableField;
    private JButton Button;
    private JTextArea seatingArrangement;
    private Connection connection;

    public ExamSeatingArrangement() {
        super("Seating Arrangement"); 
        // Create connection to MySQL database
        try {
        	//created a student_seating database 
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_seat", "root", "pavan@1234"); 
            //use mysql workbench and give your username and password of mysql server
            // add the external mysql-connector jar file into the libraries for the jdbc connection
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize GUI components
        totalNoOfStudentsLabel = new JLabel("Total Students:");
        totalNoOfStudentsField = new JTextField(10);
        totalSeatsAvailableLabel = new JLabel("Seats Available:");
        totalSeatsAvailableField = new JTextField(10);
        Button = new JButton("Generate Seating Arrangement");
        Button.setPreferredSize(new Dimension(250, 70));
        seatingArrangement = new JTextArea(15, 60);

        // Add action listener to button
        Button.addActionListener(this);

        // Layout two components
        JPanel Panel1 = new JPanel(new FlowLayout());
        Panel1.add(totalNoOfStudentsLabel);
        Panel1.add(totalNoOfStudentsField);
        Panel1.add(totalSeatsAvailableLabel);
        Panel1.add(totalSeatsAvailableField);
        Panel1.add(Button);

        JPanel Panel2 = new JPanel();
        Panel2.add(seatingArrangement);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(Panel1, BorderLayout.NORTH); // at the top
        getContentPane().add(Panel2, BorderLayout.CENTER); // to the center

        // Set frame size and visibility
        setSize(800, 600);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // default exit operation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Button) {
            int totalStudents = Integer.parseInt(totalNoOfStudentsField.getText());
            int seatsAvailable = Integer.parseInt(totalSeatsAvailableField.getText());

            if (totalStudents > seatsAvailable) { // if no of students exceeds the total no of seats available raise the error
                seatingArrangement.setText("Error Message: Number of students exceeds available seats.");
            } else {
                generateSeatingArrangement(totalStudents, seatsAvailable);
            }
        }
    }

    private void generateSeatingArrangement(int totalStudents, int seatsAvailable) {
        seatingArrangement.setText("");

        // Fetch students and their branch
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student LIMIT " + totalStudents; // query to fetch all data from student table
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int rollNo = resultSet.getInt("roll_no");
                String name = resultSet.getString("name");
                String branch = resultSet.getString("branch");
                students.add(new Student(rollNo, name, branch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            seatingArrangement.setText("Error: Failed to retrieve student data.");
            return;
        }

        // Sort students based on branch
        Collections.sort(students, (student1, student2) -> student1.getBranch().compareTo(student2.getBranch()));

        int row = 1, seat = 1;
        String previousBranch = null;

        for (Student student : students) { 
            if (seat % 5 == 0 || (previousBranch != null && previousBranch.equals(student.getBranch()))) {
                row++;     
                // if two students come from same branch or if seats are filled for that row , increment the row and assign seat=1
                seat = 1; 
            }

            seatingArrangement.append(String.format("Row %d, Seat %d: %d - %s\n", row, seat, student.getRollNo(), student.getName()));
            previousBranch = student.getBranch();
            seat++;
        }
    }

    // Create a Student class to hold student information
    class Student {
        private int rollNo;
        private String name;
        private String branch;

        public Student(int rollNo, String name, String branch) {
            this.rollNo = rollNo;
            this.name = name;
            this.branch = branch;
        }
        //getter methods
        public int getRollNo() {
            return rollNo;
        }

        public String getName() {
            return name;
        }

        public String getBranch() {
            return branch;
        }
    }

    public static void main(String[] args) {
        new ExamSeatingArrangement();
    }
}
