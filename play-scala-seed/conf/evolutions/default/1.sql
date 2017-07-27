
# User schema

# --- !Ups
create table `users` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
)

# --- !Downs
drop table `users`