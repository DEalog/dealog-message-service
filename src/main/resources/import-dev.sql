CREATE MATERIALIZED VIEW public.vg250_compound AS
SELECT
    row_number() OVER (ORDER BY ars) AS id,
    *
FROM (
     SELECT DISTINCT ON (ars) ars, gen, bez, geom FROM public.vg250_sta
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom FROM public.vg250_lan
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom FROM public.vg250_rbz
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom FROM public.vg250_krs
        UNION SELECT DISTINCT ON (ars) ars, gen, bez, geom FROM public.vg250_gem
) as foo