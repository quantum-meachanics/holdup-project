drop user quantum;
CREATE DATABASE IF NOT EXISTS holdupdb;
CREATE USER IF NOT EXISTS quantum identified by 'quantum';
GRANT ALL PRIVILEGES ON holdupdb.* to 'quantum'@'%';