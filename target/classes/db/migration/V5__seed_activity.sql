INSERT INTO applications(job_id,candidate_id,status,cover_letter)
SELECT DISTINCT ON (j.id,c.id) j.id,c.id,CASE WHEN n%4=0 THEN 'interviewing' WHEN n%3=0 THEN 'shortlisted' ELSE 'applied' END,'Demo application'
FROM generate_series(1,35) n
JOIN jobs j ON j.id=(SELECT min(id) FROM jobs)+((n-1) % (SELECT count(*) FROM jobs))
JOIN candidates c ON c.id=(SELECT min(id) FROM candidates)+((n-1) % (SELECT count(*) FROM candidates))
WHERE NOT EXISTS (SELECT 1 FROM applications a WHERE a.job_id=j.id AND a.candidate_id=c.id);

INSERT INTO notifications(user_id,title,message)
SELECT u.id,'Welcome to HireTrack','Your HireTrack account is ready.'
FROM users u
WHERE NOT EXISTS (SELECT 1 FROM notifications n WHERE n.user_id=u.id);

INSERT INTO interviews(application_id,scheduled_at,mode,status,notes)
SELECT a.id,NOW()+((a.id % 14)||' days')::interval,'Video','scheduled','Demo interview'
FROM applications a
WHERE a.id % 4 = 0
AND NOT EXISTS (SELECT 1 FROM interviews i WHERE i.application_id=a.id);
