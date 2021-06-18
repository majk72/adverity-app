insert into RAW_METRICS (DATASOURCE,CAMPAIGN,DAILY,CLICKS,IMPRESSIONS)
SELECT * FROM CSVREAD('${csv.data.temp.file}', null,'charset=UTF-8 escape=\" fieldDelimiter= fieldSeparator=,');
