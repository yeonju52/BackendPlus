package com.lion.demo.service;

import com.lion.demo.entity.Book;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
public class CsvFileReaderService {
    @Autowired private ResourceLoader resourceLoader;
    @Autowired private BookService bookService;

    public void csvFileToH2() {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/data/20241114_yes24_국내도서_새로나온_상품.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                int count = 0;
                for (CSVRecord record: csvParser) {
                    String title = record.get("title");
                    String author = record.get("author");
                    String company = record.get("company");
                    String _price = record.get("price");
                    int price = Integer.parseInt(_price);
                    String imageUrl = record.get("imageUrl");
                    String summary = record.get("summary");
                    Book book = Book.builder()
                            .title(title).author(author).company(company).price(price).imageUrl(imageUrl).summary(summary)
                            .build();
                    bookService.insertBook(book);

                    if (count ++ == 100)
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//                    Book book = new Book(null, title, author, company, price, imageUrl, summary); // bid에 Null이 안됨