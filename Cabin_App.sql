drop database if exists Cabin_App;

create database Cabin_App;

use Cabin_App;

create table `users`(
`user_id` bigint primary key auto_increment,
`username` varchar(30),
`email` varchar(75),
`password` varchar(100),
`first_name` varchar(25),
`last_name` varchar(50),
`phone_number` varchar(15),
`enabled` boolean DEFAULT 1,
`provider` varchar(15),
`reset_password_token` varchar(30)
);

create table `contact` (
	`id` bigint primary key auto_increment,
	`name` varchar(75),
    `phone` varchar(15),
    `email` varchar(100),
    `comments` varchar(255),
    `copy-email` boolean
);

create table `roles` (
	`role_id` bigint primary key auto_increment,
    `name` varchar(45) NOT NULL
);

create table `score_form`(
`id` bigint primary key auto_increment,
`name_one` varchar(50),
`name_two` varchar(50),
`name_three` varchar(50),
`name_four` varchar(50),
`name_five` varchar(50),
`name_six` varchar(50),
`activity_name` varchar(100),
`completion_date` date,
`completion_time` int,
`help_cards` int,
`stars` int,
`user_id` bigint not null,
foreign key (`user_id`) references `users`(`user_id`)
);

create table `users_roles` (
	`user_id` bigint not null,
    `role_id` bigint not null,
  KEY `user_fk_idx` (`user_id`),
  KEY `role_fk_idx` (`role_id`),
  CONSTRAINT `role_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

insert into `users`(`username`,`password`,`enabled`, first_name, last_name, email, phone_number)	VALUES
					("admin", "$2a$10$Vy90.sVNbgPNuuocZ3TEgu35CKYdkv8xmYl294ge7pd9PWSRT3qnG", 1,'Chris', 'Ference', 'cference8@gmail.com', '952-826-9152'),
					("circlepines", "$2a$10$C379XVnNCJ9YuxP/bniu.u12e37cYRKC47nxfa7C6qsQvXC7C93vu", 1, 'Cabin', 'House', 'cabin.escape.room.web.app@gmail.com', '952-270-9353');

insert into `roles` (`name`) VALUES
				('ROLE_ADMIN'),
                ('ROLE_USER');
                
insert into `users_roles` (`user_id`, `role_id`) VALUES
						(1,1),
                        (1,2),
                        (2,2);

insert into `score_form` (`name_one`, `name_two`, `name_three`, 
						`name_four`, `name_five`, `name_six`, 
                        `activity_name`, `completion_date`, `completion_time`, `help_cards`, `stars`, `user_id`) values
                        ('Chris','Ellen','Becky','','','','Adandon Cabin', '2022-11-12', 136, 3, 2, 2);

use cabin_app;
select * from Users WHERE Users.email = "cference8@gmail.com";
select * from users;

