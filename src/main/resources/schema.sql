CREATE TABLE go_train_schedule (
    id INT PRIMARY KEY,
    line VARCHAR(250) NOT NULL,
    departure INT NOT NULL,
    arrival INT NOT NULL
);
