import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  selectedProduct: Product | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';

  newProduct: Product = {
    name: '',
    description: '',
    price: 0,
    quantity: 0
  };

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.errorMessage = '';
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load products. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading products:', error);
      }
    });
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedProduct = null;
    this.newProduct = {
      name: '',
      description: '',
      price: 0,
      quantity: 0
    };
    this.clearMessages();
  }

  createProduct(): void {
    this.productService.createProduct(this.newProduct).subscribe({
      next: (product) => {
        this.products.push(product);
        this.isCreating = false;
        this.successMessage = 'Product created successfully!';
        this.clearMessages(3000);
        this.newProduct = { name: '', description: '', price: 0, quantity: 0 };
      },
      error: (error) => {
        this.errorMessage = 'Failed to create product.';
        console.error('Error creating product:', error);
      }
    });
  }

  editProduct(product: Product): void {
    this.selectedProduct = { ...product };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateProduct(): void {
    if (this.selectedProduct && this.selectedProduct.id) {
      this.productService.updateProduct(this.selectedProduct.id, this.selectedProduct).subscribe({
        next: (updatedProduct) => {
          const index = this.products.findIndex(p => p.id === updatedProduct.id);
          if (index !== -1) {
            this.products[index] = updatedProduct;
          }
          this.isEditing = false;
          this.selectedProduct = null;
          this.successMessage = 'Product updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update product.';
          console.error('Error updating product:', error);
        }
      });
    }
  }

  deleteProduct(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.products = this.products.filter(p => p.id !== id);
          this.successMessage = 'Product deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete product.';
          console.error('Error deleting product:', error);
        }
      });
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedProduct = null;
    this.clearMessages();
  }

  clearMessages(delay: number = 0): void {
    if (delay > 0) {
      setTimeout(() => {
        this.errorMessage = '';
        this.successMessage = '';
      }, delay);
    } else {
      this.errorMessage = '';
      this.successMessage = '';
    }
  }
}

