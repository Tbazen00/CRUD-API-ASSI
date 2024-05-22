package com.csc340.restapidemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService {
    private static final String FILE_PATH = "s.json"; // Path to JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static List<Student> getAllStudents() throws IOException {
        File file = new File(FILE_PATH);
        if (file.exists() && file.length() > 0) { // Check if file exists and is not empty
            return objectMapper.readValue(file, new TypeReference<List<Student>>() {}); // Read students from file
        }
        // Return an empty list
        return List.of();
    }

    // read a student by ID
    public static Student getStudentById(int id) throws IOException {
        // Filter the list of students by the given ID
        return getAllStudents()
                .stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElse(null); // Return null if student not found
    }

    // new student to JSON file
    public static void writeStudent(Student student) throws IOException {
        List<Student> students = getAllStudents(); // current list of students
        students.add(student); // Add the new student to list
        objectMapper.writeValue(new File(FILE_PATH), students); // Write list to file
    }

    // update student
    public static void updateStudent(int id, Student updatedStudent) throws IOException {
        // Map list of students and replace
        List<Student> students = getAllStudents()
                .stream()
                .map(student -> {
                    if (student.getId() == id) {
                        return updatedStudent; // updated student
                    }
                    return student; // Keep og student
                })
                .collect(Collectors.toList());
        objectMapper.writeValue(new File(FILE_PATH), students); // Write updated list to file
    }

    // delete a student by ID
    public static void deleteStudent(int id) throws IOException {
        // Filter list of student
        List<Student> students = getAllStudents()
                .stream()
                .filter(student -> student.getId() != id)
                .collect(Collectors.toList());
        objectMapper.writeValue(new File(FILE_PATH), students); // Write list to file
    }
}
