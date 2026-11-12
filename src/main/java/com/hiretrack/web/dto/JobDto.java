package com.hiretrack.web.dto;
public record JobDto(String title, String location, String employmentType, Integer salaryMin, Integer salaryMax, String description, Long companyId) {}
