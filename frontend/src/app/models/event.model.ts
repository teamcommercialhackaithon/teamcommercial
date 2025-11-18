export interface Event {
  eventId?: number;
  type?: string;
  macAddress?: string;
  serial?: string;
  message?: string;
  date: string;
  payload?: string; // JSON string payload (stored as BLOB in database)
  processed?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

