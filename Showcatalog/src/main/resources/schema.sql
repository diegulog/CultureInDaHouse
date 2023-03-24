INSERT INTO role (id, role_name)
VALUES (0, 'ADMIN_USER')
     , (1, 'BUSINESS_USER')
     , (2, 'STANDARD_USER');

INSERT INTO category (name, description)
VALUES ('Teatro', 'Obras de teatro, musicales, drama, comedia...')
     , ('Concierto', 'Conciertos de música clásica, pop, rock, etc.')
     , ('Danza', 'Representaciones de danza clásica, contemporánea...')
     , ('Ópera', 'Ópera')
     , ('Circo', 'Actuaciones de circo')
     , ('Monólogo', 'Monólogos, stand-up comedy, improvisación, etc.');

INSERT INTO show (name, id_category, description, price, duration, capacity, on_sale_date, status, image)
VALUES ('Cats', 1, 'Musical Cats', 20, 120, 300, '2022-05-01', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/CatsMusicalLogo.jpg?alt=media&token=d201ea7a-cf51-4e6a-a685-dc6e9fbe3748')

     , ('Macbeth', 1, 'MacBeth by Shakespeare', 30, 180, 350, '2022-06-15', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/The%20Tragedie%20of%20Macbeth.jpg?alt=media&token=76a38f06-96b8-4e2c-a368-1533c90b0f7f')

     , ('Concierto de año nuevo', 2, 'Concierto de música clásica con ocasión del año nuevo 2023', 15, 120, 250, '2022-11-01', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')

     , ('Las 4 estaciones', 2, 'Las 4 estaciones de Vivaldi', 17, 90, 200, '2022-11-01', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')

     , ('El lago de los cisnes', 3, 'Interpretado por la compañía de danza de San Petersburgo', 40, 180, 325, '2022-12-05', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/Wilfride_Piollet_danse_dans__le_Lac_des_Cygnes__(%C3%A0_l''Op%C3%A9ra_de_Paris%2C_1977).jpg?alt=media&token=80cab3b8-753f-4326-90f5-3a9b52f7deae')

     , ('El cascanueces', 3, 'Interpretado por la compañía de danza de Sebastopol', 38, 120, 300, '2022-09-18', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')

     , ('Così fan tutte', 4, 'Ciclo Mozart en el Palau de la Musica', 24, 110, 225, '2023-01-12', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/Cosi_fan_tutte_-_first_performance.jpg?alt=media&token=2b75baac-8c16-456d-b7d4-8a7611f21331')

     , ('Rigoletto', 4, 'Verdi al Liceu', 42, 100, 150, '2022-08-01', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')

     , ('Circo Raluy', 5, 'El Circo Raluy visita Barcelona', 10, 120, 400, '2022-06-01', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')
     , ('Circo Mundial', 5, 'Gira de verano del Circo Mundial - Ahora SIN leones!', 12, 100, 300, '2022-08-10', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')

     , ('Vermunólogos', 6, 'Diferentes artistas cada día!', 5, 45, 100, '2022-06-15', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')
     , ('Tinder sorpresa', 6, 'El nuevo espectáculo de Toni Moog', 15, 90, 200, '2022-07-20', 0,
         'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')
     , ('El club de la comedia', 6, 'Actuación en vivo del aclamado programa de humor', 20, 90, 300, '2022-10-05', 0,
        'https://firebasestorage.googleapis.com/v0/b/cultureindahouse-group4.appspot.com/o/No_Image_Available.jpg?alt=media&token=fd5f7410-689c-467b-859f-339c638bc097')
;


INSERT INTO performance (id_show, date, time, streaming_url, remaining_seats, status)
values (1, '2022-05-15', '21:30', 'http://foo.bar', 300, 0)
     , (1, '2022-05-18', '21:30', 'http://foo.bar', 300, 0)
     , (2, '2022-07-25', '22:00', 'http://foo.bar', 350, 0)
;

INSERT INTO users (active, create_time, first_name, last_name, password, username)
values (false, '2022-12-12 00:00:00.000', 'admin1', 'mylastname', '$2a$12$J5mFs8D720HU2AdnfQggFe58rtjjvXSUo5pFSwwagh9KAfukt5nrG', 'admin1@fakemail.com')
     , (false, '2022-12-12 00:00:00.000', 'business1', 'mylastname1', '$2a$12$J5mFs8D720HU2AdnfQggFe58rtjjvXSUo5pFSwwagh9KAfukt5nrG', 'business1@fakemail.com')
     , (false, '2022-12-12 00:00:00.000', 'business2', 'mylastname2', '$2a$12$J5mFs8D720HU2AdnfQggFe58rtjjvXSUo5pFSwwagh9KAfukt5nrG', 'business2@fakemail.com')
     , (false, '2022-12-12 00:00:00.000', 'business3', 'mylastname3', '$2a$12$J5mFs8D720HU2AdnfQggFe58rtjjvXSUo5pFSwwagh9KAfukt5nrG', 'business3@fakemail.com')
     , (false, '2022-12-12 00:00:00.000', 'user1', 'mylastname2', '$2a$12$J5mFs8D720HU2AdnfQggFe58rtjjvXSUo5pFSwwagh9KAfukt5nrG', 'user1@fakemail.com')
     , (false, '2022-12-12 00:00:00.000', 'user2', 'mylastname2', '$2a$12$J5mFs8D720HU2AdnfQggFe58rtjjvXSUo5pFSwwagh9KAfukt5nrG', 'user2@fakemail.com')
;
INSERT INTO user_role (user_id, role_id)
values (1, 0), (2, 1), (3, 2), (4, 2), (5, 2), (6, 2)
;

INSERT INTO company (name, description, image)
VALUES ('Teatro Nacional de Cataluña', 'Teatro Nacional de Cataluña TNC', 'https://upload.wikimedia.org/wikipedia/commons/b/b0/Teatre_Nacional_de_Catalunya%2C_Bofill.JPG')
     , ('Gran Teatre del Liceu', 'El Gran Teatro del Liceo de Barcelona, conocido como El Liceo (Gran Teatre del Liceu o El Liceu en catalán), es el teatro en activo más antiguo y prestigioso de Barcelona', 'https://upload.wikimedia.org/wikipedia/commons/7/7a/Liceu_-_Interior.jpg');

INSERT INTO company_user (company_id, user_id)
values (1, 2);
