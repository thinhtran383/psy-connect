create table Certificates
(
    certificate_id    int primary key,
    user_id           int,
    certificate_name  varchar(255),
    certificate_image varchar(255),

    foreign key (user_id) references Users (user_id)
)