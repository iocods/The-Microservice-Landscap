package com.iocodes.app.api.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(
        name = "Product Service",
        description = "This service is called for requests regarding products; these includes addition of a new product, deletion of a product and more"
)
public interface AbstractProductCompositeController {

    @Operation(
        description = "Requests for a product by its id"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "422", description = "Product request could not be processed"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request could not process request")
        }
    )
    @GetMapping(value = "product-composite/{productId}", produces = "application/json")
    ProductAggregate getProductById (@PathVariable("productId") int productId);
}
