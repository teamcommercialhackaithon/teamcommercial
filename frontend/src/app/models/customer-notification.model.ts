export interface CustomerNotification {
  notificationId?: number;
  customerId: string;
  macAddress?: string;
  ipAddress?: string;
  serial?: string;
  startDate: string;
  endDate?: string;
  type?: string;
  notified: boolean;
  createdAt?: string;
  updatedAt?: string;
}

