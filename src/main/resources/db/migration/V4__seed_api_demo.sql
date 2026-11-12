INSERT INTO companies(name, industry, location, website)
SELECT 'Demo Company ' || n, CASE WHEN n % 3 = 0 THEN 'Finance' WHEN n % 3 = 1 THEN 'Technology' ELSE 'Retail' END,
       'United States', 'https://demo' || n || '.example'
FROM generate_series(4,10) n
WHERE (SELECT count(*) FROM companies) < 10;

INSERT INTO candidates(first_name,last_name,email,city,skills)
SELECT 'Demo','Candidate ' || n,'candidate' || n || '@demo.hiretrack.local','Remote','Java, SQL, Communication'
FROM generate_series(4,15) n
WHERE NOT EXISTS (SELECT 1 FROM candidates c WHERE c.email = 'candidate' || n || '@demo.hiretrack.local');

INSERT INTO recruiters(company_id,name,email)
SELECT ((n - 1) % GREATEST((SELECT count(*) FROM companies),1)) + 1,'Demo Recruiter ' || n,'recruiter' || n || '@demo.hiretrack.local'
FROM generate_series(4,10) n
WHERE NOT EXISTS (SELECT 1 FROM recruiters r WHERE r.email = 'recruiter' || n || '@demo.hiretrack.local');

INSERT INTO jobs(company_id,title,location,employment_type,salary_min,salary_max,description)
SELECT ((n - 1) % GREATEST((SELECT count(*) FROM companies),1)) + 1,
       'Demo Engineering Role ' || n,'Remote','Full-time',80000 + n * 1000,120000 + n * 1000,'Join a growing HireTrack demo company.'
FROM generate_series(4,30) n
WHERE NOT EXISTS (SELECT 1 FROM jobs j WHERE j.title = 'Demo Engineering Role ' || n);

INSERT INTO users(email,password_hash,role,candidate_id)
SELECT 'candidate' || c.id || '@demo.hiretrack.local',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'CANDIDATE',c.id FROM candidates c
WHERE c.email LIKE 'candidate%@demo.hiretrack.local'
AND NOT EXISTS (SELECT 1 FROM users u WHERE u.email = 'candidate' || c.id || '@demo.hiretrack.local');
