package com.dlshopping.shoppingcart.service;

import com.dlshopping.shoppingcart.entity.Product;
import com.dlshopping.shoppingcart.form.ProductForm;
import com.dlshopping.shoppingcart.model.CartInfo;
import com.dlshopping.shoppingcart.model.ProductInfo;
import com.dlshopping.shoppingcart.repositories.ProductRepository;
import com.dlshopping.shoppingcart.utils.SessionUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products
     * @return List of products
     */
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    /**
    * Get product by code
    * @param code
    * @return Product or null
    */
    public Product getProductByCode(String code){
        return productRepository.findById(code).orElse(null);
    }


    /**
     * Get products with pagination
     * @param pageable
     * @return PaginationResult of products
     */
    public Page getProductsWithPage(Pageable pageable){
        Page<Product> productPages = productRepository.findAll(pageable);
        return productPages;
    }

    /**
     * Create a new product
     * @param productForm
     * @throws IOException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    public void creatProduct(ProductForm productForm) throws IOException {
        productRepository.save(SessionUtils.productMapping(productForm));
    }

    /**
     * Update an existing product
     * @param productForm
     * @param product
     * @throws IOException
     */
    public void updateProduct(ProductForm productForm, Product product) throws IOException {
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setImage(productForm.getFileData().getBytes());
        productRepository.save(product);
    }

    /**
     * Creates or update a product
     * @param productForm
     * @throws IOException
     */
    public void saveOrUpdateProduct(ProductForm productForm) throws IOException {
        Product product = getProductByCode(productForm.getCode());
        if (product != null ) updateProduct(productForm, product);
        else creatProduct(productForm);
    }

    /**
     * Creates a new productInfo from a product
     * @param product
     * @return ProductInfo
     */
    public ProductInfo productInfoMapping(Product product) {
        return new ProductInfo(product);
    }

    /**
     * Buy products
     * @param session
     * @param code
     * @param quantity
     * @throws NoResultException
     */
    public void buyProduct(HttpSession session, String code, int quantity) throws NoResultException {
        if (code == null || code.isEmpty()) throw new IllegalArgumentException("Le code du produit ne peut pas être nul ou vide");
        if (quantity <= 0) throw new IllegalArgumentException("La quantité doit être supérieure à zéro");
        Product product = getProductByCode(code);

        if (product == null) throw new NoResultException("Aucun produit trouvé pour le code: " + code);

        SessionUtils.storeCartInSession(session);
        CartInfo cartInfo = SessionUtils.getCartInfoSession(session);
        cartInfo.addProduct(productInfoMapping(product), quantity);
    }

    /**
     * Find a product info by code
     * @param code
     * @return
     */
    public ProductInfo findProductInfo(String code) {
        if (code == null || code.isEmpty()) throw new IllegalArgumentException("Le code du produit ne peut pas être nul ou vide.");
        Product product = getProductByCode(code);

        if (product == null) throw new NoResultException("Aucun produit trouvé pour le code : " + code);
        return new ProductInfo(product);
    }

    /**
     * Save a product
     * @param productForm
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(ProductForm productForm) {
        String code = productForm.getCode();
        if (code == null || code.isEmpty()) throw new IllegalArgumentException("Le code du produit ne peut pas être nul ou vide.");

        Product product = productRepository.findById(code).orElse(new Product());
        if (product.getCode() == null) product.setCreateDate(new Date());

        product.setCode(code);
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());

        if (productForm.getFileData() != null) {
            try {
                byte[] image = productForm.getFileData().getBytes();
                if (image != null && image.length > 0) product.setImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la lecture de l'image du produit", e);
            }
        }
        productRepository.save(product);
    }

    public void removeProduct(HttpSession session, String code) {
        Product product = getProductByCode(code);
        if (product == null) throw new EntityNotFoundException("Le produit "+ code +" introuvable");

        productRepository.deleteById(code);
    }
}
