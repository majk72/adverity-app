# adverity-app

Adverity Java Challenge API is Java based app with PostgreSQL db as backend. During startup it populates CSV data from url into database.

# Adverity Java Challenge API how to

	http://34.107.14.214/dw/metrics/select={coma separated selections}
	http://34.107.14.214/dw/metrics/select={coma separated selections}?filters

## selection parameters

### dimensions
	datasource,campaign,year,quarter,month,date,dayOfMonth,dayOfWeek (0-sun,1-mon,...)

### metrics
	clicks,impressions,ctr

### aggregates
	count,total_clicks,total_impressions,avg_clicks,avg_ipressions,avg_ctr

### filters
can be both dimensions or metrics

1. year,quarter,month,date follow the one or two parameter mapping as below
Param | Query
--- | ---
date=2019-01-01 | "date=2019-01-01"
date=2019-01-01&date=2019-02-01 | "date between 2019-01-01 and 2019-02-01"
quarter=1&quarter=3 | "quarter between 1 and 3"

2. campaign, datasource use only one with like operator
Param | Query
--- | ---
campaign=App* | "campaign like App%"
datasource=Tw* | "datasource like App%"

## Input Constraints
1. Must have either dimensions or metrics
2. When selecting metric a filter must be added (just to contstraint number of browsed data)
3. Result is grouped by dimensions if added
4. date format yyyy-MM-dd

## Output format

```
  {
    "headers":
       ["year","total_clicks"],
    "data":[
       [2019,2847007],
       [2020,35246]
      ]
  }
```

# Examples

## Metric browsing (no aggregates)
    curl "http://34.107.14.214/dw/metrics/select=datasource,campaign,clicks,impressions,ctr?date=2019-01-01&date=2019-02-01"
## Aggregates
    curl "http://34.107.14.214/dw/metrics/select=datasource,count,total_clicks,total_impressions,avg_impressions,avg_ctr?datasource=Tw*"
## Grouping by dimensions (year,quarter,month,date)
    curl "http://34.107.14.214/dw/metrics/select=year,quarter,count,total_clicks,total_impressions,avg_impressions,avg_ctr"

# Adverity-app building
Requirements : Java 11
From current directory (maven included)
    mvnw clean install

### Dockerization

Starting containers with PostgreSQL db and Spring Boot app (based on *Dockerfile* and composed by *docker-compose.yml*, both from current directory )

    docker compose build
    docker compose up
