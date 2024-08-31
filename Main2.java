import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Student {
    private int studentId;
    private String name;
    private String className;

    public Student(int studentId, String name, String className) {
        this.studentId = studentId;
        this.name = name;
        this.className = className;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }
}

class Room {
    private String roomId;
    private int capacity;
    private Student[] seats;

    public Room(String roomId, int capacity) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.seats = new Student[capacity]; // Initialize empty seats
    }

    public String getRoomId() {
        return roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    public Student[] getSeats() {
        return seats;
    }

    public void setSeat(int index, Student student) {
        this.seats[index] = student;
    }

    public Student getSeat(int index) {
        return this.seats[index];
    }
}

class ExamSeatingSystem {
    private List<Student> students;
    private List<Room> rooms;

    public ExamSeatingSystem() {
        students = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void allocateSeats() {
        Collections.shuffle(students);  // Shuffle students to randomize seating
        int seatIndex = 0;

        for (Room room : rooms) {
            for (int i = 0; i < room.getCapacity(); i++) {
                if (seatIndex < students.size()) {
                    room.setSeat(i, students.get(seatIndex));
                    seatIndex++;
                }
            }

            // Enforce no same class adjacent rule
            for (int i = 1; i < room.getCapacity(); i++) {
                if (room.getSeat(i) != null && room.getSeat(i - 1) != null) {
                    if (room.getSeat(i).getClassName().equals(room.getSeat(i - 1).getClassName())) {
                        // Try to find a non-conflicting seat
                        for (int j = i + 1; j < room.getCapacity(); j++) {
                            if (room.getSeat(j) != null && 
                                !room.getSeat(j).getClassName().equals(room.getSeat(i - 1).getClassName())) {
                                // Swap seats
                                Student temp = room.getSeat(i);
                                room.setSeat(i, room.getSeat(j));
                                room.setSeat(j, temp);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void displaySeating() {
        for (Room room : rooms) {
            System.out.println("Room " + room.getRoomId() + " Seating Arrangement:");
            for (int index = 0; index < room.getCapacity(); index++) {
                Student student = room.getSeat(index);
                if (student != null) {
                    System.out.println("  Seat " + (index + 1) + ": " + student.getName() + " (Class: " + student.getClassName() + ")");
                } else {
                    System.out.println("  Seat " + (index + 1) + ": Empty");
                }
            }
            System.out.println();
        }
    }

    // Method to load students from CSV file
    public void loadStudentsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int studentId = Integer.parseInt(values[0].trim());
                String name = values[1].trim();
                String className = values[2].trim();
                addStudent(new Student(studentId, name, className));
            }
            System.out.println("Loaded students from CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Main2 {
    public static void main(String[] args) {
        // Create the exam seating system
        ExamSeatingSystem system = new ExamSeatingSystem();

        // Load students from CSV file
        system.loadStudentsFromCSV("students.csv");

        // Add rooms with capacities
        system.addRoom(new Room("Room 101", 25));
        system.addRoom(new Room("Room 102", 25));
        system.addRoom(new Room("Room 102", 25));
        system.addRoom(new Room("Room 102", 25));


        // Allocate seats and display the arrangement
        system.allocateSeats();
        system.displaySeating();
    }
}
