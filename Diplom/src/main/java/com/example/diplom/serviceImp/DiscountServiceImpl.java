package com.example.diplom.serviceImp;

import com.example.diplom.dao.DiscountRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Discount;
import com.example.diplom.domain.Product;
import com.example.diplom.dto.DiscountDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<DiscountDTO> getAll() {
        List<Discount> discounts = discountRepository.findAll();
        List<DiscountDTO> dtos = new ArrayList<>();
        for (Discount d : discounts) {
            dtos.add(new DiscountDTO(d));
        }
        return dtos;
    }

    @Override
    public boolean save(double discount, Long productId) {
        Discount savedDiscount = discountRepository.findFirstByProductId(productId);
        if (savedDiscount == null) {
            try {
                Product product = productRepository.findFirstById(productId);
                Discount newDiscount = Discount.builder()
                        .discount_price(discount)
                        .product(product)
                        .build();
                discountRepository.save(newDiscount);
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        } else {
            savedDiscount.setDiscount_price(discount);
            discountRepository.save(savedDiscount);
            return true;
        }

    }

    @Override
    public void remove(Long discountId) {
        discountRepository.deleteById(discountId);
    }

    @Override
    public DiscountDTO findDiscountByProductId(Long productId) {
        Discount discount = discountRepository.findFirstByProductId(productId);
        if (discount != null) {
            return DiscountDTO.builder()
                    .id(discount.getId())
                    .product_id(discount.getProduct().getId())
                    .discount_price(discount.getDiscount_price())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public List<DiscountDTO> findDiscountsByProducts(List<ProductDTO> productDTOList){
        List<DiscountDTO> dtos = new ArrayList<>();
        for (ProductDTO p : productDTOList){
            dtos.add(findDiscountByProductId(p.getId()));
        }
        return dtos;
    }
}
