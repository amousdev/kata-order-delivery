INSERT INTO services (id, store_id, delivery_method, enable) VALUES (1, 1, 'DRIVE', true);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (2, 1, 'DELIVERY', true);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (3, 1, 'DELIVERY_TODAY', true);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (4, 1, 'DELIVERY_ASAP', true);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (5, 2, 'DRIVE', true);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (6, 2, 'DELIVERY', true);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (7, 2, 'DELIVERY_TODAY', false);
INSERT INTO services (id, store_id, delivery_method, enable) VALUES (8, 2, 'DELIVERY_ASAP', true);


INSERT INTO SLOTS (ID, SERVICE_ID, DAY_CAPACITY, USED_CAPACITY, BEGIN_DATE, END_DATE) VALUES (1, 1, 10, 5, '2024-06-16T14:00:00.000Z', '2024-06-16T16:00:00.000Z');
INSERT INTO SLOTS (ID, SERVICE_ID, DAY_CAPACITY, USED_CAPACITY, BEGIN_DATE, END_DATE) VALUES (2, 2, 10, 3, '2024-06-16T16:00:00.000Z', '2024-06-16T18:00:00.000Z');
INSERT INTO SLOTS (ID, SERVICE_ID, DAY_CAPACITY, USED_CAPACITY, BEGIN_DATE, END_DATE) VALUES (3, 3, 10, 10, '2024-06-16T18:00:00.000Z', '2024-06-16T20:00:00.000Z');
INSERT INTO SLOTS (ID, SERVICE_ID, DAY_CAPACITY, USED_CAPACITY, BEGIN_DATE, END_DATE) VALUES (4, 4, 5, 5, '2024-06-16T14:00:00.000Z', '2024-06-16T16:00:00.000Z');
INSERT INTO SLOTS (ID, SERVICE_ID, DAY_CAPACITY, USED_CAPACITY, BEGIN_DATE, END_DATE) VALUES (5, 5, 10, 0, '2024-06-16T16:00:00.000Z', '2024-06-16T18:00:00.000Z');
INSERT INTO SLOTS (ID, SERVICE_ID, DAY_CAPACITY, USED_CAPACITY, BEGIN_DATE, END_DATE) VALUES (6, 5, 10, 0, '2024-06-18T14:00:00.000Z', '2024-06-18T16:00:00.000Z');