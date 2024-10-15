
CREATE TABLE creatures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(100) NOT NULL,
    description TEXT,
    endangered BOOLEAN DEFAULT FALSE
);


CREATE TABLE donations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    donor_name VARCHAR(100) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    email VARCHAR(100) NOT NULL,
    donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE feedback (
    id INT AUTO_INCREMENT PRIMARY KEY,
    donor_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    feedback_text VARCHAR(400) NOT NULL,
    feedback_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO creatures (name, species, description, endangered) VALUES
('African Elephant', 'Elephantidae', 'Largest land mammal in the world', TRUE),
('Bald Eagle', 'Haliaeetus leucocephalus', 'National bird and symbol of the USA', FALSE),
('Giant Panda', 'Ailuropoda melanoleuca', 'Known for its black-and-white coat and love for bamboo', TRUE),
('Snow Leopard', 'Panthera uncia', 'Rare, mountain-dwelling big cat', TRUE),
('Komodo Dragon', 'Varanus komodoensis', 'The largest living species of lizard', TRUE),
('Golden Eagle', 'Aquila chrysaetos', 'One of the best-known birds of prey in the Northern Hemisphere', FALSE),
('Red Panda', 'Ailurus fulgens', 'Small, arboreal mammal with reddish-brown fur', TRUE),
('Blue Whale', 'Balaenoptera musculus', 'The largest animal ever known to have existed', TRUE),
('Siberian Tiger', 'Panthera tigris altaica', 'The largest tiger subspecies, native to Siberia', TRUE),
('Galápagos Tortoise', 'Chelonoidis nigra', 'Known for its long lifespan and large size', TRUE);


-- GET REQUESTS: 
-- 1. get all the creatures. 
SELECT * FROM creatures;

-- 2. get all endangered species
SELECT * FROM creatures WHERE endangered = TRUE;

-- 3. get all donors and info about their donations
SELECT 
    d.donor_name, 
    d.amount, 
    d.email, 
    d.donation_date 
FROM donations d;

-- 4. total amount of donations collected. 
SELECT 
    SUM(amount) AS total_donations 
FROM donations;

-- 5. No. of donors
SELECT 
    COUNT(DISTINCT donor_name) AS number_of_donors 
FROM donations;

-- 6. top donor details 
SELECT 
    donor_name, 
    amount, 
    email, 
    donation_date 
FROM donations 
ORDER BY amount DESC 
LIMIT 1;

select * from donations;
-- POST REQUESTS
-- insert a donation
INSERT INTO donations(donor_name, amount, email) VALUES ('Muniyamma', '4200', 'enkannulamayyi@gmail.com');

-- insert a feedback
INSERT INTO feedback (donor_name, email, feedback_text, feedback_date) VALUES 
('Alice Maami', 'alice@example.com', 'Great initiative! Keep up the good work!', '2023-09-15 11:00:00');