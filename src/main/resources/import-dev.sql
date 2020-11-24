CREATE MATERIALIZED VIEW public.vg250_compound AS
SELECT
    row_number() OVER (ORDER BY ars) AS id,
    *
FROM (
     SELECT DISTINCT ON (ars) ars, gen, bez, geom, 'COUNTRY' AS regiontype FROM public.vg250_sta
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom, 'STATE' AS regiontype FROM public.vg250_lan
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom, 'COUNTY' AS regiontype FROM public.vg250_rbz
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom, 'DISTRICT' AS regiontype FROM public.vg250_krs
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom, 'MUNICIPALITY' AS regiontype FROM public.vg250_gem
) as vg250_compound