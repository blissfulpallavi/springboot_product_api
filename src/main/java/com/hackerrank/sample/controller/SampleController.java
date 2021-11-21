package com.hackerrank.sample.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.dto.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SampleController {

    final String uri = "https://jsonmock.hackerrank.com/api/inventory";
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject(uri, String.class);
    JSONObject root = new JSONObject(result);

    JSONArray data = root.getJSONArray("data");

    @CrossOrigin
    @GetMapping("/filter/discount/{discount_percentage}")
    private ResponseEntity<List<Product>> filteredBooksByDiscount(@PathVariable("discount_percentage") int discountPercentage) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(data.toString(), new
                    TypeReference<List<Product>>() {
                    });

            List<Product> filteredProducts = products.stream().filter(product -> product.getDiscount() > discountPercentage).collect(Collectors.toList());

            System.out.println(filteredProducts);

            return new ResponseEntity<List<Product>>(filteredProducts, HttpStatus.OK);
        } catch (Exception E) {
            System.out.println("Error encountered : " + E.getMessage());
            return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @GetMapping("/filter/price/{initial_price}/{final_price}")
    private ResponseEntity<List<Product>> filteredBooksByPriceRange(@PathVariable("initial_price") int init_price, @PathVariable("final_price") int final_price) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(data.toString(), new
                    TypeReference<List<Product>>() {
                    });

            List<Product> filteredProducts = products.stream().filter(product -> product.getPrice() > init_price &&
                    product.getPrice() < final_price).collect(Collectors.toList());

            System.out.println(filteredProducts);

            return new ResponseEntity<List<Product>>(filteredProducts, HttpStatus.OK);
        } catch (Exception E) {
            System.out.println("Error encountered : " + E.getMessage());
            return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping("/sort/price")
    private ResponseEntity<List<Product>> getSortedBooksByPrice() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            List<Product> products = mapper.readValue(data.toString(), new
                    TypeReference<List<Product>>() {
                    });

            List<Product> sortedList = products.stream()
                    .sorted(Comparator.comparingDouble(Product::getPrice))
                    .collect(Collectors.toList());

            return new ResponseEntity<List<Product>>(sortedList, HttpStatus.OK);

        } catch (Exception E) {
            System.out.println("Error encountered : " + E.getMessage());
            return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
        }
    }
}
