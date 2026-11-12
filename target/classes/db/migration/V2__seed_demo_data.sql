INSERT INTO companies (name, industry, location, website) VALUES
('Northstar Labs', 'Software', 'Boston, MA', 'https://northstarlabs.example'),
('Blue Harbor Logistics', 'Logistics', 'Seattle, WA', 'https://blueharbor.example'),
('Summit Health Group', 'Healthcare', 'Chicago, IL', 'https://summithealth.example');

INSERT INTO recruiters (company_id, name, email, phone) VALUES
(1, 'Alicia Moore', 'alicia.moore@northstarlabs.example', '+1-617-555-0101'),
(2, 'Marcus Chen', 'marcus.chen@blueharbor.example', '+1-206-555-0144'),
(3, 'Priya Nair', 'priya.nair@summithealth.example', '+1-312-555-0129');

INSERT INTO candidates (first_name, last_name, email, phone, city, skills) VALUES
('Lena', 'Patel', 'lena.patel@example.com', '+1-415-555-0107', 'San Francisco', 'Java, Spring Boot, PostgreSQL, AWS'),
('Owen', 'Griffin', 'owen.griffin@example.com', '+1-512-555-0153', 'Austin', 'Python, SQL, Data Analysis, Tableau'),
('Maya', 'Rodriguez', 'maya.rodriguez@example.com', '+1-646-555-0199', 'New York', 'React, Node.js, UX Research, Product Strategy');

INSERT INTO jobs (company_id, title, location, employment_type, salary_min, salary_max, description) VALUES
(1, 'Senior Java Engineer', 'Boston, MA', 'Full-time', 140000, 190000, 'Build and maintain Java microservices powering recruitment workflows.'),
(2, 'Operations Analyst', 'Seattle, WA', 'Hybrid', 85000, 115000, 'Support supply chain analytics and operational planning across regional hubs.'),
(3, 'Product Designer', 'Chicago, IL', 'Remote', 100000, 145000, 'Design patient-first hiring and candidate experiences for digital health teams.');

INSERT INTO applications (job_id, candidate_id, status, cover_letter) VALUES
(1, 1, 'interviewing', 'I am excited to bring my Java and Spring Boot experience to your next-generation hiring platform.'),
(2, 2, 'applied', 'I would love to support operational excellence and improve decision-making through analytics.'),
(3, 3, 'shortlisted', 'I enjoy creating accessible candidate journeys and mentoring teams on UX best practices.');
