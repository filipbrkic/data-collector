## Data Collector Project

The Data Collector project is a Java and Spring Boot application that I created while learning the fundamentals of these technologies. It allows users to perform basic CRUD operations to manage machines and save them to a PostgreSQL database. Additionally, the project includes a cron job that runs every minute to collect data from the database. In case of errors that are randomly generated when making a machine, the system sends notifications to Slack. To prevent spam and redundant notifications, machines are temporarily stored in Redis for one hour.

## Features

- CRUD Operations

- Data Collection Cron Job

- Slack Notifications

- Redis Caching
