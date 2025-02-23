create table tasks (id bigserial primary key,
                    title varchar(50) not null,
                    description varchar(255),
                    user_id bigint)