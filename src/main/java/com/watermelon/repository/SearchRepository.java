package com.watermelon.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ProductResponse;
import com.watermelon.mapper.imp.ProductMapper;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.ProductQuantity;
import com.watermelon.model.entity.Size;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;
    ProductMapper productMapper;

    public PageResponse<List<ProductResponse>> findProductsByCriteria(
            String name,
            Integer[] brands,
            Integer[] categories,
            Integer[] sizes,
            int pageNo,
            int pageSize,
            Sort sort) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);
        
        Predicate whereClause = buildWhereClause(cb, product, name, brands, categories, sizes);
        cq.where(whereClause).distinct(true);
        addSorting(cb, cq, sort, product);

        Long totalElements = getTotalElements(cb, whereClause, name, brands, categories, sizes);
        TypedQuery<Product> query = entityManager.createQuery(cq)
                .setMaxResults(pageSize)
                .setFirstResult(pageNo * pageSize);

        List<Product> products = query.getResultList();
        List<ProductResponse> productDTOs = productMapper.toDTO(products);

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResponse<>(pageNo, pageSize, totalPages, totalElements, productDTOs);
    }

    private Predicate buildWhereClause(CriteriaBuilder cb, Root<Product> product, 
            String name, Integer[] brands, Integer[] categories, Integer[] sizes) {
        
        Predicate whereClause = cb.conjunction();
        
        if (StringUtils.hasLength(name)) {
            whereClause = cb.and(whereClause, cb.like(product.get("name"), "%" + name + "%"));
        }
        
        if (brands != null && brands.length > 0) {
            whereClause = cb.and(whereClause, product.get("brand").get("id").in(Arrays.asList(brands)));
        }
        
        if (categories != null && categories.length > 0) {
            whereClause = cb.and(whereClause, product.get("category").get("id").in(Arrays.asList(categories)));
        }
        
        if (sizes != null && sizes.length > 0) {
            Join<Product, ProductQuantity> quantityJoin = product.join("quantityOfSizes", JoinType.INNER);
            Join<ProductQuantity, Size> sizeJoin = quantityJoin.join("size", JoinType.INNER);
            whereClause = cb.and(whereClause, sizeJoin.get("id").in(Arrays.asList(sizes)));
        }
        whereClause = cb.and(whereClause, cb.isTrue(product.get("isActive")));

        return whereClause;
    }

    private void addSorting(CriteriaBuilder cb, CriteriaQuery<Product> cq, Sort sort, Root<Product> product) {
        if (sort.isSorted()) {
            List<Order> orders = sort.stream()
                .map(order -> order.isAscending() 
                		? cb.asc(product.get(order.getProperty())) 
                		: cb.desc(product.get(order.getProperty()))
                )
                .toList();
            cq.orderBy(orders);
        }
    }

    private Long getTotalElements(CriteriaBuilder cb, Predicate whereClause, 
            String name, Integer[] brands, Integer[] categories, Integer[] sizes) {
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        Predicate countWhereClause = buildWhereClause(cb, countRoot, name, brands, categories, sizes);
        countQuery.select(cb.countDistinct(countRoot)).where(countWhereClause);
        
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
