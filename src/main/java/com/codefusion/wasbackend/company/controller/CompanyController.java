package com.codefusion.wasbackend.company.controller;

import com.codefusion.wasbackend.company.dto.CompanyEntityDto;
import com.codefusion.wasbackend.company.dto.CreateCompanyDto;
import com.codefusion.wasbackend.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/getall")
    public ResponseEntity<List<CompanyEntityDto>> getAllCompanies() {
        List<CompanyEntityDto> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CompanyEntityDto> getCompanyById(@PathVariable Long id) {
        CompanyEntityDto company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }


    @PostMapping(value = "/addCompany", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateCompanyDto> addTransaction(@ModelAttribute CreateCompanyDto companyEntityDto,
                                                           @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        return new ResponseEntity<>(companyService.addCompany(companyEntityDto, file), HttpStatus.CREATED);
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyEntityDto> updateCompany(@PathVariable("id") Long id,
                                                          @ModelAttribute CompanyEntityDto companyEntityDto,
                                                          @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        return new ResponseEntity<>(companyService.updateCompany(id, companyEntityDto, file), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) throws IOException {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
