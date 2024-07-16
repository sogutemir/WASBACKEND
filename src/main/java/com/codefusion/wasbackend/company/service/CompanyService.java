package com.codefusion.wasbackend.company.service;

import com.codefusion.wasbackend.company.dto.CompanyEntityDto;
import com.codefusion.wasbackend.company.dto.CreateCompanyDto;
import com.codefusion.wasbackend.company.helper.CompanyHelper;
import com.codefusion.wasbackend.company.mapper.CompanyEntityMapper;
import com.codefusion.wasbackend.company.mapper.CreateCompanyMapper;
import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.company.repository.CompanyRepository;
import com.codefusion.wasbackend.resourceFile.dto.ResourceFileDTO;
import com.codefusion.wasbackend.resourceFile.service.ResourceFileService;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository repository;
    private final CompanyEntityMapper companyEntityMapper;
    private final CreateCompanyMapper createCompanyMapper;
    private final ResourceFileService resourceFileService;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository repository, CreateCompanyMapper createCompanyMapper, CompanyEntityMapper companyEntityMapper,
                          ResourceFileService resourceFileService, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.companyEntityMapper = companyEntityMapper;
        this.createCompanyMapper = createCompanyMapper;
        this.resourceFileService = resourceFileService;
    }

    public List<CompanyEntityDto> getAllCompanies() {
        List<CompanyEntity> companies = repository.findAllByIsDeletedFalse();
        return companies.stream()
                .map(this::convertToCompanyDTO)
                .toList();
    }

    public CompanyEntityDto getCompanyById(Long id) {
        CompanyEntity companyEntity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        return convertToCompanyDTO(companyEntity);
    }

    public CreateCompanyDto addCompany(CreateCompanyDto companyEntityDto, MultipartFile file) throws IOException {
        UserEntity userEntity = userRepository.findById(companyEntityDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + companyEntityDto.getUserId()));

        CompanyEntity companyEntity = createCompanyMapper.toEntity(companyEntityDto);
        companyEntity.setIsDeleted(false);
        companyEntity.setUser(userEntity);

        companyEntity = repository.save(companyEntity);
        userEntity.setCompany(companyEntity);

        resourceFileService.handleFile(companyEntity, file, ResourceFileService.ProcessType.ADD);
        userRepository.save(userEntity);

        return forCreateconvertToCompanyDTO(companyEntity);
    }

    public CompanyEntityDto updateCompany(Long id, CompanyEntityDto companyEntityDto, MultipartFile file) throws IOException {
        CompanyEntity companyEntity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        resourceFileService.handleFile(companyEntity, file, ResourceFileService.ProcessType.UPDATE);
        companyEntityMapper.partialUpdate(companyEntityDto, companyEntity);
        companyEntity = repository.save(companyEntity);
        return convertToCompanyDTO(companyEntity);
    }

    public void deleteCompany(Long id) throws IOException {
        CompanyEntity companyEntity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        resourceFileService.handleFile(companyEntity, null, ResourceFileService.ProcessType.DELETE);
        companyEntity.setIsDeleted(true);
        repository.save(companyEntity);
    }

    private CompanyEntityDto convertToCompanyDTO(CompanyEntity companyEntity) {
        CompanyEntityDto.ResourceFileEntityDto resourceFileDto = null;
        if (companyEntity.getResourceFile() != null) {
            try {
                ResourceFileDTO fileDTO = resourceFileService.downloadFile(companyEntity.getResourceFile().getId());
                resourceFileDto = CompanyHelper.mapResourceFile(fileDTO);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return CompanyHelper.convertToCompanyDTO(companyEntity, resourceFileDto);
    }

    private CreateCompanyDto forCreateconvertToCompanyDTO(CompanyEntity companyEntity) {
        CreateCompanyDto.ResourceFileEntityDto resourceFileDto = null;
        if (companyEntity.getResourceFile() != null) {
            try {
                ResourceFileDTO fileDTO = resourceFileService.downloadFile(companyEntity.getResourceFile().getId());
                resourceFileDto = CompanyHelper.forCreatermapResourceFile(fileDTO);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return CompanyHelper.forCreateconvertToCompanyDTO(companyEntity, resourceFileDto);
    }
}
