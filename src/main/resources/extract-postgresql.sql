copy raw_metrics(datasource, campaign, daily, clicks, impressions) FROM '${csv.data.temp.file}' DELIMITER ','  CSV HEADER;
