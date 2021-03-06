package de.dealog.msg.service;

import com.google.common.base.Strings;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.persistence.model.RegionEntity;
import de.dealog.msg.persistence.model.RegionType;
import de.dealog.msg.persistence.repository.RegionRepository;
import de.dealog.msg.service.model.PagedList;
import de.dealog.msg.service.model.QueryParams;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class RegionService {

    public static final String QUERY_PARAM_POINT = "point";

    RegionRepository regionRepository;

    @Inject
    public RegionService(final RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public Optional<Region> findOne(final String ars) {
        log.debug("Find region by ars {}", ars);
        final RegionEntity byIdentifier = regionRepository.findByArs(ars);
        return Optional.ofNullable(byIdentifier);
    }

    public PagedList<? extends Region> findAll(final String name,
                                               final List<RegionType> types,
                                               final int page,
                                               final int size) {
        log.debug("List regions by page {}, size {}, name '{}' and types '{}'...", page, size, name, types);
        final PanacheQuery<RegionEntity> regionQuery;
        final Parameters parameters = new Parameters();

        String query = "";
        if(!Strings.isNullOrEmpty(name)) {
            query = "select r from RegionEntity r where lower(name) like '%" + name.toLowerCase() + "%'";
            if (types.size() > 0) {
                query += " and";
            }
        }
        if (types.size() > 0) {
            parameters.and("types", types);
            query += " type in :types";
        }

        regionQuery = regionRepository.find(query, Sort.ascending("name"), parameters);

        final List<RegionEntity> list = regionQuery.page(page, size).list();

        log.debug("... return {} / {} regions ", list.size(), regionQuery.count());

        return PagedList.<Region>builder()
                .page(page)
                .pageSize(size)
                .content(list)
                .count(regionQuery.count())
                .pageCount(regionQuery.pageCount())
                .build();
    }

    public PagedList<? extends Region> findHierarchy(final QueryParams queryParams, final int page, final int size) {
        log.debug("List regions for page {}, size {} and queryParams '{}' ...", page, size, queryParams);
        final PanacheQuery<RegionEntity> regionQuery;
        final StringBuilder queryBuilder = new StringBuilder();
        final Parameters parameters = new Parameters();

        final List<String> codes = new ArrayList<>(Arrays.asList("000000000000"));
        queryParams.maybeRegionalCode().ifPresent(regionalCode -> {
            codes.addAll(regionalCode.asList());

            queryBuilder.append(" ars IN (")
                    .append(codes.stream().map(c -> ("'" + c + "'")).collect(Collectors.joining(",")))
                    .append(") ");
        });

        queryParams.maybePoint().ifPresent(point -> {
            parameters.and(QUERY_PARAM_POINT, point);
            queryBuilder.append(" within(:point, geometries) = true");
        });

        regionQuery = regionRepository.find(queryBuilder.toString(), parameters);

        final List<RegionEntity> list = regionQuery.page(page, size).list();

        log.debug("... return {} / {} regions ", list.size(), regionQuery.count());

        return PagedList.<Region>builder()
                .page(page)
                .pageSize(size)
                .content(list)
                .count(regionQuery.count())
                .pageCount(regionQuery.pageCount())
                .build();
    }
}
