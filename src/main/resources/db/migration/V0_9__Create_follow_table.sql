do
$$
    begin
        if not exists(select from pg_type where typname = 'course_status') then
            create type "course_status" as enum ('LINKED', 'UNLINKED');
        end if;
    end
$$;


create table if not exists "follow"
(
    id                varchar
        constraint courses_pk primary key                 default uuid_generate_v4(),
    course_id    varchar    constraint course_id_fk references "course"(id),
    students_id   varchar    constraint students_id_fk references "user"(id),
    status        status_type  default 'LINKED'
);
