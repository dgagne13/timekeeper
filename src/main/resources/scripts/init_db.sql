CREATE TABLE department
            (
                id IDENTITY,
                name VARCHAR(50) NOT NULL,
                annual_budget DECIMAL(19,4),
                last_modified TIMESTAMP,
                modified_by VARCHAR(20),
                PRIMARY KEY(id)
            );
    CREATE TABLE position
    (
        id IDENTITY,
        title VARCHAR(25) NOT NULL,
        description VARCHAR(100),
        max_hourly_rate DECIMAL(19,4),
        min_hourly_rate DECIMAL(19,4),
        last_modified TIMESTAMP,
        modified_by VARCHAR(20),
        PRIMARY KEY(id)
    );
    CREATE TABLE employee
    (
        id IDENTITY,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        hire_date DATE NOT NULL,
        hourly_rate DECIMAL(19,4) NOT NULL,
        vacation_hours DECIMAL(19,4) NOT NULL,
        sick_hours DECIMAL(19,4) NOT NULL,
        position_id INT NOT NULL,
        department_id INT NOT NULL,
        employment_status_id INT NOT NULL,
        last_modified TIMESTAMP,
        modified_by VARCHAR(20),
        PRIMARY KEY(id)
    );
    CREATE TABLE employment_status
    (
        id IDENTITY,
        status VARCHAR(25) NOT NULL,
        payable BOOLEAN NOT NULL,
        accrues_time BOOLEAN NOT NULL,
        last_modified TIMESTAMP,
        modified_by VARCHAR(20),
        PRIMARY KEY(id)
    );
    CREATE TABLE shift_type
    (
        id IDENTITY,
        value VARCHAR(50) NOT NULL,
        last_modified TIMESTAMP,
        modified_by VARCHAR(20),
        PRIMARY KEY(id)
    );
    CREATE TABLE shift
    (
        id IDENTITY,
        employee_id INT NOT NULL,
        shift_start DATETIME,
        shift_end DATETIME,
        lunch_start DATETIME,
        lunch_end DATETIME,
        duration DECIMAL(19,4),
        shift_type_id INT NOT NULL,
        last_modified TIMESTAMP,
        modified_by VARCHAR(20),
        PRIMARY KEY(id)
    );
    ALTER TABLE employee ADD CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES department(id);
    ALTER TABLE employee ADD CONSTRAINT fk_employee_position FOREIGN KEY (position_id) REFERENCES position(id);
    ALTER TABLE employee ADD CONSTRAINT fk_employee_employment_status FOREIGN KEY (employment_status_id) REFERENCES employment_status(id);
    ALTER TABLE shift ADD CONSTRAINT fk_shift_employee FOREIGN KEY (employee_id) REFERENCES employee(id);
    ALTER TABLE shift ADD CONSTRAINT fk_shift_shift_type FOREIGN KEY (shift_type_id) REFERENCES shift_type(id);

    INSERT INTO shift_type(value, last_modified, modified_by) VALUES ('Regular', CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO shift_type(value, last_modified, modified_by) VALUES ('Sick', CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO shift_type(value, last_modified, modified_by) VALUES ('Vacation', CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO shift_type(value, last_modified, modified_by) VALUES ('UnpaidLeave', CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO shift_type(value, last_modified, modified_by) VALUES ('Holiday', CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employment_status(status, payable, accrues_time, last_modified, modified_by) VALUES ('Active', true, true, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employment_status(status, payable, accrues_time, last_modified, modified_by) VALUES ('Terminated', false, false, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employment_status(status, payable, accrues_time, last_modified, modified_by) VALUES ('Retired', false, false, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employment_status(status, payable, accrues_time, last_modified, modified_by) VALUES ('Sabattical', true, false, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employment_status(status, payable, accrues_time, last_modified, modified_by) VALUES ('FMLA', false, false, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO department(name, annual_budget, last_modified, modified_by) VALUES ('IT', 300000.00, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO department(name, annual_budget, last_modified, modified_by) VALUES ('Accounting', 400000.00, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO department(name, annual_budget, last_modified, modified_by) VALUES ('Complaints', 500000.00, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO department(name, annual_budget, last_modified, modified_by) VALUES ('Human Resources', 100000.00, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO position(title, description, max_hourly_rate, min_hourly_rate, last_modified, modified_by) VALUES ('Human Resources Manager', 'Manages human resources', 30.0000, 20.0000, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO position(title, description, max_hourly_rate, min_hourly_rate, last_modified, modified_by) VALUES ('IT Manager', 'Herds cats', 50.0000, 30.0000, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO position(title, description, max_hourly_rate, min_hourly_rate, last_modified, modified_by) VALUES ('Accounting Manager', 'Manages the number crunchers', 45.0000, 28.0000, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO position(title, description, max_hourly_rate, min_hourly_rate, last_modified, modified_by) VALUES ('Human Resources Associate', 'Handles payroll', 25.0000, 18.0000, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO position(title, description, max_hourly_rate, min_hourly_rate, last_modified, modified_by) VALUES ('IT Associate', 'A little of everything', 45.0000, 25.0000, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO position(title, description, max_hourly_rate, min_hourly_rate, last_modified, modified_by) VALUES ('Accounting Associate', 'Cunches Numbers', 40.0000, 23.0000, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employee(first_name, last_name, hire_date, hourly_rate, vacation_hours, sick_hours, position_id, department_id, employment_status_id, last_modified, modified_by) VALUES ('Herb', 'Stempel', '2010-10-10', 15.0000, 40.0000, 20.0000, 3, 2, 1, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employee(first_name, last_name, hire_date, hourly_rate, vacation_hours, sick_hours, position_id, department_id, employment_status_id, last_modified, modified_by) VALUES ('Larry', 'Page', '2012-06-13', 30.0000, 10.0000, 5.0000, 2, 1, 1, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employee(first_name, last_name, hire_date, hourly_rate, vacation_hours, sick_hours, position_id, department_id, employment_status_id, last_modified, modified_by) VALUES ('Randy', 'Savage', '2014-08-08', 20.0000, 50.0000, 10.0000, 1, 4, 1, CURRENT_TIMESTAMP(), 'dgagne');
    INSERT INTO employee(first_name, last_name, hire_date, hourly_rate, vacation_hours, sick_hours, position_id, department_id, employment_status_id, last_modified, modified_by) VALUES ('Donny', 'Trump', '2015-08-08', 20.0000, 50.0000, 10.0000, 6, 2, 2, CURRENT_TIMESTAMP(), 'dgagne');