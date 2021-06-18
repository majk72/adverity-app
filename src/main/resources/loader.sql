insert into campaigns(id, name) 
	select nextval('campaigns_sq'), raw.campaign 
	from (select distinct campaign from raw_metrics) as raw;

insert into datasources(id, name) 
	select nextval('datasources_sq'), raw.datasource 
	from (select distinct datasource from raw_metrics) as raw;

insert into days(id, year, qtr, month, day_of_week, day_month, calendar_date)
	select nextval('days_sq'), raw.year, raw.quarter, raw.month, raw.day_of_week, raw.day_of_month, raw.dt 
	from (select extract(year from raw.dt) as year,
			extract(quarter from raw.dt) as quarter,
			extract(month from raw.dt) as month,
			extract(dow from raw.dt) as day_of_week,
			extract(day from raw.dt) as day_of_month,
			raw.dt
		 from (select distinct to_date(daily, 'MM/DD/YY') as dt from raw_metrics) as raw 
		 ) as raw;

insert into metrics(day_id, datasource_id, campaign_id, clicks, impressions)
	select t.id,d.id,c.id, m.clicks, m.impressions 
	from campaigns as c 
	cross join days as t 
	cross join datasources as d 	
	inner join raw_metrics as m 
		on c.name = m.campaign 
		and d.name = m.datasource 
		and t.calendar_date = to_date(daily, 'MM/DD/YY');

