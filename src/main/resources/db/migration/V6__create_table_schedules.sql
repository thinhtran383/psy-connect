create table Schedules
(
    schedule_id      int auto_increment
        primary key,
    doctor_id        int      not null, -- tham chiếu vào bảng Users cho bác sĩ
    patient_id       int      not null, -- tham chiếu vào bảng Users cho bệnh nhân
    appointment_date datetime not null,
    status           enum ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')
                    default 'PENDING'                   not null,
    created_at       timestamp default CURRENT_TIMESTAMP null,
    updated_at       timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    notes            text null,
    constraint schedules_ibfk_1
        foreign key (doctor_id) references psy.Users (user_id),
    constraint schedules_ibfk_2
        foreign key (patient_id) references psy.Users (user_id)
);

