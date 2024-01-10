
alter table `user` add column last_login_date_time timestamp null default null;

update `user` u join
(select max(created_date_time) as last_login, user_email from user_login_tracker ult
 where succeeded  =  true group by user_email) as t
 on (t.user_email = u.email)
set u.last_login_date_time = t.last_login
