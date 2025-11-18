export interface User {
  userId: number;
  firstName: string;
  lastName: string;
  username: string;
  password?: string;
  emailId: string;
  phoneNumber?: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
  captchaVerified: boolean;
}

export interface AuthResponse {
  token: string;
  type: string;
  userId: number;
  username: string;
  firstName: string;
  lastName: string;
  emailId: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  emailId: string;
  phoneNumber?: string;
}

export interface PasswordResetRequest {
  emailId: string;
  captchaVerified: boolean;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  captchaVerified: boolean;
}

