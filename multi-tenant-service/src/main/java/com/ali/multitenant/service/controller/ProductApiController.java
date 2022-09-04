package com.ali.multitenant.service.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ali.multitenant.service.domain.entity.Employee;
import com.ali.multitenant.service.services.EmployeeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class ProductApiController {

    private final EmployeeService employeeService;

    @GetMapping(value = "/employees")
    public ResponseEntity<List<Employee>> getProducts() {
        return ResponseEntity.ok().body(employeeService.getEmployees());
    }

    @GetMapping(value = "/employees/{email}")
    public ResponseEntity<Employee> getProduct(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(employeeService.getEmployee(email));
    }

    @PostMapping(value = "/employees")
    public ResponseEntity<Employee> createProduct(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok().body(employeeService.createEmployee(employee)) ;
    }

    @PutMapping(value = "/employees/{email}")
    ResponseEntity<Employee> updateProduct(@PathVariable String email, @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(employee, email);
        return ResponseEntity.ok().body(updatedEmployee);
    }

    @DeleteMapping("/employees/{email}")
    ResponseEntity<Void> deleteProduct(@PathVariable String email) {
        employeeService.deleteEmployeeById(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @GetMapping(value = "/async/products", produces = {ContentType.PRODUCTS_1_0})
    // public CompletableFuture<ResponseEntity<List<ProductValue>>> asyncGetProducts() {
    //     List<ProductValue> productValues = productService.getProducts();
    //     return CompletableFuture.completedFuture(new ResponseEntity<>(productValues, HttpStatus.OK));
    // }

    // @GetMapping(value = "/async/products/{productId}", produces = {ContentType.PRODUCT_1_0})
    // public CompletableFuture<ResponseEntity<ProductValue>> asyncGetProduct(@PathVariable("productId") long productId) {
    //     return CompletableFuture.completedFuture(getProduct(productId));
    // }

}
