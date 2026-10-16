package StudentManagementSystemm;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StudentManagementSystemm {

    // Enum for student status
    enum StudentStatus {
        ACTIVE, INACTIVE, COMPLETED
    }

    // Course class
    static class Course {
        private String courseId;
        private String courseName;
        private int durationWeeks;

        Course(String courseId, String courseName, int durationWeeks) {
            if (courseId.trim().isEmpty() || courseName.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Course ID and name cannot be empty.");
            }
            if (durationWeeks <= 0) {
                throw new IllegalArgumentException(
                        "Duration must be greater than zero.");
            }

            this.courseId = courseId;
            this.courseName = courseName;
            this.durationWeeks = durationWeeks;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public int getDurationWeeks() {
            return durationWeeks;
        }
    }

    // Student class
    static class Student {
        private int id;
        private String name;
        private String email;
        private double marks;
        private int attendance;
        private StudentStatus status;
        private Course course;

        Student(int id, String name, String email, double marks,
                int attendance, StudentStatus status, Course course) {

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Student ID must be positive.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Name cannot be empty.");
            }
            if (email == null ||
                    !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                throw new IllegalArgumentException(
                        "Enter a valid email address.");
            }

            this.id = id;
            this.name = name.trim();
            this.email = email.trim();
            setMarks(marks);
            setAttendance(attendance);
            setStatus(status);
            setCourse(course);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public double getMarks() {
            return marks;
        }

        public int getAttendance() {
            return attendance;
        }

        public StudentStatus getStatus() {
            return status;
        }

        public Course getCourse() {
            return course;
        }

        public void setMarks(double marks) {
            if (marks < 0 || marks > 100) {
                throw new IllegalArgumentException(
                        "Marks must be between 0 and 100.");
            }
            this.marks = marks;
        }

        public void setAttendance(int attendance) {
            if (attendance < 0 || attendance > 100) {
                throw new IllegalArgumentException(
                        "Attendance must be between 0 and 100.");
            }
            this.attendance = attendance;
        }

        public void setStatus(StudentStatus status) {
            if (status == null) {
                throw new IllegalArgumentException(
                        "Status cannot be null.");
            }
            this.status = status;
        }

        public void setCourse(Course course) {
            if (course == null) {
                throw new IllegalArgumentException(
                        "Course cannot be null.");
            }
            this.course = course;
        }

        public String calculateGrade() {
            if (marks >= 90) return "A";
            if (marks >= 80) return "B";
            if (marks >= 70) return "C";
            if (marks >= 60) return "D";
            return "F";
        }
    }

    // Student service: manages all student operations
    static class StudentService {
        private Map<Integer, Student> students = new HashMap<>();

        public void addStudent(Student student) {
            if (students.containsKey(student.getId())) {
                throw new IllegalArgumentException(
                        "Student ID already exists.");
            }
            students.put(student.getId(), student);
            System.out.println("Student added successfully.");
        }

        public Student searchById(int id) {
            return students.get(id);
        }

        public void searchByName(String name) {
            boolean found = false;

            for (Student s : students.values()) {
                if (s.getName().toLowerCase()
                        .contains(name.trim().toLowerCase())) {
                    printStudent(s);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No student found.");
            }
        }

        public void displayAllStudents() {
            if (students.isEmpty()) {
                System.out.println("No student records available.");
                return;
            }

            printHeader();
            for (Student s : students.values()) {
                printStudent(s);
            }
        }

        public void updateMarks(int id, double marks) {
            Student s = searchById(id);
            if (s == null) {
                throw new IllegalArgumentException("Student not found.");
            }
            s.setMarks(marks);
            System.out.println("Marks updated successfully.");
        }

        public void updateAttendance(int id, int attendance) {
            Student s = searchById(id);
            if (s == null) {
                throw new IllegalArgumentException("Student not found.");
            }
            s.setAttendance(attendance);
            System.out.println("Attendance updated successfully.");
        }

        public void removeStudent(int id) {
            if (students.remove(id) != null) {
                System.out.println("Student removed successfully.");
            } else {
                System.out.println("Student not found.");
            }
        }

        public void displayTopper() {
            Student topper = null;

            for (Student s : students.values()) {
                if (topper == null ||
                        s.getMarks() > topper.getMarks()) {
                    topper = s;
                }
            }

            if (topper == null) {
                System.out.println("No student records available.");
            } else {
                System.out.println("\n========== TOPPER ==========");
                printHeader();
                printStudent(topper);
            }
        }

        public Collection<Student> getAllStudents() {
            return students.values();
        }

        public void printHeader() {
            System.out.printf(
                "%-8s %-16s %-25s %-8s %-12s %-8s %-12s %-15s%n",
                "ID", "Name", "Email", "Marks", "Attendance",
                "Grade", "Status", "Course");
            System.out.println(
                "----------------------------------------------------------------------------------------------------------------");
        }

        public void printStudent(Student s) {
            System.out.printf(
                "%-8d %-16s %-25s %-8.2f %-12d %-8s %-12s %-15s%n",
                s.getId(), s.getName(), s.getEmail(), s.getMarks(),
                s.getAttendance(), s.calculateGrade(), s.getStatus(),
                s.getCourse().getCourseName());
        }
    }

    // File handling
    static class StudentFileService {
        private static final String FILE_NAME = "students.txt";

        public void save(Collection<Student> students) throws IOException {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(FILE_NAME))) {

                for (Student s : students) {
                    String line =
                            s.getId() + "|" +
                            s.getName() + "|" +
                            s.getEmail() + "|" +
                            s.getMarks() + "|" +
                            s.getAttendance() + "|" +
                            s.getStatus().name() + "|" +
                            s.getCourse().getCourseId() + "|" +
                            s.getCourse().getCourseName() + "|" +
                            s.getCourse().getDurationWeeks();

                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Records saved to " + FILE_NAME);
        }

        public List<Student> load() throws IOException {
            List<Student> result = new ArrayList<>();
            Path path = Paths.get(FILE_NAME);

            if (!Files.exists(path)) {
                return result;
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    try {
                        String[] d = line.split("\\|", -1);

                        if (d.length != 9) {
                            throw new IllegalArgumentException(
                                    "Invalid number of fields.");
                        }

                        Course course = new Course(
                                d[6], d[7], Integer.parseInt(d[8]));

                        Student student = new Student(
                                Integer.parseInt(d[0]),
                                d[1],
                                d[2],
                                Double.parseDouble(d[3]),
                                Integer.parseInt(d[4]),
                                StudentStatus.valueOf(d[5]),
                                course);

                        result.add(student);

                    } catch (IllegalArgumentException e) {
                        System.out.println("Skipping invalid file record at line "
                                + lineNumber + ": " + e.getMessage());
                    }
                }
            }

            return result;
        }
    }

    // Main program
    static Scanner sc = new Scanner(System.in);
    static StudentService service = new StudentService();
    static StudentFileService fileService = new StudentFileService();

    public static void main(String[] args) {

        // Load previously saved students
        try {
            for (Student s : fileService.load()) {
                try {
                    service.addStudent(s);
                } catch (IllegalArgumentException e) {
                    System.out.println("Could not load student: "
                            + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load records: " + e.getMessage());
        }

        boolean running = true;

        while (running) {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search by Student ID");
            System.out.println("4. Search by Student Name");
            System.out.println("5. Update Marks");
            System.out.println("6. Update Attendance");
            System.out.println("7. Calculate Grade");
            System.out.println("8. Remove Student");
            System.out.println("9. Display Topper");
            System.out.println("10. Save Records");
            System.out.println("11. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        service.displayAllStudents();
                        break;
                    case 3:
                        searchById();
                        break;
                    case 4:
                        searchByName();
                        break;
                    case 5:
                        updateMarks();
                        break;
                    case 6:
                        updateAttendance();
                        break;
                    case 7:
                        calculateGrade();
                        break;
                    case 8:
                        removeStudent();
                        break;
                    case 9:
                        service.displayTopper();
                        break;
                    case 10:
                        saveData();
                        break;
                    case 11:
                        saveData();
                        running = false;
                        System.out.println("Thank you for using the system.");
                        break;
                    default:
                        System.out.println("Invalid choice. Enter 1 to 11.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }

    static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static double readDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void addStudent() {
        System.out.println("\n----- ADD STUDENT -----");

        System.out.print("Student ID: ");
        int id = readInt();

        System.out.print("Student Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Marks (0-100): ");
        double marks = readDouble();

        System.out.print("Attendance (0-100): ");
        int attendance = readInt();

        System.out.print("Status (ACTIVE/INACTIVE/COMPLETED): ");
        StudentStatus status = StudentStatus.valueOf(
                sc.nextLine().trim().toUpperCase());

        System.out.print("Course ID: ");
        String courseId = sc.nextLine().trim();

        System.out.print("Course Name: ");
        String courseName = sc.nextLine().trim();

        System.out.print("Course Duration in Weeks: ");
        int duration = readInt();

        Course course = new Course(courseId, courseName, duration);

        Student student = new Student(
                id, name, email, marks, attendance, status, course);

        service.addStudent(student);
        saveData();
    }

    static void searchById() {
        System.out.print("Enter Student ID: ");
        int id = readInt();

        Student s = service.searchById(id);

        if (s == null) {
            System.out.println("Student not found.");
        } else {
            service.printHeader();
            service.printStudent(s);
        }
    }

    static void searchByName() {
        System.out.print("Enter Student Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        service.printHeader();
        service.searchByName(name);
    }

    static void updateMarks() {
        System.out.print("Enter Student ID: ");
        int id = readInt();

        System.out.print("Enter new Marks: ");
        double marks = readDouble();

        service.updateMarks(id, marks);
        saveData();
    }

    static void updateAttendance() {
        System.out.print("Enter Student ID: ");
        int id = readInt();

        System.out.print("Enter new Attendance: ");
        int attendance = readInt();

        service.updateAttendance(id, attendance);
        saveData();
    }

    static void calculateGrade() {
        System.out.print("Enter Student ID: ");
        int id = readInt();

        Student s = service.searchById(id);

        if (s == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Student Name: " + s.getName());
            System.out.println("Marks: " + s.getMarks());
            System.out.println("Grade: " + s.calculateGrade());
        }
    }

    static void removeStudent() {
        System.out.print("Enter Student ID: ");
        int id = readInt();

        int before = service.getAllStudents().size();
        service.removeStudent(id);
        int after = service.getAllStudents().size();

        if (after < before) {
            saveData();
        }
    }

    static void saveData() {
        try {
            fileService.save(service.getAllStudents());
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }
}
