package com.csc340.restapidemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RestApiController {

    /**
     * Hello World API endpoint.
     *
     * @return response string.
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    /**
     * Greeting API endpoint.
     *
     * @param name the request parameter
     * @return the response string.
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "Dora") String name) {
        return "Hola, soy " + name;
    }
    /**
     * List all students.
     *
     * @return the list of students.
     */
    @GetMapping("students/all")
    public Object getAllStudents() throws IOException {
        return StudentService.getAllStudents();
    }
    /**
     * Get one student by Id
     *
     * @param id the unique student id.
     * @return the student.
     */
    @GetMapping("students/{id}")
    public Student getStudentById(@PathVariable int id) throws IOException {
      
        return StudentService.getStudentById(id);
    }
    /**
     * Create a new Student entry.
     *
     * @param student the new Student
     * @return the List of Students.
     */
    @PostMapping("students/create")
    public Object createStudent(@RequestBody Student student) throws IOException {
       
        StudentService.writeStudent(student);
       
        return StudentService.getAllStudents();
    }
    /**
     * Update a Student by id
     *
     * @param id      the id of student to be updated
     * @param student the new student info
     * @return the list of students
     */
    @PutMapping("students/update/{id}")
    public Object updateStudent(@PathVariable int id, @RequestBody Student student) throws IOException {
       
        StudentService.updateStudent(id, student);
       
        return StudentService.getAllStudents();
    }
    /**
     * Delete a Student by id
     *
     * @param id the id of student to be deleted.
     * @return the List of Students.
     */
    @DeleteMapping("students/delete/{id}")
    public Object deleteStudent(@PathVariable int id) throws IOException {
       
        StudentService.deleteStudent(id);
        
        return StudentService.getAllStudents();
    }

    /**
     * Get a quote from quotable and make it available our own API endpoint
     *
     * @return The quote json response
     */
    @GetMapping("/quote")
    public Object getQuote() {
        try {
            String url = "https://api.quotable.io/random";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            // We are expecting a String object as a response from the above API.
            String jsonQuote = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonQuote);

            // Parse out the most important info from the response and use it for whatever you want. In this case, just print.
            String quoteAuthor = root.get("author").asText();
            String quoteContent = root.get("content").asText();
            System.out.println("Author: " + quoteAuthor);
            System.out.println("Quote: " + quoteContent);

            return root;

        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /quote";
        }
    }

    /**
     * Get a list of universities from hipolabs and make them available at our own API
     * endpoint.
     *
     * @return json array
     */
    @GetMapping("/univ")
    public Object getUniversities() {
        try {
            String url = "http://universities.hipolabs.com/search?name=sports";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonListResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonListResponse);

            // The response from the above API is a JSON Array, which we loop through.
            for (JsonNode rt : root) {
                // Extract relevant info from the response and use it for what you want, in this case just print to the console.
                String name = rt.get("name").asText();
                String country = rt.get("country").asText();
                System.out.println(name + ": " + country);
            }

            return root;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /univ";
        }

    }

    /**
     * Get a random cat fact from the cat-facts API and make it available at our own API endpoint.
     *
     * @return the cat fact json response.
     */
    @GetMapping("/catfact")
    public Object getCatFact() {
        try {
            String url = "https://cat-fact.herokuapp.com/facts/random";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonFactResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonFactResponse);

            String catFact = root.get("text").asText();
            System.out.println("Cat Fact: " + catFact);

            return root;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /catfact";
        }
    }
}
