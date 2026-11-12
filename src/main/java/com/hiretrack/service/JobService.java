package com.hiretrack.service;
import com.hiretrack.model.Job; import com.hiretrack.repo.JobRepository; import org.springframework.stereotype.Service; import java.util.List;
@Service public class JobService { private final JobRepository jobs; public JobService(JobRepository j){jobs=j;} public List<Job> search(String q){return q==null?jobs.findAll():jobs.findAll().stream().filter(x->(x.title+" "+x.description).toLowerCase().contains(q.toLowerCase())).toList();} }
