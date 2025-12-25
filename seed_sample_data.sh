#!/bin/bash

# Database connection details
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="expense_splitter"
DB_USER="expense_user"
DB_PASSWORD="115417946"

# Export password to avoid prompt
export PGPASSWORD=$DB_PASSWORD

echo "Starting to populate fake data..."

# Clear existing data and reset sequences
echo "Clearing existing data and resetting sequences..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME << EOF
TRUNCATE TABLE expense_participants CASCADE;
TRUNCATE TABLE expenses RESTART IDENTITY CASCADE;
TRUNCATE TABLE people RESTART IDENTITY CASCADE;
EOF

# Insert People
echo "Inserting people..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME << EOF
INSERT INTO people (name) VALUES 
('Alice Johnson'),
('Bob Smith'),
('Charlie Brown'),
('Diana Prince'),
('Eve Davis'),
('Frank Miller'),
('Grace Lee'),
('Henry Wilson');
EOF

# Insert Expenses
echo "Inserting expenses..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME << EOF
INSERT INTO expenses (description, amount, paid_by_person_id) VALUES 
('Dinner at Italian Restaurant', 120.50, 1),
('Movie Tickets', 45.00, 2),
('Grocery Shopping', 89.99, 3),
('Uber to Airport', 35.75, 1),
('Coffee Shop', 28.40, 4),
('Concert Tickets', 180.00, 5),
('Pizza Night', 52.30, 2),
('Gas for Road Trip', 65.00, 3),
('Birthday Gift', 75.25, 6),
('Hotel Booking', 250.00, 1),
('Breakfast Brunch', 95.60, 7),
('Office Supplies', 42.15, 8),
('Team Lunch', 156.80, 4),
('Netflix Subscription', 15.99, 5),
('Gym Membership', 49.99, 6);
EOF

# Insert Expense Participants
echo "Inserting expense participants..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME << EOF
-- Expense 1: Dinner (Alice paid, Alice, Bob, Charlie participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(1, 1), (1, 2), (1, 3);

-- Expense 2: Movie (Bob paid, Bob, Diana participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(2, 2), (2, 4);

-- Expense 3: Grocery (Charlie paid, Charlie, Alice, Eve participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(3, 3), (3, 1), (3, 5);

-- Expense 4: Uber (Alice paid, Alice, Bob participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(4, 1), (4, 2);

-- Expense 5: Coffee (Diana paid, Diana, Frank, Grace participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(5, 4), (5, 6), (5, 7);

-- Expense 6: Concert (Eve paid, Eve, Frank, Diana, Henry participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(6, 5), (6, 6), (6, 4), (6, 8);

-- Expense 7: Pizza (Bob paid, Bob, Charlie, Alice participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(7, 2), (7, 3), (7, 1);

-- Expense 8: Gas (Charlie paid, Charlie, Bob, Alice, Diana participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(8, 3), (8, 2), (8, 1), (8, 4);

-- Expense 9: Birthday Gift (Frank paid, Frank, Grace participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(9, 6), (9, 7);

-- Expense 10: Hotel (Alice paid, Alice, Bob, Charlie, Diana participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(10, 1), (10, 2), (10, 3), (10, 4);

-- Expense 11: Brunch (Grace paid, Grace, Henry, Frank participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(11, 7), (11, 8), (11, 6);

-- Expense 12: Office Supplies (Henry paid, Henry participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(12, 8);

-- Expense 13: Team Lunch (Diana paid, all participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(13, 1), (13, 2), (13, 3), (13, 4), (13, 5), (13, 6), (13, 7), (13, 8);

-- Expense 14: Netflix (Eve paid, Eve, Diana participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(14, 5), (14, 4);

-- Expense 15: Gym (Frank paid, Frank, Henry participated)
INSERT INTO expense_participants (expense_id, person_id) VALUES 
(15, 6), (15, 8);
EOF

echo "Verifying data..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME << EOF
SELECT 'People count:' as info, COUNT(*) as count FROM people
UNION ALL
SELECT 'Expenses count:', COUNT(*) FROM expenses
UNION ALL
SELECT 'Participants count:', COUNT(*) FROM expense_participants;
EOF

echo "Done! Fake data has been populated successfully."

# Unset password
unset PGPASSWORD