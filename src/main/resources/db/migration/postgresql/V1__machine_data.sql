CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE machines (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  hostname VARCHAR(255),
  timeout INT,
  gpu_quantity INT,
  total_flops FLOAT,
  gpu_name VARCHAR(255),
  gpu_ram INT,
  gpu_max_current_temperature INT,
  cpu_name VARCHAR(255),
  earn_day FLOAT,
  error_description VARCHAR(255)
);