DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id) VALUES
('2020-06-24 08:30','Завтрак', 1000, 100000),
('2020-06-24 13:22', 'Обед', 500, 100000),
('2020-06-24 22:02', 'Ужин', 800, 100000),
('2020-06-23 19:20', 'Суп', 700, 100001),
('2020-06-22 12:33', 'Торт', 1200, 100001),
('2020-06-20 18:50', 'Печенье', 300, 100001),
('2020-06-21 13:30', 'Пирог', 900, 100001);

