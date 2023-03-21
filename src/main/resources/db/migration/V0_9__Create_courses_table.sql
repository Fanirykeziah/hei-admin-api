create table if not exists "course"
(
    id                varchar
        constraint course_pk primary key                 default uuid_generate_v4(),
    code              varchar                  not null,
    name              varchar                  not null,
    credits           int                      not null,
    total_hours       int                      not null,
    main_teacher      varchar constraint main_teacher_id_fk   references "user"(id)
);
