--test data for research sample view
insert into s_study(s_studyid, createdt, createby, moddt, modby)
values ('study-1', current_timestamp, 'test', current_timestamp, 'test'),
       ('study-2', current_timestamp, 'test', current_timestamp, 'test');
insert into s_studysite(s_studysiteid, sstudyid, createdt, createby, moddt, modby)
values ('site-1a', 'study-1', current_timestamp, 'test', current_timestamp, 'test'),
       ('site-1b', 'study-1', current_timestamp, 'test', current_timestamp, 'test'),
       ('site-2a', 'study-2', current_timestamp, 'test', current_timestamp, 'test');
INSERT INTO s_subject (s_subjectid, createdt, createby, moddt, modby, u_donorid, u_yearofbirth,
                       u_ageatcollection, u_ethnicity,
                       u_lungcancerdiagnosed, u_lungcancerstage, u_lungcancersubtype, u_smokinghistory,
                       u_numberofyearssmoked,
                       u_averagepacksperday, u_previouslytreated)
VALUES ('subject-1', current_timestamp, 'test', current_timestamp, 'test', 'donor1', '1980', 40, 'Asian', 'Y', 4,
        'NSCLC', 120, 30, 4, 'N'),
       ('subject-2', current_timestamp, 'test', current_timestamp, 'test', 'donor2', '1970', 50,
        'Black or African American', 'Y', 3, 'SCLC', 100, 40, 4, 'N'),
       ('subject-3', current_timestamp, 'test', current_timestamp, 'test', 'donor3', '1960', 60, 'Pacific Islander',
        'Y',
        4, 'NSCLC', 80, 20, 5, 'N'),
       ('subject-4', current_timestamp, 'test', current_timestamp, 'test', 'donor4', '1950', 70, 'White', 'Y', 4,
        'NSCLC', 120, 30, 2, 'N'),
       ('subject-5', current_timestamp, 'test', current_timestamp, 'test', 'donor5', '1940', 80, 'Native American', 'Y',
        4, 'SCLC', 240, 50, 5, 'N');
INSERT INTO s_participant
(s_participantid, sstudyid, studysiteid, subjectid, externalparticipantid, createdt, createby, moddt, modby)
VALUES ('participant-1a', 'study-1', 'site-1a', 'subject-1', 'external-1a', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('participant-1b', 'study-1', 'site-1b', 'subject-1', 'external-1b', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('participant-1c', 'study-2', 'site-2a', 'subject-1', 'external-1c', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('participant-2a', 'study-1', 'site-1a', 'subject-2', 'external-2a', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('participant-3a', 'study-1', 'site-1a', 'subject-3', 'external-3a', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('participant-4a', 'study-2', 'site-2a', 'subject-4', 'external-4a', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('participant-5a', 'study-2', 'site-1a', 'subject-5', 'external-5a', current_timestamp, 'test',
        current_timestamp, 'test');

INSERT INTO u_kit
(u_kitid, externalid, createdt, createby, moddt, modby)
VALUES ('kit-1', 'externalKit-1', current_timestamp, 'test', current_timestamp, 'test'),
       ('kit-2', 'externalKit-2', current_timestamp, 'test', current_timestamp, 'test');

INSERT INTO s_samplefamily
(s_samplefamilyid, sstudyid, studysiteid, subjectid, participantid, u_kitid, createdt, createby, moddt, modby,)
VALUES ('sf-1a', 'study-1', 'site-1a', 'subject-1', 'participant-1a', 'kit-1', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('sf-1b', 'study-1', 'site-1b', 'subject-1', 'participant-1b', 'kit-1', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('sf-1c', 'study-2', 'site-2a', 'subject-1', 'participant-1c', 'kit-2', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('sf-2a', 'study-1', 'site-1a', 'subject-2', 'participant-2a', 'kit-1', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('sf-3a', 'study-1', 'site-1a', 'subject-3', 'participant-3a', 'kit-1', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('sf-4a', 'study-2', 'site-2a', 'subject-4', 'participant-4a', 'kit-2', current_timestamp, 'test',
        current_timestamp, 'test'),
       ('sf-5a', 'study-2', 'site-2a', 'subject-5', 'participant-5a', 'kit-2', current_timestamp, 'test',
        current_timestamp, 'test');
INSERT INTO s_sample
(s_sampleid, sampletypeid, collectiondt, samplefamilyid, preptypeid, u_externalsampleid, u_qrcode, u_donorid, u_quality,
 u_hemolysisrating, createdt, createby, moddt, modby)
VALUES ('s-1a', 'Whole Blood', '2021-01-01', 'sf-1a', 'DNA Prep', 'ext-s1a', 'barcode-1a', 'donor1', 'Pass', '120',
        current_timestamp, 'test',
        current_timestamp, 'test'),
       ('s-1b', 'Whole Blood', '2021-02-01', 'sf-1b', 'DNA Prep', 'ext-s1b', 'barcode-1b', 'donor1', 'Pass', '100',
        current_timestamp, 'test',
        current_timestamp, 'test'),
       ('s-1c', 'Whole Blood', '2021-03-01', 'sf-1c', 'RNA Prep', 'ext-s1c', 'barcode-1c', 'donor1', 'Pass', null,
        current_timestamp, 'test',
        current_timestamp, 'test'),
       ('s-2a', 'Whole Blood', '2021-04-01', 'sf-2a', 'DNA Prep', 'ext-s2a', 'barcode-2a', 'donor2', 'Pass', '100',
        current_timestamp, 'test',
        current_timestamp, 'test'),
       ('s-3a', 'Whole Blood', '2021-05-01', 'sf-3a', 'DNA Prep', 'ext-s3a', 'barcode-3a', 'donor3', 'Pass', '90',
        current_timestamp, 'test',
        current_timestamp, 'test'),
       ('s-4a', 'Whole Blood', '2021-06-01', 'sf-4a', 'DNA Prep', 'ext-s4a', 'barcode-4a', 'donor4', 'Pass', '300',
        current_timestamp, 'test',
        current_timestamp, 'test'),
       ('s-5a', 'Whole Blood', '2021-07-01', 'sf-5a', 'DNA Prep', 'ext-s5a', 'barcode-5a', 'donor5', 'Pass', null,
        current_timestamp, 'test',
        current_timestamp, 'test');
--end test data for research sample view