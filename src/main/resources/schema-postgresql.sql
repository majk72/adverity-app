create unlogged table if not exists RAW_METRICS (
	DATASOURCE VARCHAR(128),
 	CAMPAIGN VARCHAR(128),
 	DAILY VARCHAR(12),
 	CLICKS INTEGER,
 	IMPRESSIONS INTEGER
);

truncate table RAW_METRICS;
