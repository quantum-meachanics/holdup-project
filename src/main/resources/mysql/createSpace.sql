-- holdupdb 데이터베이스 사용
USE holdupdb;

-- 15개의 더미 데이터 삽입 (생성일시를 오름차순으로 설정)
INSERT INTO space (name, address, detail_address, description, width, height, depth, count, price, is_hide, gu, dong,
                   member_id, create_date)
VALUES ('test1', '서울시 강남구 역삼동', '역삼로 101', '강남구의 모던한 공간입니다.', 10, 3, 5, 25, FLOOR(RAND() * 900) + 100, FALSE, '강남구',
        '역삼동', 1, '2024-09-01 10:00:00'),
       ('test2', '서울시 마포구 서교동', '서교로 202', '마포구의 트렌디한 공간입니다.', 15, 4, 6, 30, FLOOR(RAND() * 900) + 100, FALSE, '마포구',
        '서교동', 1, '2024-09-02 11:00:00'),
       ('test3', '서울시 종로구 관철동', '종로로 303', '종로구의 역사적인 공간입니다.', 12, 3, 5, 35, FLOOR(RAND() * 900) + 100, FALSE, '종로구',
        '관철동', 1, '2024-09-03 12:00:00'),
       ('test4', '서울시 영등포구 당산동', '당산로 404', '영등포구의 편안한 공간입니다.', 18, 5, 7, 40, FLOOR(RAND() * 900) + 100, FALSE, '영등포구',
        '당산동', 1, '2024-09-04 13:00:00'),
       ('test5', '서울시 서초구 방배동', '방배로 505', '서초구의 고급스러운 공간입니다.', 14, 4, 6, 28, FLOOR(RAND() * 900) + 100, FALSE, '서초구',
        '방배동', 1, '2024-09-05 14:00:00'),
       ('test6', '서울시 동대문구 장안동', '장안로 606', '동대문구의 다양한 공간입니다.', 13, 3, 5, 32, FLOOR(RAND() * 900) + 100, FALSE, '동대문구',
        '장안동', 1, '2024-09-06 15:00:00'),
       ('test7', '서울시 서대문구 홍제동', '홍제로 707', '서대문구의 감각적인 공간입니다.', 15, 4, 6, 45, FLOOR(RAND() * 900) + 100, FALSE, '서대문구',
        '홍제동', 1, '2024-09-07 16:00:00'),
       ('test8', '서울시 강북구 수유동', '수유로 808', '강북구의 조용한 공간입니다.', 17, 5, 7, 50, FLOOR(RAND() * 900) + 100, FALSE, '강북구',
        '수유동', 1, '2024-09-08 17:00:00'),
       ('test9', '서울시 광진구 화양동', '화양로 909', '광진구의 현대적인 공간입니다.', 20, 6, 8, 60, FLOOR(RAND() * 900) + 100, FALSE, '광진구',
        '화양동', 1, '2024-09-09 18:00:00'),
       ('test10', '서울시 용산구 한남동', '한남로 1010', '용산구의 멋진 공간입니다.', 18, 5, 7, 55, FLOOR(RAND() * 900) + 100, FALSE, '용산구',
        '한남동', 1, '2024-09-10 19:00:00'),
       ('test11', '서울시 성동구 성수동', '성수로 1111', '성동구의 트렌디한 공간입니다.', 12, 3, 5, 35, FLOOR(RAND() * 900) + 100, FALSE, '성동구',
        '성수동', 1, '2024-09-11 20:00:00'),
       ('test12', '서울시 노원구 상계동', '상계로 1212', '노원구의 아늑한 공간입니다.', 14, 4, 6, 29, FLOOR(RAND() * 900) + 100, FALSE, '노원구',
        '상계동', 1, '2024-09-12 21:00:00'),
       ('test13', '서울시 서초구 서초동', '서초로 1313', '서초구의 세련된 공간입니다.', 15, 5, 7, 33, FLOOR(RAND() * 900) + 100, FALSE, '서초구',
        '서초동', 1, '2024-09-13 22:00:00'),
       ('test14', '서울시 동작구 사당동', '사당로 1414', '동작구에 위치한 넓은 공간입니다.', 20, 6, 8, 42, FLOOR(RAND() * 900) + 100, FALSE,
        '동작구', '사당동', 1, '2024-09-14 23:00:00'),
       ('test15', '서울시 송파구 잠실동', '잠실로 1515', '송파구의 화려한 공간입니다.', 22, 7, 9, 50, FLOOR(RAND() * 900) + 100, FALSE, '송파구',
        '잠실동', 1, '2024-09-15 00:00:00');
