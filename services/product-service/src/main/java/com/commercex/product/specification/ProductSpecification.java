package com.commercex.product.specification;

import com.commercex.product.dto.request.ProductSearchRequest;
import com.commercex.product.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> search(ProductSearchRequest request){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(request.getKeyword()!=null && !request.getKeyword().isBlank()){

                predicates.add(

                        cb.like(

                                cb.lower(root.get("name")),

                                "%" + request.getKeyword().toLowerCase() + "%"

                        )
                );
            }

            if(request.getBrand()!=null){

                predicates.add(

                        cb.equal(

                                root.get("brand").get("name"),

                                request.getBrand()

                        )
                );
            }

            if(request.getCategory()!=null){

                predicates.add(

                        cb.equal(

                                root.get("category").get("name"),

                                request.getCategory()

                        )
                );
            }

            if(request.getStatus()!=null){

                predicates.add(

                        cb.equal(

                                root.get("status"),

                                request.getStatus()

                        )
                );
            }

            if(request.getMinPrice()!=null){

                predicates.add(

                        cb.greaterThanOrEqualTo(

                                root.get("price"),

                                request.getMinPrice()

                        )
                );
            }

            if(request.getMaxPrice()!=null){

                predicates.add(

                        cb.lessThanOrEqualTo(

                                root.get("price"),

                                request.getMaxPrice()

                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }

}