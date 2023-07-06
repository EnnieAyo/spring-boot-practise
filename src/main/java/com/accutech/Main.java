package com.accutech;

import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/customers")
public class Main {
    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @GetMapping("/greet")
    public GreetResponse greet(){
        GreetResponse response = new GreetResponse("hello",
                List.of("JAVA", "PYTHON", "JAVASCRIPT"),
                new Person("Ennie", 30, 30_000));
        return response;
    }

    record Person(String name, int age, double savings){}
    record GreetResponse( String greet,
                          List<String> favProgrammingLanguages,
                          Person person){}
    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){}

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request){
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());
        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id){
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    @Transactional
    public List<Customer> updateCustomer(@RequestParam(required = false, name = "name") String name,
                                         @RequestParam(required = false, name = "age") Integer age,
                                         @PathVariable("customerId") Integer id){
        Customer customer = customerRepository.findById(id).get();
        customer.setName(name);
        customer.setAge(age);
        return List.of(customer);
    }

//    class GreetResponse {
//        private final String greet;
//
//        GreetResponse(String greet) {
//            this.greet = greet;
//        }
//
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            GreetResponse that = (GreetResponse) o;
//            return Objects.equals(greet, that.greet);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(greet);
//        }
//    }
}
