--liquibase formatted sql logicalFilePath:db/postgres/migration.sql
--changeset vasilev:initial-data
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from AUTHOR;
insert into AUTHOR (id, surname, name, patronymic, sex, country) values (nextval('author_seq'), 'Кузнецова', 'Мария', 'Александровна', 'ж', 'Россия');
insert into AUTHOR (id, surname, name, patronymic, sex, country) values (nextval('author_seq'), 'Кузнецова', 'Алина', 'Георгиевна', 'ж', 'Беларусь');
insert into AUTHOR (id, surname, name, patronymic, sex, country) values (nextval('author_seq'), 'Мелехин', 'Петр', 'Андреевич', 'м', 'Россия');

insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Распознавание речи', 'Обработка естественного языка');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Извлечение информации из текстов', 'Обработка естественного языка');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Распознавание образов', 'Интеллектуальный анализ данных');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Реактивные агенты', 'Многоагентные системы');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Планирование поведения агентов', 'Многоагентные системы');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Глубокие нейронные сети', 'Неросетевые технологии');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Адаптивное обучение', 'Неросетевые технологии');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Гибридные нейронные сети', 'Неросетевые технологии');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Экспертные системы', 'Инженерия знаний');
insert into AI_AREA (id, area_name, area_branch) values (nextval('ai_area_seq'), 'Извлечение знаний', 'Инженерия знаний');

insert into KEYWORD (id, value) values (nextval('keyword_seq'), 'Извлечение знаний');
insert into KEYWORD (id, value) values (nextval('keyword_seq'), 'Нейронные сети');
insert into KEYWORD (id, value) values (nextval('keyword_seq'), 'Глубокое обучение');

insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Сквозное обучение', 'Анализ данных');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Методы дескриминантного анализа', 'Анализ данных');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Семантический анализ', 'Анализ данных');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Скрытые марковские модели', 'Анализ данных');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Нейронные сети', 'Анализ данных');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Методы семантического анализа', 'Лингвистика');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Методы синтаксического анализа', 'Лингвистика');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Генетические алгоритмы', 'Биология');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Методы анализа коллективного поведение', 'Психология');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Алгоритмы Data-mining', 'Лингвистика');
insert into METHOD (id, method_name, science_branch) values (nextval('method_seq'), 'Выявление НЕ-факторов знаний', 'Лингвистика');

insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Глубокие нейросети');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Сверточные нейросети');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Машинное обучение');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Динамически реконфигурированное интеллектуальное управление');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Асинхронное управление');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Язык коммуникации объектов');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Язык представления знаний');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Распознавание по закрытым грамматикам');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Встроенные грамматики');
insert into TECHNOLOGY (id, technology_name) values (nextval('technology_seq'), 'Робототехника');

insert into SAI_PROPERTY (id, property_name) values (nextval('sai_property_seq'), 'Принятие решений');
insert into SAI_PROPERTY (id, property_name) values (nextval('sai_property_seq'), 'Представление знаний');
insert into SAI_PROPERTY (id, property_name) values (nextval('sai_property_seq'), 'Планирование');
insert into SAI_PROPERTY (id, property_name) values (nextval('sai_property_seq'), 'Обучение');
insert into SAI_PROPERTY (id, property_name) values (nextval('sai_property_seq'), 'Общение на ЕЯ');
insert into SAI_PROPERTY (id, property_name) values (nextval('sai_property_seq'), 'Прогнозирование');

insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Глубокая нейросеть');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Гибридная нейросеть');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Интеллектуальный робот');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Многоагентная система');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Динамическая экспертная ситсема');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Интегрированная экспертная ситсема');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Статическая экспертная ситсема');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Система распознавания речи');
insert into DEVELOPMENT_TYPE (id, type_name) values (nextval('development_type_seq'), 'Система обработки визуальной информации');
--comment initial-data
--rollback not required


