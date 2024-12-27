INSERT INTO categories(name)
VALUES ('Movies'),
       ('Music'),
       ('Gaming'),
       ('Sports'),
       ('Travel and Events'),
       ('Lifestyle'),
       ('Science and Technology'),
       ('Food and Cooking');

INSERT INTO channels(name, description, author_id, creation_date, language, category_id)
VALUES ('Tech Explorers',
        'A channel dedicated to exploring the latest in technology and gadgets.',
        2,
        '2024-10-11',
        'English',
        7);