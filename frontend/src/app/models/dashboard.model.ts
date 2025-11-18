export interface DashboardStats {
  totalEvents: number;
  totalNotifications: number;
  eventsByType: { [key: string]: number };
  eventsByDate: { [key: string]: number };
  notificationsByDate: { [key: string]: number };
}

