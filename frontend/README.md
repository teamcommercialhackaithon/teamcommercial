# Team Commercial Frontend - Angular Application

Modern Angular application for managing products with a beautiful, responsive UI.

## Features

- Product CRUD operations (Create, Read, Update, Delete)
- Modern card-based layout
- Responsive design
- Real-time validation
- Success/error message feedback
- Beautiful gradient UI
- Standalone components (Angular 17+)

## Technology Stack

- Angular 17
- TypeScript 5.2
- RxJS 7.8
- HttpClient for API calls
- Standalone components architecture

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   └── product-list/
│   │   │       ├── product-list.component.ts
│   │   │       ├── product-list.component.html
│   │   │       └── product-list.component.css
│   │   ├── models/
│   │   │   └── product.model.ts
│   │   ├── services/
│   │   │   └── product.service.ts
│   │   ├── app.component.ts
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── angular.json
├── package.json
└── tsconfig.json
```

## Prerequisites

- Node.js 18+ and npm
- Angular CLI: `npm install -g @angular/cli`

## Installation

1. Install dependencies:
```bash
npm install
```

## Running the Application

Start the development server:
```bash
npm start
```
or
```bash
ng serve
```

The application will be available at `http://localhost:4200`

## Backend Connection

The frontend connects to the backend API at `http://localhost:8080`. Make sure the backend is running before starting the frontend.

API endpoint configuration is in `src/app/services/product.service.ts`:
```typescript
private apiUrl = 'http://localhost:8080/api/products';
```

## Building for Production

```bash
ng build --configuration production
```

The build artifacts will be stored in the `dist/` directory.

## Features Overview

### Product List
- View all products in a beautiful card grid
- Each card displays product name, description, price, and quantity
- Responsive grid that adapts to screen size

### Create Product
- Click "Add New Product" button
- Fill in product details (name, description, price, quantity)
- Form validation ensures required fields are filled
- Success message on creation

### Edit Product
- Click "Edit" button on any product card
- Update product information
- Changes are saved immediately
- Success message on update

### Delete Product
- Click "Delete" button on any product card
- Confirmation dialog before deletion
- Product removed from list
- Success message on deletion

## Component Details

### ProductListComponent
Main component handling all product operations:
- `loadProducts()` - Fetches all products from API
- `createProduct()` - Creates a new product
- `updateProduct()` - Updates existing product
- `deleteProduct()` - Deletes a product
- `editProduct()` - Opens edit form for a product

### ProductService
Service for API communication:
- `getAllProducts()` - GET all products
- `getProductById(id)` - GET product by ID
- `createProduct(product)` - POST new product
- `updateProduct(id, product)` - PUT update product
- `deleteProduct(id)` - DELETE product
- `searchProducts(name)` - GET search products
- `getProductsInStock()` - GET in-stock products

## Styling

The application uses custom CSS with:
- Gradient backgrounds
- Modern card design
- Smooth transitions and hover effects
- Responsive grid layout
- Mobile-friendly design

Main color scheme:
- Primary: Purple gradient (#667eea to #764ba2)
- Danger: Pink gradient (#f093fb to #f5576c)
- Background: White cards on gradient background

## Development

### Adding New Features

1. Create new component:
```bash
ng generate component components/new-component --standalone
```

2. Add route in `app.routes.ts`
3. Implement component logic
4. Add styles

### Code Scaffolding

Generate service:
```bash
ng generate service services/new-service
```

Generate interface:
```bash
ng generate interface models/new-model
```

## Testing

Run unit tests:
```bash
ng test
```

## Troubleshooting

### Cannot connect to backend
- Ensure backend is running on port 8080
- Check browser console for CORS errors
- Verify API URL in `product.service.ts`

### Port 4200 already in use
Angular CLI will prompt to use a different port automatically, or specify:
```bash
ng serve --port 4201
```

### Module not found errors
Reinstall dependencies:
```bash
rm -rf node_modules package-lock.json
npm install
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

This project is created for Team Commercial hackathon purposes.

